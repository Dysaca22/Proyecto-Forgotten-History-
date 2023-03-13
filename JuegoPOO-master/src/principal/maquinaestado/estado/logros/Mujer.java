package principal.maquinaestado.estado.logros;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.GestorPrincipal;
import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;
import principal.sonido.GestorSonido;

/**
 * Biografia de las mujeres ya rescatadas
 *
 * @author Dylan
 */
public class Mujer implements EstadoJuego {

    private final SuperficieDibujo sd;
    private final GestorSonido boton = Constantes.BOTON;

    private int numSalida = 0;

    private BufferedImage imagenBiografia;

    private final BufferedImage volver = Constantes.VOLVERGRANDE;
    private final BufferedImage volverConMouse = Constantes.VOLVERCONMOUSEGRANDE;
    private BufferedImage volverActual;
    private final Rectangle volverR;

    private Rectangle r;

    public Mujer(final SuperficieDibujo sd) {
        this.sd = sd;

        volverR = new Rectangle(5, Constantes.ALTO_JUEGO - volver.getHeight() - 9, volver.getWidth(), volver.getHeight());
    }

    @Override
    public void actualizar() {
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(6);
                asignarValor();
            }
        } else {
            volverActual = volver;
        }
        comprobarSalida();
    }

    /**
     * Permite volver al menu de logros
     */
    private void asignarValor() {
        switch (numSalida) {
            case 1:
                todo();
                break;
            case 2:
                todo();
                break;
            case 3:
                todo();
                break;
            case 4:
                todo();
                break;
            case 5:
                todo();
                break;
        }
    }

    /**
     * Permite volver al juego
     */
    private void comprobarSalida() {
        switch (numSalida) {
            case 1:
                if (!GestorControles.teclado.num1) {
                    GestorPrincipal.ge.cambiarEstadoActual(1);
                    todo();
                }
                break;
            case 2:
                if (!GestorControles.teclado.num2) {
                    GestorPrincipal.ge.cambiarEstadoActual(1);
                    todo();
                }
                break;
            case 3:
                if (!GestorControles.teclado.num3) {
                    GestorPrincipal.ge.cambiarEstadoActual(1);
                    todo();
                }
                break;
            case 4:
                if (!GestorControles.teclado.num4) {
                    GestorPrincipal.ge.cambiarEstadoActual(1);
                    todo();
                }
                break;
            case 5:
                if (!GestorControles.teclado.num5) {
                    GestorPrincipal.ge.cambiarEstadoActual(1);
                    todo();
                }
                break;
        }
    }

    /**
     * Verificacion de que todos las teclas de numero esten desactivadas
     */
    private void todo() {
        GestorControles.teclado.num1 = false;
        GestorControles.teclado.num2 = false;
        GestorControles.teclado.num3 = false;
        GestorControles.teclado.num4 = false;
        GestorControles.teclado.num5 = false;
    }

    @Override
    public void dibujar(Graphics g) {
        DibujoOpciones.dibujarImagen(g, imagenBiografia, new Point(0, 0));
        DibujoOpciones.dibujarImagen(g, volverActual, 5, Constantes.ALTO_JUEGO - volver.getHeight() - 9);
    }

    public void setImagenBiografia(final BufferedImage imagenBiografia) {
        this.imagenBiografia = imagenBiografia;
    }

    public void setNumSalida(int numSalida) {
        this.numSalida = numSalida;
    }

}
