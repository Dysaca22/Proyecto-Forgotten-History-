package principal.sonido;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import principal.Constantes;
import principal.herramientas.CargadorRecursos;

/**
 * Se encarga de gestionar todos los sonidos dentro de la aplicación, ya sea
 * música ambiental o efectos del juego, como los ataques y sonidos de los
 * botones al ser presionados <br>
 *
 * volumenMusica: Esta variable almacena la información sobre el nivel actual
 * del volumen de la música del juego <br>
 * VolumenJuego: Esta variable almacena la información el nivel
 * actual del volumen de todos los efectos del juego, aumentarla o disminuirla
 * aplicara los respectivos cambios a todos los efectos(botones al presionarse,
 * ataques de enemigos, etc...)
 */
public final class GestorSonido {

    private final Clip sonido;
    public static float volumenMusica = Constantes.VOLUMENMUSICA;
    public static float volumenJuego = Constantes.VOLUMENJUEGO;
    public static boolean musica = true;

    public GestorSonido(final String ruta) {
        sonido = CargadorRecursos.cargarSonido(ruta);
        setVolumen(volumenMusica);
    }

    /**
     * Utilizado para reproducir sonidos simples, como el de un ataque,
     * presionar un boton, abrir una interfaz etc...
     */
    public void reproducir() {
        sonido.stop();
        //Liberar memoria
        sonido.flush();
        //Ubicar el sonido al principio
        sonido.setMicrosecondPosition(0);
        sonido.start();
    }

    /**
     * utilizado para reproducir sonidos en un bucle indefinido, hasta que se
     * decida detenerlo. <br>
     *
     * Su principal función es la de reproducir la música ambiental del juego
     */
    public void reproducirBucle() {
        sonido.stop();
        //Liberar memoria
        sonido.flush();
        //Ubicar el sonido al principio
        sonido.setMicrosecondPosition(0);
        //Repetir infinitamente
        sonido.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * procesa la variable de volumen y la convierte en decibelios, dB, que son
     * los valores con los que trabaja la API de Java.
     *
     * @param vol (volumen)
     */
    public void setVolumen(double vol) {
        FloatControl gain = (FloatControl) sonido.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(vol) / Math.log(10) * 20);
        if (dB < (-80)) {
            dB = (float) (-80);
            if (dB > 6) {
                dB = (float) (6);
            }
        }

        gain.setValue(dB);
    }

    public void detenerSonido() {
        sonido.stop();
        sonido.flush();
    }

    public Long getDuracion() {
        return sonido.getMicrosecondLength();
    }

    public float getVolumenMusica() {
        return volumenMusica;
    }

    public float getVolumenJuego() {
        return volumenJuego;
    }

    public void aumentarVolumenMusica(float volumen) {
        if (volumen < 0) {
            GestorSonido.volumenMusica = 0;
        } else {
            GestorSonido.volumenMusica = volumen;
        }
        setVolumen(GestorSonido.volumenMusica);
    }

    public void aumentarVolumenJuego(float volumen) {
        if (volumen < 0) {
            GestorSonido.volumenJuego = 0;
        } else {
            GestorSonido.volumenJuego = volumen;
        }
        setVolumen(GestorSonido.volumenJuego);
    }
}
