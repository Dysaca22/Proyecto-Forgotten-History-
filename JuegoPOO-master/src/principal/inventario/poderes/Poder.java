package principal.inventario.poderes;

import java.awt.Rectangle;
import java.util.ArrayList;
import principal.Constantes;
import principal.entes.Jugador;

public class Poder extends Cetro {

    public Poder(int id, String nombre, String descripcion, int ataqueMin, int ataqueMax, boolean automatica, boolean penetrante, double ataquePorSegundo) {
        super(id, nombre, descripcion, ataqueMin, ataqueMax, automatica, penetrante, ataquePorSegundo, "/sonidos/Pelea.wav");
    }

    @Override
    public ArrayList<Rectangle> getAlcance(final Jugador jugador) {
        final ArrayList<Rectangle> alcance = new ArrayList();
        final Rectangle alcancePistola = new Rectangle();
        final int alcanceMaximo = 3;

        //Saber a donde esta mirando el jugador
        //0 abajo, 1 arriba, 2 derecha, 3 izquierda
        if (jugador.getDireccion() == 0 || jugador.getDireccion() == 1) {
            alcancePistola.width = 4;
            alcancePistola.height = alcanceMaximo * Constantes.LADO_SPRITE;
            alcancePistola.x = Constantes.CENTRO_VENTANA_X - 18;
            if (jugador.getDireccion() == 0) {
                alcancePistola.y = Constantes.CENTRO_VENTANA_Y - 9;
            } else {
                alcancePistola.y = Constantes.CENTRO_VENTANA_Y - 9 - alcancePistola.height;
            }
        } else {
            alcancePistola.width = alcanceMaximo * Constantes.LADO_SPRITE;
            alcancePistola.height = 4;
            alcancePistola.y = Constantes.CENTRO_VENTANA_Y - 9;
            if (jugador.getDireccion() == 3) {
                alcancePistola.x = Constantes.CENTRO_VENTANA_X - alcancePistola.width - 18;
            } else {
                alcancePistola.x = Constantes.CENTRO_VENTANA_X - 18;
            }
        }
        alcance.add(alcancePistola);
        return alcance;
    }

}
