package principal.control;

/**
 * Controla las teclas principales del movimiento del jugador
 * w, s, d, a
 * @author Dylan
 */
public class Tecla {

    private boolean pulsada = false;
    private long ultimaPulsacion = System.nanoTime();

    /**
     * Identifica si la tecla fue pulsada y guarda el tiempo exacto de esa ultima pulsada
     */
    public void teclaPulsada() {
        pulsada = true;
        ultimaPulsacion = System.nanoTime();
    }

    public void teclaLiberada() {
        pulsada = false;
    }

    public boolean isPulsada() {
        return pulsada;
    }

    public long getUltimaPulsacion() {
        return ultimaPulsacion;
    }
}
