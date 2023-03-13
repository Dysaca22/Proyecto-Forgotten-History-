package principal.herramientas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Proporciona al usuario un conjunto de elementos importantes del juego
 * como son los APS, FPS, algunas coordenadas
 * Funciona cuando se preciona la letra "o" del teclado
 * @author Dylan
 */
public class DatoOpcion {

    private static ArrayList<String> datos = new ArrayList<String>();

    public static void enviarDato(final String dato) {

        datos.add(dato);
    }

    public static void vaciar() {

        datos.clear();
    }

    public static void dibujarDatos(final Graphics g) {

        g.setColor(Color.GRAY);
        for (int i = 0; i < datos.size(); i++) {
            DibujoOpciones.dibujarString(g, datos.get(i), 20, 10 + i * 10);
        }
        datos.clear();
    }
}
