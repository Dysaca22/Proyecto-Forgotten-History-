package principal.maquinaestado;

import java.awt.Graphics;

/**
 * Clase general de los estados del juego
 *
 * @author Dylan
 */
public interface EstadoJuego {

    /**
     * En las vistas de menús verifica que elementos son presionados, o sobre
     * cuales está encima el cursor. Actualizando la información. <br>
     */
    void actualizar();

    /**
     * Dibuja los elementos de la vista en la pantalla.
     *
     * @param g Elementos Grapchis de Java, que dibuja sobre la
     * superficieDeDibujo(canvas)
     */
    void dibujar(final Graphics g);
}
