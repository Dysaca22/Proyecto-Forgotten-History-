package principal.maquinaestado.estado.menuinicial;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;
import principal.maquinaestado.estado.menuinicial.itemsMenu.CargarPartida;
import principal.sonido.GestorSonido;

/**
 * Vista de inicio del juego
 *
 * @author Dylan
 */
public class MenuInicio implements EstadoJuego {

    private final BufferedImage imagenFondo = Constantes.IMAGENFONDO;
    private final BufferedImage inicioPartida = Constantes.INICIOPARTIDA;
    private final BufferedImage inicioPartidaConMouse = Constantes.INICIOPARTIDACONMOUSE;
    private final BufferedImage cargarPartida = Constantes.CARGARPARTIDA;
    private final BufferedImage cargarPartidaConMouse = Constantes.CARGARPARTIDACONMOUSE;
    private final BufferedImage cargarSinPartida = Constantes.CARGARSONPARTIDA;
    private final BufferedImage configuracion = Constantes.CONFIGURACION;
    private final BufferedImage configuracionConMouse = Constantes.CONFIGURACIONCONMOUSE;
    private final BufferedImage salirMenu = Constantes.SALIRMENU;
    private final BufferedImage salirMenuConMouse = Constantes.SALIRMENUCONMOUSE;
    private final BufferedImage quiereSalir = Constantes.QUIERESALIR;
    private final BufferedImage salirSi = Constantes.QUIERESALIRSI;
    private final BufferedImage salirNo = Constantes.QUIERESALIRNO;
    private final BufferedImage salirSiConMouse = Constantes.QUIERESALIRSICONMOUSE;
    private final BufferedImage salirNoConMouse = Constantes.QUIERESALIRNOCONMOUSE;

    private final Rectangle inicioPartidaR;
    private final Rectangle cargarPartidaR;
    private final Rectangle configuracionR;
    private final Rectangle salirR;

    private final SuperficieDibujo sd;

    private BufferedImage inicioPartidaActual;
    private BufferedImage cargarSinPartidaActual;
    private BufferedImage configuracionActual;
    private BufferedImage salirActual;
    private BufferedImage siActual;
    private BufferedImage noActual;

    private final CargarPartida partida;
    private final GestorSonido boton = Constantes.BOTON;

    private Rectangle si = null;
    private Rectangle no = null;
    private boolean seguroSalir = false;

    private Rectangle r;
    private int tiempoEspera;

    public MenuInicio(final SuperficieDibujo sd) {
        this.sd = sd;
        this.partida = new CargarPartida();

        inicioPartidaR = new Rectangle(Constantes.CENTRO_VENTANA_X - inicioPartida.getWidth() / 2, 180, inicioPartida.getWidth(), inicioPartida.getHeight());
        cargarPartidaR = new Rectangle(inicioPartidaR.x, inicioPartidaR.y + 40, cargarPartida.getWidth(), cargarPartida.getHeight());
        configuracionR = new Rectangle(cargarPartidaR.x, cargarPartidaR.y + 40, configuracion.getWidth(), configuracion.getHeight());
        salirR = new Rectangle(configuracionR.x, configuracionR.y + 40, salirMenu.getWidth(), salirMenu.getHeight());

        inicioPartidaActual = inicioPartida;
        cargarSinPartidaActual = cargarSinPartida;
        configuracionActual = configuracion;
        salirActual = salirMenu;

        tiempoEspera = 0;
    }

    @Override
    public void actualizar() {
        r = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);

        if (seguroSalir) {
            if (r.intersects(no)) {
                noActual = salirNoConMouse;
                if (sd.getRaton().isClickIzquierdo()) {
                    boton.reproducir();
                    tiempoEspera = 10;
                    seguroSalir = false;
                }
            } else {
                noActual = salirNo;
            }
            if (r.intersects(si)) {
                siActual = salirSiConMouse;
                if (sd.getRaton().isClickIzquierdo()) {
                    boton.reproducir();
                    System.exit(0);
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
        if (r.intersects(salirR)) {
            salirActual = salirMenuConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                seguroSalir = true;
                asignarSeguroSalir();
            }
        } else {
            salirActual = salirMenu;
        }
        if (r.intersects(inicioPartidaR)) {
            inicioPartidaActual = inicioPartidaConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                ElementosPrincipales.jugador.renacer();
                GestorPrincipal.ge.cambiarEstadoActual(1);
            }
        } else {
            inicioPartidaActual = inicioPartida;
        }
        if (partida.getPartidas().isEmpty()) {
            cargarSinPartidaActual = cargarSinPartida;
        } else if (r.intersects(cargarPartidaR)) {
            boton.reproducir();
            cargarSinPartidaActual = cargarPartidaConMouse;
        } else {
            cargarSinPartidaActual = cargarPartida;
        }
        if (r.intersects(configuracionR)) {
            configuracionActual = configuracionConMouse;
            if (sd.getRaton().isClickIzquierdo()) {
                boton.reproducir();
                GestorPrincipal.ge.cambiarEstadoActual(4);
            }
        } else {
            configuracionActual = configuracion;
        }
    }

    private void asignarSeguroSalir() {
        si = new Rectangle(Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 40, Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50,
                salirSi.getWidth(), salirSi.getHeight());
        no = new Rectangle(Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 90, Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50,
                salirNo.getWidth(), salirNo.getHeight());
    }

    @Override
    public void dibujar(Graphics g) {

        DibujoOpciones.dibujarImagen(g, imagenFondo, new Point(0, 0));
        DibujoOpciones.dibujarImagen(g, inicioPartidaActual, new Point(inicioPartidaR.x, inicioPartidaR.y));
        DibujoOpciones.dibujarImagen(g, cargarSinPartidaActual, new Point(cargarPartidaR.x, cargarPartidaR.y));
        DibujoOpciones.dibujarImagen(g, configuracionActual, new Point(configuracionR.x, configuracionR.y));
        DibujoOpciones.dibujarImagen(g, salirActual, new Point(salirR.x, salirR.y));

        if (seguroSalir) {
            DibujoOpciones.dibujarImagen(g, quiereSalir, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2);
            DibujoOpciones.dibujarImagen(g, siActual, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 40,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50);
            DibujoOpciones.dibujarImagen(g, noActual, Constantes.CENTRO_VENTANA_X - quiereSalir.getWidth() / 2 + 90,
                    Constantes.CENTRO_VENTANA_Y - quiereSalir.getHeight() / 2 + 50);
        }

    }

    /**
     * Otorga un tiempo de espera de actualizacion para no ocacionar errores
     *
     * @param tiempoEspera para actualizar
     */
    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

}
