package principal.maquinaestado.estado;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.GestorPrincipal;
import static principal.GestorPrincipal.sd;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;
import principal.sonido.GestorSonido;

/**
 * Vista que muestra las instrucciones iniciales al jugador,
 * acerca del juego
 */
public class Instrucciones implements EstadoJuego {

    private final GestorSonido boton = Constantes.BOTON;

    private final BufferedImage imagenFondo = Constantes.FONDO;

    private final BufferedImage volverGrande = Constantes.VOLVERGRANDE;
    private final BufferedImage volverConMouseGrande = Constantes.VOLVERCONMOUSEGRANDE;

    private BufferedImage volverGrandeActual;

    private final Rectangle volverR;
    private Rectangle r;
    
    private int tiempoEspera;

    public Instrucciones() {
        volverR = new Rectangle(2, Constantes.ALTO_JUEGO - volverGrande.getHeight() - 2, volverGrande.getWidth(), volverGrande.getHeight());
        
        tiempoEspera = 0;
    }

    @Override
    public void actualizar() {
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (tiempoEspera > 0) {
            tiempoEspera--;
            return;
        }
        
        if (r.intersects(volverR)) {
            volverGrandeActual = volverConMouseGrande;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(2);
                tiempoEspera = 2;
            }
        } else {
            volverGrandeActual = volverGrande;
        }
    }

    @Override
    public void dibujar(Graphics g) {
        DibujoOpciones.dibujarImagen(g, imagenFondo, new Point(0, 0));
        DibujoOpciones.dibujarImagen(g, volverGrandeActual, 2, Constantes.ALTO_JUEGO - volverGrande.getHeight() - 2);

        g.setFont(g.getFont().deriveFont(35f));
        DibujoOpciones.dibujarString(g, "INSTRUCCIONES", new Point(220, 64), Color.WHITE);
        Font font = new Font("Agency FB", Font.BOLD, 15);
        g.setFont(font);
        //instrucciones iniciales.
        DibujoOpciones.dibujarString(g, "Te mueves con las teclas \"w\", \"s\", \"d\", \"a\"", new Point(130, 96), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Lanza tu poder con la tecla de \"Espacio\"", new Point(130, 128), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Recorre todo el mapa eliminando a los enemigos que no te permiten avanzar", new Point(130, 160), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Recolecta los items para no olvidar a las mujeres del pasado", new Point(130, 192), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Después de recolectar los items puedes ver tus logros con los números en tu teclado", new Point(130, 224), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Recuerda que tienes una habilidad especial, encuentrala", new Point(130, 256), Color.WHITE);
        DibujoOpciones.dibujarString(g, "Con la tecla \"Shift\" puedes correr", new Point(130, 288), Color.WHITE);
    }

}
