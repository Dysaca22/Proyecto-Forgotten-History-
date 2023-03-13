package principal.maquinaestado.estado.menujuego.itemsMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import static principal.GestorPrincipal.sd;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorComentario;
import principal.herramientas.MedidorString;
import principal.inventario.Objeto;

/**
 * Menú de inventario.
 */
public class MenuInventario extends PlantillaMenu {

    //Cantidad de items total que cabe en el inventario
    private final int capacidadMaxima = ElementosPrincipales.jugador.limitePeso;
    //Cantidad de items que lleva le personaje actualmente
    private final int capacicadActual = ElementosPrincipales.jugador.pesoActual;

    private final Rectangle barraCapacidad;

    private Objeto botiquin;
    private Rectangle botiquinR;
    private boolean inicializarBotiquin = true;

    private Rectangle libroR;
    private boolean inicializarLibro = true;
    
    private int tiempoEspera;
    private Rectangle r;

    public MenuInventario(String nombreEtiqueta, Rectangle etiqueta, FormaMenu formaMenu) {
        super(nombreEtiqueta, etiqueta, formaMenu);

        int anchoBarra = 80;
        int altoBarra = 9;

        botiquinR = new Rectangle();
        libroR = new Rectangle();
        tiempoEspera = 0;

        barraCapacidad = new Rectangle(Constantes.ANCHO_JUEGO - anchoBarra - margenGeneral + 1, formaMenu.SUPERIOR.height + margenGeneral, anchoBarra, altoBarra);
    }

    @Override
    public void actualizar() {
        actualizarPosicionesMenu();
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (tiempoEspera > 0) {
            tiempoEspera--;
            return;
        }

        if (r.intersects(botiquinR)) {
            if (sd.getRaton().isClickIzquierdo()) {
                botiquin.reducirCantidad(1);
                tiempoEspera = 10;
            }
        }
        if (r.intersects(libroR)) {
            if (sd.getRaton().isClickIzquierdo()) {
                GestorPrincipal.ge.cambiarEstadoActual(8);
                tiempoEspera = 10;
            }
        }
    }
    
    /**
     * Actualiza y organiza la posición de los items en el inventario.
     */
    private void actualizarPosicionesMenu() {
        if (!ElementosPrincipales.inventario.getObjetosConsumibles().isEmpty()) {
            for (int i = 0; i < ElementosPrincipales.inventario.getObjetosConsumibles().size(); i++) {
                final Point puntoInicial = new Point(formaMenu.CENTRAL.x + margenGeneral, formaMenu.CENTRAL.y + formaMenu.CENTRAL.height + margenGeneral);
                final int lado = Constantes.LADO_SPRITE;
                int idActual = ElementosPrincipales.inventario.getObjetosConsumibles().get(i).getId();
                ElementosPrincipales.inventario.getObjeto(idActual).setPosicionEnMenu(new Rectangle(puntoInicial.x + i * (lado + margenGeneral), puntoInicial.y, lado, lado));
            }
        }
    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        dibujarLimiteCapacidad(g);
        dibujarElementosInventario(g);
        //Es necesario escalar la barra de peso para comprobar con el raton, porque ya esta escalado
        if (sd.getRaton().getPosicionRectangulo().intersects(EscaladorElementos.escalarRectanguloArriba(barraCapacidad))) {
            //Nueva fuente para el texto porque no dejaba poner porcentaje (%)
            Font font = new Font("Agency FB", Font.BOLD, 7);
            g.setFont(font);
            GeneradorComentario.dibujarComentario(g, sd, (capacidadMaxima - capacicadActual) + "% de capacidad de inventario libre");
        }
    }

    private void dibujarLimiteCapacidad(final Graphics g) {
        //Dibujar el rectangulo que dice que capacidad tenemos
        //(capacidadMaxima/capacicadActual), esto es para detectar el porcentaje de la barra que esta llena
        final Rectangle capacidad = new Rectangle(barraCapacidad.x + 1, barraCapacidad.y + 1, barraCapacidad.width / (capacidadMaxima / capacicadActual), barraCapacidad.height - 2);

        //Dibujar la barra
        DibujoOpciones.dibujarString(g, "Capacidad", barraCapacidad.x - 35, barraCapacidad.y + 7, Color.BLACK);
        DibujoOpciones.dibujarRectRelleno(g, barraCapacidad, Color.DARK_GRAY);
        DibujoOpciones.dibujarRectRelleno(g, capacidad, Constantes.COLOR_VERDE_CLARO);
    }

    private void dibujarElementosInventario(final Graphics g) {
        if (!ElementosPrincipales.inventario.getObjetosConsumibles().isEmpty()) {
            final Point puntoInicial = new Point(formaMenu.CENTRAL.x + 16, barraCapacidad.y + barraCapacidad.height + margenGeneral);
            final int lado = Constantes.LADO_SPRITE;

            for (int i = 0; i < ElementosPrincipales.inventario.getObjetosConsumibles().size(); i++) {
                int idObjeto = ElementosPrincipales.inventario.getObjetosConsumibles().get(i).getId();
                Objeto objeto = ElementosPrincipales.inventario.getObjeto(idObjeto);

                DibujoOpciones.dibujarImagen(g, objeto.getSprite().getImagen(), puntoInicial.x + i * (32 + margenGeneral), puntoInicial.y);
                if (objeto.getNombre().equals("Botiquin") && inicializarBotiquin) {
                    botiquin = objeto;
                    botiquinR = new Rectangle(puntoInicial.x + i * (32 + margenGeneral), puntoInicial.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    inicializarBotiquin = false;
                }
                if (objeto.getNombre().equals("Libro") && inicializarLibro) {
                    libroR = new Rectangle(puntoInicial.x + i * (32 + margenGeneral), puntoInicial.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    inicializarLibro = false;
                }
                ElementosPrincipales.jugador.pesoActual += objeto.getCantidad();
                DibujoOpciones.dibujarRectRelleno(g, puntoInicial.x + i * (32 + margenGeneral) + 20, puntoInicial.y + 24, 12, 8, Color.BLACK);
                String texto = "";
                if (ElementosPrincipales.inventario.getObjetosConsumibles().get(i).getCantidad() < 10) {
                    texto = "0" + objeto.getCantidad();
                } else {
                    texto = "" + objeto.getCantidad();
                }
                DibujoOpciones.dibujarString(g, texto, puntoInicial.x + i * (lado + margenGeneral) + lado - MedidorString.medirAnchoPixeles(g, texto),
                        puntoInicial.y + 31, Color.WHITE);
            }
        }
    }

}
