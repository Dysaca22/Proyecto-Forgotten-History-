package principal.sprites;

import java.awt.image.BufferedImage;
import principal.herramientas.CargadorRecursos;

/**
 * Creando una instancia de esta clase, permite extraer y almacenar la
 * informarción de una spriteSheet, almecenando un arreglo que contiene cada uno
 * de los sprites de la imagen.<br>
 *
 * su método de uso es el siguiente: <br>
 * HojaSprite hoja = new HojaSprite("/ruta/hacia/mi/spriteSheet");<br>
 *
 * para extrar el sprite: <br>
 * Sprite sprite = hoja.getSprite(posicion de mi sprite en la hoja);<br>
 *
 * - la posicion debe ser mayor a 0 y menor a 32.<br>
 *
 * @see Sprite
 */
public class HojaSprites {

    final private int anchoHojaEnPixeles;
    final private int altoHojaEnPixeles;

    final private int anchoHojaEnSprites;
    final private int altoHojaEnSprites;

    final private int anchoSprites;
    final private int altoSprites;

    private Sprite[] sprites;

    /**
     * Constructor para spriteSheets con sprites cuyas medidas sean cuadradas, es decir
     * su ancho y su alto son iguales ej: 32x32. 
     *
     * @param ruta string con la ruta hacia la hoja
     * @param tamSprites alto del archivo
     * @param hojaOpaca booleano que determina si las imagenes se podran
     * atravesar o no.
     */
    public HojaSprites(final String ruta, final int tamSprites, final boolean hojaOpaca) {

        final BufferedImage imagen;

        if (hojaOpaca) {
            imagen = CargadorRecursos.cargarImagenCompatibleOpaca(ruta);
        } else {
            imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(ruta);
        }

        anchoHojaEnPixeles = imagen.getWidth();
        altoHojaEnPixeles = imagen.getHeight();

        anchoHojaEnSprites = anchoHojaEnPixeles / tamSprites;
        altoHojaEnSprites = altoHojaEnPixeles / tamSprites;

        anchoSprites = tamSprites;
        altoSprites = tamSprites;

        sprites = new Sprite[anchoHojaEnSprites * altoHojaEnSprites];

        extraerSpritesDeLaImagen(imagen);
    }

    /**
     * Constructor para spriteSheets con sprites cuyas medidas no sean cuadradas, es decir
     * su ancho y su alto es distinto.
     *
     * @param ruta string con la ruta hacia la hoja
     * @param anchoSprites ancho del archivo
     * @param altoSprites alto del archivo
     * @param hojaOpaca booleano que determina si las imagenes se podran
     * atravesar o no.
     */
    public HojaSprites(final String ruta, final int anchoSprites, final int altoSprites, final boolean hojaOpaca) {

        final BufferedImage imagen;

        if (hojaOpaca) {
            imagen = CargadorRecursos.cargarImagenCompatibleOpaca(ruta);
        } else {
            imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(ruta);
        }

        anchoHojaEnPixeles = imagen.getWidth();
        altoHojaEnPixeles = imagen.getHeight();

        anchoHojaEnSprites = anchoHojaEnPixeles / anchoSprites;
        altoHojaEnSprites = altoHojaEnPixeles / altoSprites;

        this.anchoSprites = anchoSprites;
        this.altoSprites = altoSprites;

        sprites = new Sprite[anchoHojaEnSprites * altoHojaEnSprites];

        extraerSpritesDeLaImagen(imagen);
    }

    private void extraerSpritesDeLaImagen(final BufferedImage imagen) {

        for (int y = 0; y < altoHojaEnSprites; y++) {
            for (int x = 0; x < anchoHojaEnSprites; x++) {

                final int posicionX = x * anchoSprites;
                final int posicionY = y * altoSprites;

                sprites[x + y * anchoHojaEnSprites] = new Sprite(imagen.getSubimage(posicionX, posicionY,
                        anchoSprites, altoSprites));
            }
        }
    }

    public Sprite getSprite(final int indice) {

        return sprites[indice];
    }

    public Sprite getSprite(final int x, final int y) {

        return sprites[x + y * anchoHojaEnSprites];
    }
}
