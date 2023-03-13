package principal.maquinaestado.estado.juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.ElementosPrincipales;
import static principal.GestorPrincipal.ge;
import principal.control.GestorControles;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DatoOpcion;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.MedidorString;
import principal.interface_usuario.MenuInferior;
import principal.maquinaestado.EstadoJuego;

/**
 * Clase de control total del juego
 *
 * @author Dylan
 */
public class GestorJuego implements EstadoJuego {

    BufferedImage logotipo;
    MenuInferior menuInferior;

    private static boolean primeraEntrada = true;
    private static int contador = 0;

    public GestorJuego() {

        menuInferior = new MenuInferior();
        logotipo = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.RUTA_LOGOTIPO);
    }

    @Override
    public void actualizar() {

        if (primeraEntrada) {
            if (contador == 1000) {
                primeraEntrada = false;
            }
            contador++;
        }

        ElementosPrincipales.jugador.actualizar();
        ElementosPrincipales.mapa.actualizar();

        if (GestorControles.teclado.inventario) {
            ge.cambiarEstadoActual(2);
        }
        if (GestorControles.teclado.menuPausa) {
            ge.cambiarEstadoActual(5);
            GestorControles.teclado.menuPausa = true;
        }
    }

    @Override
    public void dibujar(Graphics g) {

        ElementosPrincipales.mapa.dibujar(g);
        ElementosPrincipales.jugador.dibujar(g);
        menuInferior.dibujar(g);
        DibujoOpciones.dibujarImagen(g, logotipo, Constantes.ANCHO_JUEGO - logotipo.getWidth() - 5, 5);

        if (primeraEntrada) {
            DibujoOpciones.dibujarRectRelleno(g, new Rectangle(Constantes.CENTRO_VENTANA_X - 65, 0, 130, 30), Color.WHITE);
            Font font = new Font("Arial", Font.BOLD, 7);
            g.setFont(font);
            DibujoOpciones.dibujarString(g, "Con la letra \"e\" recoges los objetos", new Point(Constantes.CENTRO_VENTANA_X - 63,
                    2 + MedidorString.medirAltoPixeles(g, "Con la letra \"e\" recoges los objetos")), Color.BLACK);
            DibujoOpciones.dibujarString(g, "Abres el inventario con la letra \"i\"", new Point(Constantes.CENTRO_VENTANA_X - 59,
                    15 + MedidorString.medirAltoPixeles(g, "Abres el inventario con la letra \"i\"")), Color.BLACK);
        }

        DatoOpcion.enviarDato((int) ElementosPrincipales.jugador.getPosicionXINT() + " ");
        DatoOpcion.enviarDato((int) ElementosPrincipales.jugador.getPosicionYINT() + " ");
    }
}
