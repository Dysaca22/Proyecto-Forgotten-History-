package principal.mapas;

/**
 * Control de capas de sprites del mapa
 *
 * @author Dylan
 */
public class CapaSprites extends CapaTiled {

    private final int[] sprites;

    public CapaSprites(int ancho, int alto, int x, int y, int[] sprites) {
        super(ancho, alto, x, y);
        this.sprites = sprites;
    }

    public int[] getSprites() {
        return sprites;
    }

}
