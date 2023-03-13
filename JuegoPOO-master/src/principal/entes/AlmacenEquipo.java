package principal.entes;

import principal.inventario.poderes.Cetro;

/**
 * Esta clase guarda los cetros del juego, por ahora solo cuanta con uno
 * @author Dylan
 */
public class AlmacenEquipo {

    private Cetro cetro;

    public AlmacenEquipo(final Cetro cetro) {
        this.cetro = cetro;
    }

    public Cetro getCetro() {
        return cetro;
    }

    public void cambiarCetro(final Cetro cetro) {
        this.cetro = cetro;
    }
}
