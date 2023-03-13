package principal.maquinaestado.estado.menujuego.itemsMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import principal.Constantes;
import principal.herramientas.DibujoOpciones;

/**
 * Otorga la forma general de los menus del inventario
 *
 * @author Dylan
 */
public class FormaMenu {

    public final Color COLOR_SUPERIOR;
    public final Color COLOR_LATERAL;
    public final Color COLOR_CENTRAL;

    public final Rectangle SUPERIOR;
    public final Rectangle LATERAL;
    public final Rectangle CENTRAL;

    public final int MARGEN_HORIZONTAL;
    public final int MARGEN_VERTICAL;
    public final int ALTO;
    public final int ANCHO;

    public FormaMenu() {

        COLOR_SUPERIOR = new Color(0x2ff40a);
        COLOR_LATERAL = Color.DARK_GRAY;
        COLOR_CENTRAL = Color.LIGHT_GRAY;

        SUPERIOR = new Rectangle(0, 0, Constantes.ANCHO_JUEGO, 30);
        LATERAL = new Rectangle(0, SUPERIOR.height, 140, Constantes.ALTO_JUEGO - SUPERIOR.height);
        CENTRAL = new Rectangle(LATERAL.width, SUPERIOR.height, Constantes.ANCHO_JUEGO - LATERAL.width, Constantes.ALTO_JUEGO - SUPERIOR.height);

        MARGEN_HORIZONTAL = 20;
        MARGEN_VERTICAL = 20;
        ALTO = 20;
        ANCHO = 100;
    }

    public void actualizar() {

    }

    public void dibujar(final Graphics g) {

        DibujoOpciones.dibujarRectRelleno(g, SUPERIOR, COLOR_SUPERIOR);
        DibujoOpciones.dibujarRectRelleno(g, LATERAL, COLOR_LATERAL);
        DibujoOpciones.dibujarRectRelleno(g, CENTRAL, COLOR_CENTRAL);
    }
}
