package principal.sprites;

import java.awt.image.BufferedImage;

/**
 * Creando una instancia de esta clase, permite extraer y almacenar la
 * informarción de un sprite en especifico, de una spriteSheet, permitiendo luego
 * utilizar está información para dibujar en patalla el sprite, o modificar su información.
 * @see HojaSprites
 */
public class Sprite {

    private final BufferedImage imagen;

    private final int ancho;
    private final int alto;
    
    /**
     * @param imagen Imagen del Sprite en forma de BufferedImage.
     */
    public Sprite(final BufferedImage imagen) {

        this.imagen = imagen;

        ancho = imagen.getWidth();
        alto = imagen.getHeight();
    }
    
    public BufferedImage getImagen() {

        return imagen;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}
