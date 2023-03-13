package principal.maquinaestado.estado.menuinicial.itemsMenu;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.entes.RegistroEnemigos;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.inventario.RegistroObjetos;
import principal.maquinaestado.EstadoJuego;
import principal.sonido.GestorSonido;

/**
 * Menu de ajustes, control general sobre el juego
 *
 * @author Dylan
 */
public class Ajustes implements EstadoJuego {

    private final SuperficieDibujo sd;

    private final BufferedImage mujer = Constantes.MUJER;
    private final BufferedImage logo = CargadorRecursos.cargarImagenCompatibleTranslucida("/imagenes/iconos/logo.png");

    private final BufferedImage imagenFondo = Constantes.FONDO;
    private final BufferedImage dificultad = Constantes.DIFICULTAD;
    private final BufferedImage dificultadConMouse = Constantes.DIFICULTADCONMOUSE;
    private final BufferedImage musica = Constantes.MUSICA;
    private final BufferedImage musicaConMouse = Constantes.MUSICACONMOUSE;
    private final BufferedImage creditos = Constantes.CREDITOS;
    private final BufferedImage creditosConMouse = Constantes.CREDITOSCONMOUSE;
    private final BufferedImage sonido = Constantes.SONIDO;
    private final BufferedImage sonidoConMouse = Constantes.SONIDOCONMOUSE;
    private final BufferedImage idioma = Constantes.IDIOMA;
    private final BufferedImage volver = Constantes.VOLVER;
    private final BufferedImage volverConMouse = Constantes.VOLVERCONMOUSE;
    private final BufferedImage volverGrande = Constantes.VOLVERGRANDE;
    private final BufferedImage volverConMouseGrande = Constantes.VOLVERCONMOUSEGRANDE;

    private BufferedImage dificultadActual;
    private BufferedImage musicaActual;
    private BufferedImage sonidoActual;
    private BufferedImage creditosActual;
    private BufferedImage volverActual;
    private BufferedImage volverGrandeActual;

    private final Rectangle dificultadR;
    private final Rectangle musicaR;
    private final Rectangle sonidoR;
    private final Rectangle creditosR;
    private Rectangle volverR;

    private Rectangle r;

    private final GestorSonido boton = Constantes.BOTON;

    private BufferedImage imagenDificultad;
    private BufferedImage imagenDificultadFacil = Constantes.DIFICULTADFACILCONMOUSE;
    private BufferedImage imagenDificultadIntermedio = Constantes.DIFICULTADINTERMEDIO;
    private BufferedImage imagenDificultadProfesional = Constantes.DIFICULTADPROFESIONAL;
    private BufferedImage imagenDificultadExtremo = Constantes.DIFICULTADEXTREMO;
    private Rectangle dificultadFacil;
    private Rectangle dificultadIntermedia;
    private Rectangle dificultadProfesional;
    private Rectangle dificultadExperto;
    private boolean newDificultad;

    private BufferedImage menuSonido;
    private BufferedImage flechaAbajo;
    private BufferedImage flechaAbajoConMouse;
    private BufferedImage flechaArriba;
    private BufferedImage flechaArribaConMouse;
    private BufferedImage flechaAbajoGeneralActual;
    private BufferedImage flechaArribaGeneralActual;
    private BufferedImage flechaAbajoMusicaActual;
    private BufferedImage flechaArribaMusicaActual;
    private BufferedImage flechaAbajoJuegoActual;
    private BufferedImage flechaArribaJuegoActual;
    private Rectangle flechaArribaRGeneral;
    private Rectangle flechaAbajoRGeneral;
    private Rectangle flechaArribaRMusica;
    private Rectangle flechaAbajoRMusica;
    private Rectangle flechaArribaRJUEGO;
    private Rectangle flechaAbajoRJUEGO;
    private boolean cambioSonido;

    private boolean cambioCancion;
    private BufferedImage imagenCancion;
    private BufferedImage cancion1 = Constantes.INFOCANCION1CONMOUSE;
    private BufferedImage cancion2 = Constantes.INFOCANCION2;
    private BufferedImage cancion3 = Constantes.INFOCANCION3;
    private BufferedImage cancion4 = Constantes.INFOCANCION4;

    private BufferedImage imagenCreditos;
    private boolean enCreditos;

    private int tiempoEspera;
    private final Rectangle volverNormalR = new Rectangle(2, Constantes.ALTO_JUEGO - volverGrande.getHeight() - 2, volverGrande.getWidth(), volverGrande.getHeight());

    public Ajustes(final SuperficieDibujo sd) {
        this.sd = sd;

        dificultadR = new Rectangle(Constantes.CENTRO_VENTANA_X - dificultad.getWidth() / 2, 40, dificultad.getWidth(), dificultad.getHeight());
        musicaR = new Rectangle(dificultadR.x, dificultadR.y + 60, musica.getWidth(), musica.getHeight());
        sonidoR = new Rectangle(musicaR.x, musicaR.y + 120, sonido.getWidth(), sonido.getHeight());
        creditosR = new Rectangle(sonidoR.x, sonidoR.y + 60, creditos.getWidth(), creditos.getHeight());
        volverR = volverNormalR;

        dificultadActual = dificultad;
        musicaActual = musica;
        sonidoActual = sonido;
        creditosActual = creditos;
        volverActual = volver;

        dificultadFacil = new Rectangle();
        dificultadIntermedia = new Rectangle();
        dificultadProfesional = new Rectangle();
        dificultadExperto = new Rectangle();
        newDificultad = false;

        flechaArribaRGeneral = new Rectangle();
        flechaAbajoRGeneral = new Rectangle();
        flechaArribaRMusica = new Rectangle();
        flechaAbajoRMusica = new Rectangle();
        flechaArribaRJUEGO = new Rectangle();
        flechaAbajoRGeneral = new Rectangle();

        cambioSonido = false;
        cambioCancion = false;
        enCreditos = false;

        tiempoEspera = 0;
    }

    @Override
    public void actualizar() {
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (tiempoEspera > 0) {
            tiempoEspera--;
            return;
        }

        if (cambioCancion) {
            cambiarCancion();
            return;
        }
        if (newDificultad) {
            cambiarDificultad();
            return;
        }

        if (cambioSonido) {
            cambiarSonido();
            return;
        }

        if (enCreditos) {
            mensajeCreditos();
            return;
        }

        comprobarBotones();
    }

    /**
     * Acciones sobre la vista de creditos
     */
    private void mensajeCreditos() {
        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 10;
                enCreditos = false;
                volverR = volverNormalR;
            }
        } else {
            volverActual = volver;
        }
    }

    /**
     * Acciones sobre vista de cambio de musica, permite escoger la cancion que
     * se quiere escuchar
     */
    private void cambiarCancion() {
        if (sd.getRaton().isClickIzquierdo()) {
            if (r.intersects(dificultadFacil)) {
                GestorPrincipal.setCancion(Constantes.CANCION1);
                cancion1 = Constantes.INFOCANCION1CONMOUSE;
                cancion2 = Constantes.INFOCANCION2;
                cancion3 = Constantes.INFOCANCION3;
                cancion4 = Constantes.INFOCANCION4;
            }
            if (r.intersects(dificultadIntermedia)) {
                GestorPrincipal.setCancion(Constantes.CANCION2);
                cancion1 = Constantes.INFOCANCION1;
                cancion2 = Constantes.INFOCANCION2CONMOUSE;
                cancion3 = Constantes.INFOCANCION3;
                cancion4 = Constantes.INFOCANCION4;
            }
            if (r.intersects(dificultadProfesional)) {
                GestorPrincipal.setCancion(Constantes.CANCION3);
                cancion1 = Constantes.INFOCANCION1;
                cancion2 = Constantes.INFOCANCION2;
                cancion3 = Constantes.INFOCANCION3CONMOUSE;
                cancion4 = Constantes.INFOCANCION4;
            }
            if (r.intersects(dificultadExperto)) {
                GestorPrincipal.setCancion(Constantes.CANCION4);
                cancion1 = Constantes.INFOCANCION1;
                cancion2 = Constantes.INFOCANCION2;
                cancion3 = Constantes.INFOCANCION3;
                cancion4 = Constantes.INFOCANCION4CONMOUSE;
            }
        }
        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 12;
                cambioCancion = false;
                volverR = volverNormalR;
            }
        } else {
            volverActual = volver;
        }
    }

    /**
     * Acciones sobre la vista de seleccion de dificultad del juego, permite
     * escoger en que dificultad se quiere jugar
     */
    private void cambiarDificultad() {
        if (sd.getRaton().isClickIzquierdo()) {
            if (r.intersects(dificultadFacil)) {
                imagenDificultadFacil = Constantes.DIFICULTADFACILCONMOUSE;
                imagenDificultadIntermedio = Constantes.DIFICULTADINTERMEDIO;
                imagenDificultadProfesional = Constantes.DIFICULTADPROFESIONAL;
                imagenDificultadExtremo = Constantes.DIFICULTADEXTREMO;
                setDificultad(0, 0, 0);
            }
            if (r.intersects(dificultadIntermedia)) {
                imagenDificultadFacil = Constantes.DIFICULTADFACIL;
                imagenDificultadIntermedio = Constantes.DIFICULTADINTERMEDIOCONMOUSE;
                imagenDificultadProfesional = Constantes.DIFICULTADPROFESIONAL;
                imagenDificultadExtremo = Constantes.DIFICULTADEXTREMO;
                setDificultad(60, 0, 100);
            }
            if (r.intersects(dificultadProfesional)) {
                imagenDificultadFacil = Constantes.DIFICULTADFACIL;
                imagenDificultadIntermedio = Constantes.DIFICULTADINTERMEDIO;
                imagenDificultadProfesional = Constantes.DIFICULTADPROFESIONALCONMOUSE;
                imagenDificultadExtremo = Constantes.DIFICULTADEXTREMO;
                setDificultad(100, 4, 200);
            }
            if (r.intersects(dificultadExperto)) {
                imagenDificultadFacil = Constantes.DIFICULTADFACIL;
                imagenDificultadIntermedio = Constantes.DIFICULTADINTERMEDIO;
                imagenDificultadProfesional = Constantes.DIFICULTADPROFESIONAL;
                imagenDificultadExtremo = Constantes.DIFICULTADEXTREMOCONMOUSE;
                setDificultad(160, 5, 300);
            }
        }
        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 12;
                newDificultad = false;
                volverR = volverNormalR;
            }
        } else {
            volverActual = volver;
        }
    }

    /**
     * Acciones sobre la vista de aumento o disminución del volumen del juego
     */
    private void cambiarSonido() {
        if (r.intersects(flechaAbajoRGeneral)) {
            flechaAbajoGeneralActual = flechaAbajoConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 3;
                GestorPrincipal.setVolumen((float) -0.01);
            }
        } else {
            flechaAbajoGeneralActual = flechaAbajo;
        }

        if (r.intersects(flechaArribaRGeneral)) {
            flechaArribaGeneralActual = flechaArribaConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 3;
                GestorPrincipal.setVolumen((float) 0.01);
            }
        } else {
            flechaArribaGeneralActual = flechaArriba;
        }

        if (r.intersects(flechaAbajoRMusica)) {
            flechaAbajoMusicaActual = flechaAbajoConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 3;
                GestorPrincipal.setVolumenMusica((float) -0.01);
            }
        } else {
            flechaAbajoMusicaActual = flechaAbajo;
        }

        if (r.intersects(flechaArribaRMusica)) {
            flechaArribaMusicaActual = flechaArribaConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 3;
                GestorPrincipal.setVolumenMusica((float) 0.01);
            }
        } else {
            flechaArribaMusicaActual = flechaArriba;
        }

        if (r.intersects(flechaAbajoRJUEGO)) {
            flechaAbajoJuegoActual = flechaAbajoConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 3;
                GestorPrincipal.setVolumenJuego((float) -0.01);
            }
        } else {
            flechaAbajoJuegoActual = flechaAbajo;
        }

        if (r.intersects(flechaArribaRJUEGO)) {
            flechaArribaJuegoActual = flechaArribaConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 3;
                GestorPrincipal.setVolumenJuego((float) 0.01);
            }
        } else {
            flechaArribaJuegoActual = flechaArriba;
        }
        if (r.intersects(volverR)) {
            volverActual = volverConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 12;
                cambioSonido = false;
                volverR = volverNormalR;
            }
        } else {
            volverActual = volver;
        }
    }

    /**
     * Comprobar si los botones han tenido interacción con el mouse
     */
    private void comprobarBotones() {
        if (r.intersects(dificultadR)) {
            dificultadActual = dificultadConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                mensajeDificultad();
            }
        } else {
            dificultadActual = dificultad;
        }
        if (r.intersects(musicaR)) {
            musicaActual = musicaConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                setMusica();
            }
        } else {
            musicaActual = musica;
        }

        if (r.intersects(creditosR)) {
            creditosActual = creditosConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                setCreditos();
            }
        } else {
            creditosActual = creditos;
        }

        if (r.intersects(volverR)) {
            volverGrandeActual = volverConMouseGrande;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(0);
            }
        } else {
            volverGrandeActual = volverGrande;
        }

        if (r.intersects(sonidoR)) {
            sonidoActual = sonidoConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                tiempoEspera = 10;
                setSonido();
            }
        } else {
            sonidoActual = sonido;
        }
    }

    @Override
    public void dibujar(Graphics g) {
        DibujoOpciones.dibujarImagen(g, imagenFondo, new Point(0, 0));
        DibujoOpciones.dibujarImagen(g, dificultadActual, new Point(dificultadR.x, dificultadR.y));
        DibujoOpciones.dibujarImagen(g, musicaActual, new Point(musicaR.x, musicaR.y));
        DibujoOpciones.dibujarImagen(g, idioma, new Point(musicaR.x, musicaR.y + idioma.getHeight() + 25));
        DibujoOpciones.dibujarImagen(g, sonidoActual, new Point(musicaR.x, musicaR.y + sonido.getHeight() + 85));
        DibujoOpciones.dibujarImagen(g, creditosActual, new Point(creditosR.x, creditosR.y));

        DibujoOpciones.dibujarImagen(g, mujer, Constantes.ANCHO_JUEGO - mujer.getWidth(), 0);
        DibujoOpciones.dibujarImagen(g, logo, 5, 5);

        if (newDificultad) {
            DibujoOpciones.dibujarImagen(g, imagenDificultad, Constantes.CENTRO_VENTANA_X - imagenDificultad.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - imagenDificultad.getHeight() / 2);
            DibujoOpciones.dibujarImagen(g, imagenDificultadFacil, Constantes.CENTRO_VENTANA_X - imagenDificultad.getWidth() / 2 + 20,
                    Constantes.CENTRO_VENTANA_Y - imagenDificultad.getHeight() / 2 + 40);
            DibujoOpciones.dibujarImagen(g, imagenDificultadIntermedio, dificultadFacil.x, dificultadFacil.y + 20);
            DibujoOpciones.dibujarImagen(g, imagenDificultadProfesional, dificultadIntermedia.x, dificultadIntermedia.y + 20);
            DibujoOpciones.dibujarImagen(g, imagenDificultadExtremo, dificultadProfesional.x, dificultadProfesional.y + 20);
            DibujoOpciones.dibujarImagen(g, volverActual, Constantes.CENTRO_VENTANA_X + imagenDificultad.getWidth() / 2 - volver.getWidth() - 5,
                    Constantes.CENTRO_VENTANA_Y + imagenDificultad.getHeight() / 2 - volver.getHeight() - 5);
            return;
        }
        if (cambioCancion) {
            DibujoOpciones.dibujarImagen(g, imagenCancion, Constantes.CENTRO_VENTANA_X - imagenCancion.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - imagenCancion.getHeight() / 2);
            DibujoOpciones.dibujarImagen(g, cancion1, dificultadFacil.x, dificultadFacil.y);
            DibujoOpciones.dibujarImagen(g, cancion2, dificultadIntermedia.x, dificultadIntermedia.y);
            DibujoOpciones.dibujarImagen(g, cancion3, dificultadProfesional.x, dificultadProfesional.y);
            DibujoOpciones.dibujarImagen(g, cancion4, dificultadExperto.x, dificultadExperto.y);
            DibujoOpciones.dibujarImagen(g, volverActual, dificultadExperto.x + dificultadExperto.width / 2,
                    dificultadExperto.y + 20);
            return;
        }
        if (cambioSonido) {
            DibujoOpciones.dibujarImagen(g, menuSonido, Constantes.CENTRO_VENTANA_X - menuSonido.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - menuSonido.getHeight() / 2);

            DibujoOpciones.dibujarImagen(g, flechaAbajoGeneralActual, Constantes.CENTRO_VENTANA_X - flechaAbajo.getWidth() * 2 + 20,
                    Constantes.CENTRO_VENTANA_Y - flechaAbajo.getHeight() / 2 - 35);
            DibujoOpciones.dibujarImagen(g, flechaArribaGeneralActual, Constantes.CENTRO_VENTANA_X + flechaArriba.getWidth() - 20,
                    Constantes.CENTRO_VENTANA_Y - flechaArriba.getHeight() / 2 - 35);

            DibujoOpciones.dibujarImagen(g, flechaAbajoMusicaActual, Constantes.CENTRO_VENTANA_X - flechaAbajo.getWidth() * 2 + 20,
                    Constantes.CENTRO_VENTANA_Y - flechaAbajo.getHeight() / 2 + 15);
            DibujoOpciones.dibujarImagen(g, flechaArribaMusicaActual, Constantes.CENTRO_VENTANA_X + flechaArriba.getWidth() - 20,
                    Constantes.CENTRO_VENTANA_Y - flechaArriba.getHeight() / 2 + 15);

            DibujoOpciones.dibujarImagen(g, flechaAbajoJuegoActual, Constantes.CENTRO_VENTANA_X - flechaAbajo.getWidth() * 2 + 20,
                    Constantes.CENTRO_VENTANA_Y - flechaAbajo.getHeight() / 2 + 65);
            DibujoOpciones.dibujarImagen(g, flechaArribaJuegoActual, Constantes.CENTRO_VENTANA_X + flechaArriba.getWidth() - 20,
                    Constantes.CENTRO_VENTANA_Y - flechaArriba.getHeight() / 2 + 65);

            DibujoOpciones.dibujarImagen(g, volverActual, Constantes.CENTRO_VENTANA_X - menuSonido.getWidth() / 2 + 2,
                    Constantes.CENTRO_VENTANA_Y + menuSonido.getHeight() / 2 - volver.getHeight());

            return;
        }

        if (enCreditos) {
            DibujoOpciones.dibujarImagen(g, imagenCreditos, Constantes.CENTRO_VENTANA_X - imagenCreditos.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - imagenCreditos.getHeight() / 2);
            DibujoOpciones.dibujarImagen(g, volverActual, Constantes.CENTRO_VENTANA_X + imagenCreditos.getWidth() / 2 + 2 - volver.getWidth() - 4,
                    Constantes.CENTRO_VENTANA_Y + imagenCreditos.getHeight() / 2 - volver.getHeight());
            return;
        }

        DibujoOpciones.dibujarImagen(g, volverGrandeActual, 2, Constantes.ALTO_JUEGO - volverGrande.getHeight() - 2);
    }

    /**
     * Otorga valores a las variables para mostrar el panel de dificultad
     */
    private void mensajeDificultad() {
        newDificultad = true;
        imagenDificultad = Constantes.DIFICULTADPANEL;

        dificultadFacil = new Rectangle(Constantes.CENTRO_VENTANA_X - imagenDificultad.getWidth() / 2 + 20,
                Constantes.CENTRO_VENTANA_Y - imagenDificultad.getHeight() / 2 + 40, imagenDificultadFacil.getWidth(), imagenDificultadFacil.getHeight());
        dificultadIntermedia = new Rectangle(dificultadFacil.x, dificultadFacil.y + 20, imagenDificultadIntermedio.getWidth(), imagenDificultadIntermedio.getHeight());
        dificultadProfesional = new Rectangle(dificultadIntermedia.x, dificultadIntermedia.y + 20, imagenDificultadProfesional.getWidth(), imagenDificultadProfesional.getHeight());
        dificultadExperto = new Rectangle(dificultadProfesional.x, dificultadProfesional.y + 20, imagenDificultadExtremo.getWidth(), imagenDificultadExtremo.getHeight());
        volverR = new Rectangle(Constantes.CENTRO_VENTANA_X + imagenDificultad.getWidth() / 2 - volver.getWidth() - 5,
                Constantes.CENTRO_VENTANA_Y + imagenDificultad.getHeight() / 2 - volver.getHeight() - 5, volver.getWidth(), volver.getHeight());
    }

    /**
     * Asigna la dificultad escogida Dependiendo de la dificultad se asignas
     * valores al juego
     *
     * @param vidaEnemigos (cantidad de vida que tendran los enemigos)
     * @param danhoPoder (danho que hara los cetros)
     * @param cantidadEnemigos (los enemigos se generan de manera aleatoria y
     * con esto se generan mas rapido o mas lento)
     */
    private void setDificultad(final int vidaEnemigos, final int danhoPoder, final int cantidadEnemigos) {
        RegistroEnemigos.setDificultad(vidaEnemigos);
        RegistroObjetos.setDificultad(danhoPoder);
        ElementosPrincipales.mapa.setDificultad(cantidadEnemigos);
    }

    /**
     * Otorga valores a las variables para mostrar el panel de musica
     */
    private void setMusica() {
        cambioCancion = true;
        imagenCancion = Constantes.CANCIONPANEL;

        dificultadFacil = new Rectangle(Constantes.CENTRO_VENTANA_X - imagenCancion.getWidth() / 2 + 20,
                Constantes.CENTRO_VENTANA_Y - imagenCancion.getHeight() / 2 + 30, cancion1.getWidth(), cancion1.getHeight());
        dificultadIntermedia = new Rectangle(dificultadFacil.x, dificultadFacil.y + 20, cancion2.getWidth(), cancion2.getHeight());
        dificultadProfesional = new Rectangle(dificultadIntermedia.x, dificultadIntermedia.y + 20, cancion3.getWidth(), cancion3.getHeight());
        dificultadExperto = new Rectangle(dificultadProfesional.x, dificultadProfesional.y + 20, cancion4.getWidth(), cancion4.getHeight());
        volverR = new Rectangle(dificultadExperto.x + dificultadExperto.width / 2,
                dificultadExperto.y + 20, volver.getWidth(), volver.getHeight());
    }

    /**
     * Otorga valores a las variables para mostrar el panel de sonido
     */
    private void setSonido() {
        cambioSonido = true;
        menuSonido = Constantes.MENUSONIDO;
        flechaAbajo = Constantes.FlECHAABAJO;
        flechaAbajoConMouse = Constantes.FlECHAABAJOCONMOUSE;
        flechaArriba = Constantes.FlECHAARRIBA;
        flechaArribaConMouse = Constantes.FlECHAARRIBACONMOUSE;
        flechaAbajoGeneralActual = flechaAbajo;
        flechaArribaGeneralActual = flechaArriba;
        flechaAbajoMusicaActual = flechaAbajo;
        flechaArribaMusicaActual = flechaArriba;
        flechaAbajoJuegoActual = flechaAbajo;
        flechaArribaJuegoActual = flechaArriba;

        flechaAbajoRGeneral = new Rectangle(Constantes.CENTRO_VENTANA_X - flechaAbajo.getWidth() * 2 + 20,
                Constantes.CENTRO_VENTANA_Y - flechaAbajo.getHeight() / 2 - 35, flechaArriba.getWidth(), flechaArriba.getHeight());
        flechaArribaRGeneral = new Rectangle(Constantes.CENTRO_VENTANA_X + flechaArriba.getWidth() - 20,
                Constantes.CENTRO_VENTANA_Y - flechaArriba.getHeight() / 2 + -35, flechaAbajo.getWidth(), flechaAbajo.getHeight());

        flechaAbajoRMusica = new Rectangle(Constantes.CENTRO_VENTANA_X - flechaAbajo.getWidth() * 2 + 20,
                Constantes.CENTRO_VENTANA_Y - flechaAbajo.getHeight() / 2 + 10, flechaArriba.getWidth(), flechaArriba.getHeight());
        flechaArribaRMusica = new Rectangle(Constantes.CENTRO_VENTANA_X + flechaArriba.getWidth() - 20,
                Constantes.CENTRO_VENTANA_Y - flechaArriba.getHeight() / 2 + 10, flechaAbajo.getWidth(), flechaAbajo.getHeight());

        flechaAbajoRJUEGO = new Rectangle(Constantes.CENTRO_VENTANA_X - flechaAbajo.getWidth() * 2 + 20,
                Constantes.CENTRO_VENTANA_Y - flechaAbajo.getHeight() / 2 + 65, flechaArriba.getWidth(), flechaArriba.getHeight());
        flechaArribaRJUEGO = new Rectangle(Constantes.CENTRO_VENTANA_X + flechaArriba.getWidth() - 20,
                Constantes.CENTRO_VENTANA_Y - flechaArriba.getHeight() / 2 + 65, flechaAbajo.getWidth(), flechaAbajo.getHeight());

        volverR = new Rectangle(Constantes.CENTRO_VENTANA_X - menuSonido.getWidth() / 2 + 2, Constantes.CENTRO_VENTANA_Y + menuSonido.getHeight() / 2 - volver.getHeight(),
                volver.getWidth(), volver.getHeight());
    }

    /**
     * Otorga valores a las variables para mostrar el panel de creditos
     */
    private void setCreditos() {

        enCreditos = true;
        imagenCreditos = Constantes.IMAGENCREDITOS;
        volverR = new Rectangle(Constantes.CENTRO_VENTANA_X + imagenCreditos.getWidth() / 2 + 2 - volver.getWidth() - 4, Constantes.CENTRO_VENTANA_Y
                + imagenCreditos.getHeight() / 2 - volver.getHeight(), volver.getWidth(), volver.getHeight());
    }

    /**
     * Asigna tiempo de espera para no causar conflictos
     */
    public void setTiempoEspera() {
        tiempoEspera = 10;
    }

}
