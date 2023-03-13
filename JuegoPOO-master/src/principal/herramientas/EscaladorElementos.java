package principal.herramientas;

//Escalara los tamaños de puntos y rectangulos
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;

/**
 * métodos estaticos para escalar puntos y rectangulos 
 */
public class EscaladorElementos {

    /**
     * Escala un rectangulo para hacerlo más grande
     * @param r Rectangulo a escalar con medidas (x,y)
     * @return Rectangulo con medidas (x*FACTOR_Escalado_X, y * FACTOR_ESCALADO_Y)
     */
    public static Rectangle escalarRectanguloArriba(Rectangle r) {
        final Rectangle newRectangle = new Rectangle((int) (r.x * Constantes.FACTOR_ESCALADO_X), (int) (r.y * Constantes.FACTOR_ESCALADO_Y), (int) (r.width * Constantes.FACTOR_ESCALADO_X), (int) (r.height * Constantes.FACTOR_ESCALADO_Y));

        return newRectangle;
    }

    /**
     * Escala las coordenadas de un punto para hacerlo más grande
     * @param p Punto con medidas (x,y);
     * @return  punto con medidas escaladas (x*FACTOR_Escalado_X, y * FACTOR_ESCALADO_Y)
     */
    public static Point escalarPuntoArriba(final Point p) {
        final Point newPoint = new Point((int) (p.x * Constantes.FACTOR_ESCALADO_X), (int) (p.y * Constantes.FACTOR_ESCALADO_Y));

        return newPoint;
    }

    /**
     * Escala las coordenadas de un punto para hacerlo más pequeño
     * @param p Punto con medidas (x,y);
     * @return  punto con medidas escaladas (x* ( 1 / FACTOR_Escalado_X), y * ( 1 / FACTOR_ESCALADO_Y^2) )
     */
    public static Point escalarPuntoAbajo(final Point p) {
        final Point newPoint = new Point((int) (p.x * Math.pow(Constantes.FACTOR_ESCALADO_X, -1)), (int) (p.y * Math.pow(Constantes.FACTOR_ESCALADO_Y, -1)));
        return newPoint;
    }
}
