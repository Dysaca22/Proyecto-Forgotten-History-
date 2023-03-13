package principal.mapas;

import java.awt.Rectangle;

/**
 * Control de capa de colisiones del mapa
 *
 * @author Dylan
 */
public class CapaColisiones extends CapaTiled {

    private final Rectangle[] colisionables;

    public CapaColisiones(int ancho, int alto, int x, int y, Rectangle[] colisionables) {
        super(ancho, alto, x, y);
        this.colisionables = colisionables;
    }

    public Rectangle[] getColisionables() {
        return colisionables;
    }

}
