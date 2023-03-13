package principal.mapas;

/**
 * Control capa de tiles
 *
 * @author Dylan
 */
public abstract class CapaTiled {

    protected int ancho;
    protected int alto;
    protected int x;
    protected int y;

    public CapaTiled(final int ancho, final int alto, final int x, final int y) {
        this.ancho = ancho;
        this.alto = alto;
        this.x = x;
        this.y = y;
    }

}
