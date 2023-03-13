package principal.maquinaestado.estado;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;
import principal.sonido.GestorSonido;

/**
 * Vista que aparece cuando muere el jugador, indicando que perdio, y brindandole
 * la opcion de volver a jugar o salir al menú inicial.
 */
public class GameOver implements EstadoJuego {

    private final SuperficieDibujo sd;

    private final BufferedImage gameOver = Constantes.GAMEOVER;
    private final BufferedImage volverAlJuego = Constantes.VOLVERALJUEGO;
    private final BufferedImage volverAlJuegoConMouse = Constantes.VOLVERALJUEGOCONMOUSE;
    private final BufferedImage salir = Constantes.SALIRMENU;
    private final BufferedImage salirConMouse = Constantes.SALIRMENUCONMOUSE;
    private final BufferedImage quiereSalir = Constantes.QUIERESALIR;
    private final BufferedImage salirSi = Constantes.QUIERESALIRSI;
    private final BufferedImage salirNo = Constantes.QUIERESALIRNO;
    private final BufferedImage salirSiConMouse = Constantes.QUIERESALIRSICONMOUSE;
    private final BufferedImage salirNoConMouse = Constantes.QUIERESALIRNOCONMOUSE;

    private BufferedImage volverActual;
    private BufferedImage salirActual;
    private BufferedImage siActual;
    private BufferedImage noActual;

    private final Rectangle volverAlJuegoR;
    private final Rectangle salirR;

    private Rectangle si = null;
    private Rectangle no = null;
    private boolean seguroSalir = false;
    private final String separador = System.getProperty("file.separator");
    
    private final GestorSonido boton = new GestorSonido("sonidos" + separador + "boton.wav");

    private Rectangle r;

    private int tiempoEspera;

    public GameOver(final SuperficieDibujo sd) {
        this.sd = sd;

        volverAlJuegoR = new Rectangle(Constantes.CENTRO_VENTANA_X - volverAlJuego.getWidth() / 2, 180, volverAlJuego.getWidth(), volverAlJuego.getHeight());
        salirR = new Rectangle(volverAlJuegoR.x, volverAlJuegoR.y + 40, salir.getWidth(), salir.getHeight());

        volverActual = volverAlJuego;
        salirActual = salir;

        tiempoEspera = 0;
    }

    @Override
    public void actualizar() {
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (seguroSalir) {
            if (r.intersects(no)) {
                noActual = salirNoConMouse;
                if (sd.getRaton().isClickIzquierdo()) {
                    tiempoEspera = 10;
                    seguroSalir = false;
                }
            } else {
                noActual = salirNo;
            }
            if (r.intersects(si)) {
                siActual = salirSiConMouse;
                if (sd.getRaton().isClickIzquierdo()) {
                    GestorPrincipal.ge.cambiarEstadoActual(0);
                    seguroSalir = false;
                    tiempoEspera = 5;
                }
            } else {
                siActual = salirSi;
            }
            return;
        }
        if (tiempoEspera > 0) {
            tiempoEspera--;
            return;
        }
        if (r.intersects(salirR)) {
            boton.reproducir();
            salirActual = salirConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                seguroSalir = true;
                asignarSeguroSalir();
            }
        } else {
            salirActual = salir;
        }
        if (r.intersects(volverAlJuegoR)) {
            volverActual = volverAlJuegoConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                ElementosPrincipales.jugador.renacer();
                GestorPrincipal.ge.cambiarEstadoActual(1);
            }
        } else {
            volverActual = volverAlJuego;
        }
    }

    private void asignarSeguroSalir() {
        si = new Rectangle(Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 40, Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50,
                salirSi.getWidth(), salirSi.getHeight());
        no = new Rectangle(Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 90, Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50,
                salirNo.getWidth(), salirNo.getHeight());
    }

    @Override
    public void dibujar(Graphics g) {
        DibujoOpciones.dibujarImagen(g, gameOver, new Point(0, 0));

        DibujoOpciones.dibujarImagen(g, volverActual, new Point(volverAlJuegoR.x, volverAlJuegoR.y));
        DibujoOpciones.dibujarImagen(g, salirActual, new Point(salirR.x, salirR.y));

        if (seguroSalir) {
            DibujoOpciones.dibujarImagen(g, quiereSalir, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2);
            DibujoOpciones.dibujarImagen(g, siActual, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 40,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50);
            DibujoOpciones.dibujarImagen(g, noActual, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 90,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50);
        }
    }

}
