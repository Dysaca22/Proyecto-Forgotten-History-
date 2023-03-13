package principal.herramientas;

//Medir el alto y ancho de un String en pixeles
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * métodos estáticos para medir el ancho y alto de las fuentes
 */
public class MedidorString {

    /**
     * Obtener el ancho de la fuente y la palabra
     * @param g objeto de graficos Graphics
     * @param palabra palabra cuyo ancho se desea medir
     * @return ancho en formato de entero
     */
    public static int medirAnchoPixeles(final Graphics g, String palabra) {
        FontMetrics fm = g.getFontMetrics();
        return fm.stringWidth(palabra);
    }
    
    
    /**
     * Obtener el alto de la fuente y la palabra
     * @param g objeto de graficos Graphics
     * @param palabra palabra cuyo alto se desea medir
     * @return alto en formato de entero
     */
    public static int medirAltoPixeles(final Graphics g, String palabra) {
        FontMetrics fm = g.getFontMetrics();
        return (int) (fm.getLineMetrics(palabra, g).getHeight());
    }
}
