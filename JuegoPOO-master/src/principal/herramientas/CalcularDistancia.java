package principal.herramientas;

import java.awt.Point;

/**
 * Calcula la distancia entre dos puntos
 *
 * @author Dylan
 */
public class CalcularDistancia {

    /**
     * Calcula la distancia que hay entre dos puntos de la pantalla del
     * ordenador
     *
     * @param punto1 punto inicial (x1, y1)
     * @param punto2 putno final (x2, y2)
     * @return distancia entre los dos puntos ( (x2-x1)^2 + (y2-y1)^2 ) ^ (1/2)
     */
    public static double getDistanciaEntrePuntos(final Point punto1, final Point punto2) {
        return Math.sqrt(Math.pow((punto2.getX() - punto1.getX()), 2) + Math.pow((punto2.getY() - punto1.getY()), 2));
    }
}
