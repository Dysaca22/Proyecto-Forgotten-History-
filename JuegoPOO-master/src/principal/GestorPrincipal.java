package principal;

import principal.graficos.SuperficieDibujo;
import principal.graficos.Ventana;
import principal.maquinaestado.GestorEstado;
import principal.sonido.GestorSonido;

/**
 * clase principal con el método main, la cual se encarga de llamar a los
 * diferentes gestores del juego(sonido, controles, etc...) y se asegura de que
 * estos se ejecuten el número de veces objetivo, establecido en la variable
 * APS_OBJETO
 */
public class GestorPrincipal {

    //Indicara si el bucle principal esta funcionando
    private static boolean enFuncionamiento = false;
    //Titulo de nuestro juego
    private final String titulo;
    //Dimensiones de la ventana
    private final int ancho;
    private final int alto;
    public static SuperficieDibujo sd;

    /**
     * JFrame sobre el cual va a habitar el programa
     */
    private Ventana ventana;
    public static GestorEstado ge;

    private static int aps = 0;
    private static int fps = 0;

    private static GestorSonido sonidoIntro = Constantes.CANCION1;

    private GestorPrincipal(final String titulo, final int ancho, final int alto) {

        this.titulo = titulo;
        this.ancho = ancho;
        this.alto = alto;
    }

    public static void main(String[] args) {
        sonidoIntro.reproducirBucle();
        //Creamos un gestor principal
        GestorPrincipal gp = new GestorPrincipal("Forgotten History", Constantes.ANCHO_PANTALLA_COMPLETA, Constantes.ALTO_PANTALLA_COMPLETA);

        gp.iniciarJuego();
        gp.iniciarBuclePrincipal();
    }

    /**
     * Llama al método inicializar y realiza el setup inicial del juego.
     *
     * @see inicializar()
     */
    private void iniciarJuego() {

        enFuncionamiento = true;
        inicializar();
    }

    private void inicializar() {

        sd = new SuperficieDibujo(ancho, alto);
        ventana = new Ventana(titulo, sd);
        ge = new GestorEstado(sd);
    }

    /**
     * pone el juego en marcha, comezando un bucle infinito principal, el cual
     * llamara al método actualizar y dibujar cada NS_POR_SEGUNDO/APS_OBJETO
     *
     * @see actualizar()
     * @see dibujar()
     */
    private void iniciarBuclePrincipal() {

        int actualizacionesAcumuladas = 0, framesAcumulados = 0;

        //Equivalencia nanosegundos en un segundo
        final int NS_POR_SEGUNDO = 1000000000;
        //Cuantas actualizaciones queremos hacer por segundo
        final byte APS_OBJETO = 60;
        //Cuantos nanosegundos transcurren por actualizacion
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETO;

        //Atribuir una cantidad de nanosegundos en tiempo exacto, de lo que ha pasado en el tiempo de inicio de juego
        long referenciaActualizacion = System.nanoTime();
        long referenciaContador = System.nanoTime();

        double tiempoTranscurrido;
        //Cantidad de tiempo que a ocurrido hasta una actualizacion
        double delta = 0;

        while (enFuncionamiento) {

            final long inicioBucle = System.nanoTime();

            //Cuanto tiempo a transcurrido
            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;

            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

            while (delta >= 1) {

                //Actualizar se ejecutara 60 veces por segundo
                actualizar();
                actualizacionesAcumuladas++;
                delta--;
            }

            dibujar();
            framesAcumulados++;

            if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {

                aps = actualizacionesAcumuladas;
                fps = framesAcumulados;
                //Escribir en pantalla los APS y FPS
                actualizacionesAcumuladas = 0;
                framesAcumulados = 0;
                referenciaContador = System.nanoTime();
            }
        }
    }

    /**
     * Actualiza la información del juego, o partida actual
     */
    private void actualizar() {

        ge.actualizar();
        sd.actualizar();
    }

    /**
     * Hace que la superficie de dibujo, muestre los elementos graficos en la
     * pantalla.
     */
    private void dibujar() {

        sd.dibujar(ge);
    }

    public static int getAps() {
        return aps;
    }

    public static int getFps() {
        return fps;
    }

    /**
     * Cambia la canción o soundtrack que se está escuchando durante la partida
     *
     * @param cancion Se le debe pasar una cancion o audio, que sea cargada y
     * extraida de su archivo mediante el GestorSonido
     * @see GestorSonido
     */
    public static void setCancion(GestorSonido cancion) {
        sonidoIntro.detenerSonido();
        sonidoIntro = cancion;
        sonidoIntro.reproducirBucle();
    }

    /**
     * Detiene la reproducción de la canción actual
     */
    public static void detenerCancion() {
        sonidoIntro.detenerSonido();
    }

    /**
     * Reproduce la canción que está establecida actualmente
     */
    public static void reproducirCancion() {
        sonidoIntro.reproducirBucle();
    }

    /**
     * Aumenta o decrementa el volumen actual del juego, tanto de los efectos,
     * como de la musica
     *
     * @param vol (volumen)
     */
    public static void setVolumen(float vol) {
        sonidoIntro.aumentarVolumenMusica(sonidoIntro.getVolumenMusica() + vol);
        Constantes.BOTON.aumentarVolumenJuego(sonidoIntro.getVolumenJuego() + vol);
    }

    /**
     * Aumenta o decrementa el volumen actual de la música.
     *
     * @param vol (volumen)
     */
    public static void setVolumenMusica(float vol) {
        sonidoIntro.aumentarVolumenMusica(sonidoIntro.getVolumenMusica() + vol);
    }

    /**
     * Aumenta o decrementa el volumen actual de los efectos del juego(ataque de
     * enemigos, sonido de los botones, ataques, etc...).
     *
     * @param vol (volumen)
     */
    public static void setVolumenJuego(float vol) {
        Constantes.BOTON.aumentarVolumenJuego(sonidoIntro.getVolumenJuego() + vol);
    }
}
