package principal.maquinaestado.estado.logros;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorComentario;
import principal.maquinaestado.EstadoJuego;
import principal.sonido.GestorSonido;

/**
 * Vista de los logros alcanzados por el jugador(rescate de mujeres).
 */
public class Logro implements EstadoJuego {

    private final SuperficieDibujo sd;

    private final BufferedImage logo = CargadorRecursos.cargarImagenCompatibleTranslucida("/imagenes/iconos/logo.png");

    private final BufferedImage imagenFondo = Constantes.IMAGENFONDOPAUSA;
    private final BufferedImage volver = Constantes.VOLVERGRANDE;
    private final BufferedImage volverConMouse = Constantes.VOLVERCONMOUSEGRANDE;

    private BufferedImage mujer1 = Constantes.MUJER1;
    private BufferedImage mujer2 = Constantes.MUJER2;
    private BufferedImage mujer3 = Constantes.MUJER3;
    private BufferedImage mujer4 = Constantes.MUJER4;
    private BufferedImage mujer5 = Constantes.MUJER5;

    private BufferedImage volverActual;

    private Rectangle r;

    private final Rectangle volverR;
    private final Rectangle mujer1R;
    private final Rectangle mujer2R;
    private final Rectangle mujer3R;
    private final Rectangle mujer4R;
    private final Rectangle mujer5R;

    private boolean activa1;
    private boolean activa2;
    private boolean activa3;
    private boolean activa4;
    private boolean activa5;

    private String nombre1 = "";
    private String nombre2 = "";
    private String nombre3 = "";
    private String nombre4 = "";
    private String nombre5 = "";

    private String nombreActual;

    private final GestorSonido boton = Constantes.BOTON;

    public Logro(final SuperficieDibujo sd) {
        this.sd = sd;

        volverR = new Rectangle(2, Constantes.ALTO_JUEGO - volver.getHeight() - 2, volver.getWidth(), volver.getHeight());
        mujer1R = new Rectangle(30, 100, 64, 64);
        mujer2R = new Rectangle(mujer1R.x + mujer1R.width + 20, 100, 64, 64);
        mujer3R = new Rectangle(mujer2R.x + mujer2R.width + 20, 100, 64, 64);
        mujer4R = new Rectangle(mujer1R.x, mujer1R.y + mujer1R.height + 20, 64, 64);
        mujer5R = new Rectangle(mujer4R.x + mujer4R.width + 20, mujer4R.y, 64, 64);

        activa1 = false;
        activa2 = false;
        activa3 = false;
        activa4 = false;
        activa5 = false;

        nombreActual = "";

        volverActual = volver;
    }

    @Override
    public void actualizar() {

        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(5);
            }
        } else {
            volverActual = volver;
        }

        activa1 = interseccionRecuadro(mujer1, Constantes.MUJER1ACTIVA, r, mujer1R, nombre1);
        activa2 = interseccionRecuadro(mujer2, Constantes.MUJER2ACTIVA, r, mujer2R, nombre2);
        activa3 = interseccionRecuadro(mujer3, Constantes.MUJER3ACTIVA, r, mujer3R, nombre3);
        activa4 = interseccionRecuadro(mujer4, Constantes.MUJER4ACTIVA, r, mujer4R, nombre4);
        activa5 = interseccionRecuadro(mujer5, Constantes.MUJER5ACTIVA, r, mujer5R, nombre5);
    }

    /**
     * Identificar sobre que cuadro esta situado el mouse para mostrar mensaje
     * adecuado
     *
     * @param mujer (Imagen de vista)
     * @param comparar (Imagen a compararar para mostrar mensaje correcto)
     * @param r (rectangulo del mouse)
     * @param mujerR (rectangulo del recuedro del logro)
     * @param nombre (nombre de la mujer o mujeres)
     * @return
     */
    private boolean interseccionRecuadro(final BufferedImage mujer, final BufferedImage comparar, final Rectangle r, final Rectangle mujerR, final String nombre) {
        boolean activa = false;
        if (r.intersects(mujerR)) {
            activa = true;
            if (mujer == comparar) {
                nombreActual = nombre;
            } else {
                nombreActual = "";
            }
        }
        return activa;
    }

    @Override
    public void dibujar(Graphics g) {

        DibujoOpciones.dibujarImagen(g, imagenFondo, new Point(0, 0));
        DibujoOpciones.dibujarImagen(g, logo, 5, 5);
        DibujoOpciones.dibujarImagen(g, volverActual, 2, Constantes.ALTO_JUEGO - volver.getHeight() - 2);
        DibujoOpciones.dibujarRectBorde(g, mujer1R, Color.red);
        DibujoOpciones.dibujarRectBorde(g, mujer2R, Color.red);
        DibujoOpciones.dibujarRectBorde(g, mujer3R, Color.red);
        DibujoOpciones.dibujarRectBorde(g, mujer4R, Color.red);
        DibujoOpciones.dibujarRectBorde(g, mujer5R, Color.red);

        DibujoOpciones.dibujarImagen(g, mujer1, mujer1R.x, mujer1R.y);
        DibujoOpciones.dibujarImagen(g, mujer2, mujer2R.x, mujer2R.y);
        DibujoOpciones.dibujarImagen(g, mujer3, mujer3R.x, mujer3R.y);
        DibujoOpciones.dibujarImagen(g, mujer4, mujer4R.x, mujer4R.y);
        DibujoOpciones.dibujarImagen(g, mujer5, mujer5R.x, mujer5R.y);

        if (activa1) {
            mostrarMensaje(g, 1);
        }
        if (activa2) {
            mostrarMensaje(g, 2);
        }
        if (activa3) {
            mostrarMensaje(g, 3);
        }
        if (activa4) {
            mostrarMensaje(g, 4);
        }
        if (activa5) {
            mostrarMensaje(g, 5);
        }
    }

    private void mostrarMensaje(final Graphics g, final int num) {
        Font font = new Font("Arial", Font.BOLD, 7);
        g.setFont(font);
        if (nombreActual.equals("")) {
            GeneradorComentario.dibujarComentario(g, sd, "Primero rescara a la mujer");
        } else {
            GeneradorComentario.dibujarComentario(g, sd, "Presiona el número " + num + ", para ver a " + nombreActual);
        }
    }

    /**
     * Se asigna la imagen adecuada al logro
     * @param num (orden de asignacion)
     */
    public void setMujer(final int num) {
        switch (num) {
            case 1:
                this.mujer1 = Constantes.MUJER1ACTIVA;
                break;
            case 2:
                this.mujer2 = Constantes.MUJER2ACTIVA;
                break;
            case 3:
                this.mujer3 = Constantes.MUJER3ACTIVA;
                break;
            case 4:
                this.mujer4 = Constantes.MUJER4ACTIVA;
                break;
            case 5:
                this.mujer5 = Constantes.MUJER5ACTIVA;
                break;
        }
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public void setNombre3(String nombre3) {
        this.nombre3 = nombre3;
    }

    public void setNombre4(String nombre4) {
        this.nombre4 = nombre4;
    }

    public void setNombre5(String nombre5) {
        this.nombre5 = nombre5;
    }

}
