package principal.maquinaestado.estado.menujuego.itemsMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.MedidorString;
import principal.inventario.Objeto;
import principal.inventario.poderes.Cetro;
import principal.inventario.poderes.DesArmado;

/**
 * Menú de equipo.
 */
public class MenuEquipo extends PlantillaMenu {

    final Rectangle panelObjetos = new Rectangle(formaMenu.CENTRAL.x + margenGeneral, formaMenu.SUPERIOR.height + margenGeneral,
            formaMenu.CENTRAL.width - formaMenu.CENTRAL.width / 3 - margenGeneral * 2, Constantes.ALTO_JUEGO - margenGeneral * 2 - formaMenu.SUPERIOR.height);

    final Rectangle panelArmas = new Rectangle(panelObjetos.x + panelObjetos.width + margenGeneral, formaMenu.SUPERIOR.height + margenGeneral,
            formaMenu.CENTRAL.width / 3 - margenGeneral, panelObjetos.height);

    final Rectangle tituloPanelObjetos = new Rectangle(panelObjetos.x, panelObjetos.y, panelObjetos.width, 22);
    final Rectangle tituloPanelArmas = new Rectangle(panelArmas.x, panelArmas.y, panelArmas.width, 22);

    final Rectangle etiquetaArma = new Rectangle(tituloPanelArmas.x + margenGeneral, tituloPanelArmas.y + tituloPanelArmas.height + margenGeneral,
            tituloPanelArmas.width - margenGeneral * 2, margenGeneral + MedidorString.medirAltoPixeles(GestorPrincipal.sd.getGraphics(), "Arma"));

    final Rectangle contenedorArma = new Rectangle(etiquetaArma.x + 1, etiquetaArma.y + etiquetaArma.height, etiquetaArma.width - 2, Constantes.LADO_SPRITE + etiquetaArma.height);

    Objeto objetoSeleccionado;

    public MenuEquipo(String nombreEtiqueta, Rectangle etiqueta, FormaMenu formaMenu) {
        super(nombreEtiqueta, etiqueta, formaMenu);
        objetoSeleccionado = null;
    }

    @Override
    public void actualizar() {
        actualizarPosicionesMenu();
        actualizarSeleccionRaton();
        actualizarObjetoSeleccionado();
    }

     /**
     * Actualiza y organiza la posición de los items en el inventario.
     */
    private void actualizarPosicionesMenu() {
        if (!ElementosPrincipales.inventario.getObjetosCetro().isEmpty()) {
            for (int i = 0; i < ElementosPrincipales.inventario.getObjetosCetro().size(); i++) {
                final Point puntoInicial = new Point(tituloPanelObjetos.x + margenGeneral, tituloPanelObjetos.y + tituloPanelArmas.height + margenGeneral);
                final int lado = Constantes.LADO_SPRITE;
                int idActual = ElementosPrincipales.inventario.getObjetosCetro().get(i).getId();
                ElementosPrincipales.inventario.getObjeto(idActual).setPosicionEnMenu(new Rectangle(puntoInicial.x + i * (lado + margenGeneral), puntoInicial.y, lado, lado));
            }
        }
    }
    
    /**
     * Verifica si el usuario está presionando algun equipo con el cursor.
     */
    private void actualizarSeleccionRaton() {
        Rectangle posicionRaton = GestorPrincipal.sd.getRaton().getPosicionRectangulo();

        if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelObjetos))) {
            if (!ElementosPrincipales.inventario.getObjetosCetro().isEmpty()) {
                for (Objeto objeto : ElementosPrincipales.inventario.getObjetosCetro()) {
                    if (GestorPrincipal.sd.getRaton().isClickIzquierdo() && posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(objeto.getPosicionEnMEnu()))) {
                        objetoSeleccionado = objeto;
                    }
                }
            }
        } else if (posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(panelArmas))) {
            if (objetoSeleccionado != null && objetoSeleccionado instanceof Cetro && GestorPrincipal.sd.getRaton().isClickIzquierdo()
                    && posicionRaton.intersects(EscaladorElementos.escalarRectanguloArriba(contenedorArma))) {
                ElementosPrincipales.jugador.getAlmacenEquipo().cambiarCetro((Cetro) objetoSeleccionado);
                objetoSeleccionado = null;
            }
        }
    }

    private void actualizarObjetoSeleccionado() {
        if (objetoSeleccionado != null) {
            if (GestorPrincipal.sd.getRaton().isClickDerecho()) {
                objetoSeleccionado = null;
                return;
            }
            Point posicionRaton = EscaladorElementos.escalarPuntoAbajo(GestorPrincipal.sd.getRaton().getPosicion());
            objetoSeleccionado.setPosicionFlotante(new Rectangle(posicionRaton.x, posicionRaton.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE));
        }
    }

    @Override
    public void dibujar(Graphics g, SuperficieDibujo sd) {
        dibujarPaneles(g);
        dibujarPanelArmas(g);
        dibujarElementosEquipables(g, tituloPanelObjetos);
    }

    private void dibujarElementosEquipables(final Graphics g, final Rectangle tituloPanel) {
        if (!ElementosPrincipales.inventario.getObjetosCetro().isEmpty()) {
            final Point puntoInicial = new Point(tituloPanel.x + margenGeneral, tituloPanel.y + tituloPanel.height + margenGeneral);
            final int lado = Constantes.LADO_SPRITE;

            for (int i = 0; i < ElementosPrincipales.inventario.getObjetosCetro().size(); i++) {
                int idObjeto = ElementosPrincipales.inventario.getObjetosCetro().get(i).getId();
                Objeto objeto = ElementosPrincipales.inventario.getObjeto(idObjeto);

                DibujoOpciones.dibujarImagen(g, objeto.getSprite().getImagen(), objeto.getPosicionEnMEnu().x, objeto.getPosicionEnMEnu().y);
                DibujoOpciones.dibujarRectRelleno(g, puntoInicial.x + i * (lado + margenGeneral) + lado - 12, puntoInicial.y + lado - 8, 12, 8, Color.BLACK);
                String texto = "";
                if (ElementosPrincipales.inventario.getObjetosCetro().get(i).getCantidad() < 10) {
                    texto = "0" + objeto.getCantidad();
                } else {
                    texto = "" + objeto.getCantidad();
                }
                DibujoOpciones.dibujarString(g, texto, puntoInicial.x + i * (lado + margenGeneral) + lado - MedidorString.medirAnchoPixeles(g, texto),
                        puntoInicial.y + 31, Color.WHITE);
            }

            if (objetoSeleccionado != null) {
                DibujoOpciones.dibujarImagen(g, objetoSeleccionado.getSprite().getImagen(), new Point(objetoSeleccionado.getPosicionFlotante().x,
                        objetoSeleccionado.getPosicionFlotante().y));
            }
        }
    }

    private void dibujarPanelArmas(final Graphics g) {
        //Dibujar ranuras de armamento
        DibujoOpciones.dibujarRectRelleno(g, etiquetaArma, Color.BLACK);
        DibujoOpciones.dibujarRectBorde(g, contenedorArma, Color.BLACK);
        DibujoOpciones.dibujarString(g, "Arma", new Point(etiquetaArma.x + etiquetaArma.width / 2 - MedidorString.medirAnchoPixeles(g, "Arma") / 2,
                etiquetaArma.y + etiquetaArma.height / 2 + MedidorString.medirAltoPixeles(g, "Arma") / 2), Color.WHITE);
        //Dibujar arma equipada si la hay
        if (!(ElementosPrincipales.jugador.getAlmacenEquipo().getCetro() instanceof DesArmado)) {
            Point coordenadaImagen = new Point(contenedorArma.x + contenedorArma.width / 2 - Constantes.LADO_SPRITE / 2, contenedorArma.y);
            DibujoOpciones.dibujarImagen(g, ElementosPrincipales.jugador.getAlmacenEquipo().getCetro().getSprite().getImagen(), coordenadaImagen);
        }
    }

    private void dibujarPaneles(final Graphics g) {
        DibujoOpciones.dibujarRectBorde(g, panelObjetos, Constantes.COLOR_VERDE_OSCURO);
        DibujoOpciones.dibujarRectBorde(g, panelArmas, Constantes.COLOR_VERDE_OSCURO);
        DibujoOpciones.dibujarRectRelleno(g, tituloPanelObjetos, Constantes.COLOR_VERDE_OSCURO);
        DibujoOpciones.dibujarRectRelleno(g, tituloPanelArmas, Constantes.COLOR_VERDE_OSCURO);

        g.setFont(g.getFont().deriveFont(14f));
        DibujoOpciones.dibujarString(g, "Items Armamento", new Point(tituloPanelObjetos.x + tituloPanelObjetos.width / 2
                - MedidorString.medirAnchoPixeles(g, "Items Armamento") / 2, tituloPanelObjetos.y + tituloPanelObjetos.height / 2
                + MedidorString.medirAltoPixeles(g, "Items Armamento") / 2), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Equipo", new Point(tituloPanelArmas.x + tituloPanelArmas.width / 2
                - MedidorString.medirAnchoPixeles(g, "Equipo") / 2, tituloPanelArmas.y + tituloPanelArmas.height / 2
                + MedidorString.medirAltoPixeles(g, "Equipo") / 2), Color.WHITE);
        g.setFont(g.getFont().deriveFont(11f));
    }

    public Objeto getObjetoSeleccionado() {
        return objetoSeleccionado;
    }

    public void setObjetoSeleccionado() {
        this.objetoSeleccionado = null;
    }

}
