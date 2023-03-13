package principal.maquinaestado.estado;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.GestorPrincipal;
import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.GeneradorComentario;
import principal.maquinaestado.EstadoJuego;
import principal.sonido.GestorSonido;

/**
 * Menu de pausa
 *
 * @author Dylan
 */
public class Pausa implements EstadoJuego {

    private final SuperficieDibujo sd;

    private final BufferedImage logo = CargadorRecursos.cargarImagenCompatibleTranslucida("/imagenes/iconos/logo.png");

    private final BufferedImage imagenFondo = Constantes.IMAGENFONDOPAUSA;
    private final BufferedImage logros = Constantes.LOGRO;
    private final BufferedImage logrosConMouse = Constantes.LOGROCONMOUSE;
    private final BufferedImage musica = Constantes.MUSICA;
    private final BufferedImage musicaConMouse = Constantes.MUSICACONMOUSE;
    private final BufferedImage salir = Constantes.SALIRMENU;
    private final BufferedImage salirConMouse = Constantes.SALIRMENUCONMOUSE;
    private final BufferedImage salirNo = Constantes.QUIERESALIRNO;
    private final BufferedImage salirNoConMouse = Constantes.QUIERESALIRNOCONMOUSE;
    private final BufferedImage salirSi = Constantes.QUIERESALIRSI;
    private final BufferedImage salirSiConMouse = Constantes.QUIERESALIRSICONMOUSE;
    private final BufferedImage quiereSalir = Constantes.QUIERESALIR;
    private final BufferedImage volver = Constantes.VOLVERGRANDE;
    private final BufferedImage volverConMouse = Constantes.VOLVERCONMOUSEGRANDE;

    private BufferedImage logroActual;
    private BufferedImage musicaActual;
    private BufferedImage salirActual;
    private BufferedImage volverActual;

    private final Rectangle logroR;
    private final Rectangle musicaR;
    private final Rectangle salirR;
    private BufferedImage siActual;
    private BufferedImage noActual;

    private Rectangle volverR;

    private boolean seguroSalir = false;
    private Rectangle si = null;
    private Rectangle no = null;

    private Rectangle r;

    private final GestorSonido boton = Constantes.BOTON;

    private int tiempoEspera;
    private boolean mostrarMensaje;

    private final Rectangle volverNormalR = new Rectangle(2, Constantes.ALTO_JUEGO - volver.getHeight() - 2, volver.getWidth(), volver.getHeight());

    public Pausa(final SuperficieDibujo sd) {
        this.sd = sd;

        logroR = new Rectangle(Constantes.CENTRO_VENTANA_X / 2 - logros.getWidth() / 2, 100, logros.getWidth(), logros.getHeight());
        musicaR = new Rectangle(logroR.x, logroR.y + 60, musica.getWidth(), musica.getHeight());
        salirR = new Rectangle(musicaR.x, musicaR.y + 60, salir.getWidth(), salir.getHeight());
        volverR = new Rectangle(2, Constantes.ALTO_JUEGO - volver.getHeight() - 2, volver.getWidth(), volver.getHeight());
        volverR = volverNormalR;

        logroActual = logros;
        musicaActual = musicaConMouse;
        salirActual = salir;
        volverActual = volver;

        tiempoEspera = 0;
        mostrarMensaje = false;
    }

    /**
     * Compueba todos los movimientos de mouse y dertermina que hacer
     */
    @Override
    public void actualizar() {
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);
        if (seguroSalir) {
            if (r.intersects(no)) {
                noActual = salirNoConMouse;
                if (sd.getRaton().isClickIzquierdo()) {
                    boton.reproducir();
                    tiempoEspera = 12;
                    seguroSalir = false;
                }
            } else {
                noActual = salirNo;
            }
            if (r.intersects(si)) {
                siActual = salirSiConMouse;
                if (sd.getRaton().isClickIzquierdo()) {
                    boton.reproducir();
                    GestorPrincipal.ge.cambiarEstadoActual(0);
                    seguroSalir = false;
                    GestorControles.teclado.menuPausa = false;
                }
            } else {
                siActual = salirSi;
            }
            return;
        }

        if (tiempoEspera > 0) {
            tiempoEspera--;
            return;
        }
        if (r.intersects(logroR)) {
            logroActual = logrosConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(6);
            }
        } else {
            logroActual = logros;
        }

        if (r.intersects(musicaR)) {
            mostrarMensaje = true;
            if (sd.getRaton().isClickIzquierdo()) {
                if (musicaActual == musicaConMouse) {
                    musicaActual = musica;
                } else {
                    musicaActual = musicaConMouse;
                }
                toggleMusica();
            }
        } else {
            mostrarMensaje = false;
        }

        if (r.intersects(salirR)) {
            salirActual = salirConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                seguroSalir = true;
                asignarSeguroSalir();
            }
        } else {
            salirActual = salir;
        }

        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(1);
                GestorControles.teclado.menuPausa = false;
            }
        } else {
            volverActual = volver;
        }
        if (!GestorControles.teclado.menuPausa) {
            GestorPrincipal.ge.cambiarEstadoActual(1);
            GestorControles.teclado.menuPausa = false;
        }
    }

    @Override
    public void dibujar(Graphics g) {
        DibujoOpciones.dibujarImagen(g, imagenFondo, new Point(0, 0));
        DibujoOpciones.dibujarImagen(g, logroActual, new Point(logroR.x, logroR.y));
        DibujoOpciones.dibujarImagen(g, musicaActual, new Point(musicaR.x, musicaR.y));
        DibujoOpciones.dibujarImagen(g, salirActual, new Point(salirR.x, salirR.y));

        DibujoOpciones.dibujarImagen(g, logo, 5, 5);

        if (seguroSalir) {
            DibujoOpciones.dibujarImagen(g, quiereSalir, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2);
            DibujoOpciones.dibujarImagen(g, siActual, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 40,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50);
            DibujoOpciones.dibujarImagen(g, noActual, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 90,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50);
        }

        DibujoOpciones.dibujarImagen(g, volverActual, 2, Constantes.ALTO_JUEGO - volver.getHeight() - 2);

        if (mostrarMensaje) {
            Font font = new Font("Agency FB", Font.BOLD, 7);
            g.setFont(font);
            if (GestorSonido.musica) {
                GeneradorComentario.dibujarComentario(g, sd, "Quitar la música");
            } else {
                GeneradorComentario.dibujarComentario(g, sd, "Poner la música");
            }
        }
    }

    /**
     * Permite la seleccion de salida
     */
    private void asignarSeguroSalir() {
        si = new Rectangle(Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 40, Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50,
                salirSi.getWidth(), salirSi.getHeight());
        no = new Rectangle(Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 90, Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50,
                salirNo.getWidth(), salirNo.getHeight());
    }

    /**
     * Dependiendo de la seleccion del usuario reproduce o detiene la cancion de
     * fondo del juego
     */
    public void toggleMusica() {
        if (GestorSonido.musica) {
            GestorPrincipal.detenerCancion();
            GestorSonido.musica = false;
            boton.reproducir();
            tiempoEspera = 10;
        } else {
            GestorPrincipal.reproducirCancion();
            GestorSonido.musica = true;
            boton.reproducir();
            tiempoEspera = 10;
        }
    }

    /**
     * Otorga un tiempo de espera de actualizaciones para no permitir errores
     */
    public void setTiempoEspera() {
        tiempoEspera = 5;
    }
}
