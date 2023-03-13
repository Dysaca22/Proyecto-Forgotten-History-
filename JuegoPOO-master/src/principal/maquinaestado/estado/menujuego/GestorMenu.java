package principal.maquinaestado.estado.menujuego;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import principal.Constantes;
import static principal.GestorPrincipal.ge;
import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.maquinaestado.EstadoJuego;
import principal.maquinaestado.estado.menujuego.itemsMenu.FormaMenu;
import principal.maquinaestado.estado.menujuego.itemsMenu.MenuEquipo;
import principal.maquinaestado.estado.menujuego.itemsMenu.MenuInventario;
import principal.maquinaestado.estado.menujuego.itemsMenu.PlantillaMenu;
import principal.sonido.GestorSonido;

/**
 * Menu de del jugador(equipo e inventario).
 */
public class GestorMenu implements EstadoJuego {

    private final SuperficieDibujo sd;

    private final FormaMenu inventario;

    private final PlantillaMenu[] etiquetas;
    private PlantillaMenu etiquetaActual;

    private final BufferedImage salirSinMouse = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.BOTONSALIR);
    private final BufferedImage salirConMouse = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.BOTONSALIRCONMOUSE);
    private BufferedImage salirActual;
    private final Rectangle recSalir = new Rectangle(Constantes.ANCHO_JUEGO - salirSinMouse.getWidth() - 2, 2, salirSinMouse.getWidth(),
            salirSinMouse.getHeight());
    Rectangle posicionRaton = new Rectangle();
    private final GestorSonido boton = Constantes.BOTON;

    public GestorMenu(final SuperficieDibujo sd) {
        this.sd = sd;

        inventario = new FormaMenu();

        etiquetas = new PlantillaMenu[2];

        final Rectangle etiquetaInventario = new Rectangle(inventario.LATERAL.x + inventario.MARGEN_HORIZONTAL, inventario.LATERAL.y + inventario.MARGEN_VERTICAL, inventario.ANCHO, inventario.ALTO);
        etiquetas[0] = new MenuInventario("Inventario", etiquetaInventario, inventario);

        final Rectangle etiquetaEquipo = new Rectangle(inventario.LATERAL.x + inventario.MARGEN_HORIZONTAL, etiquetaInventario.y + etiquetaInventario.height + inventario.MARGEN_VERTICAL, inventario.ANCHO, inventario.ALTO);
        etiquetas[1] = new MenuEquipo("Equipo", etiquetaEquipo, inventario);

        etiquetaActual = etiquetas[0];
        salirActual = salirSinMouse;
    }

    @Override
    public void actualizar() {
        posicionRaton = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);
        if (posicionRaton.intersects(recSalir)) {
            salirActual = salirConMouse;
        } else {
            salirActual = salirSinMouse;
        }

        for (int i = 0; i < etiquetas.length; i++) {
            //Saber si el mouse hizo click
            if (sd.getRaton().isClickIzquierdo()) {
                //Detectar si el mouse hizo el click sobre la etiqueta
                if (sd.getRaton().getPosicionRectangulo().intersects(etiquetas[i].getEtiquetaTamanhoReal())) {
                    if (etiquetaActual instanceof MenuEquipo) {
                        MenuEquipo temp = (MenuEquipo) etiquetaActual;
                        if (temp.getObjetoSeleccionado() != null) {
                            temp.setObjetoSeleccionado();
                        }
                    }
                    etiquetaActual = etiquetas[i];
                }
            }
        }

        etiquetaActual.actualizar();

        if (!GestorControles.teclado.inventario || (sd.getRaton().isClickIzquierdo() && posicionRaton.intersects(recSalir))) {
            if (etiquetaActual instanceof MenuEquipo) {
                MenuEquipo me = (MenuEquipo) etiquetaActual;
                me.setObjetoSeleccionado();
            }
            GestorControles.teclado.inventario = false;
            ge.cambiarEstadoActual(1);
            if (posicionRaton.intersects(recSalir)) {
                boton.reproducir();
            }
        }
    }

    @Override
    public void dibujar(final Graphics g) {

        inventario.dibujar(g);
        for (int i = 0; i < etiquetas.length; i++) {

            if (sd.getRaton().getPosicionRectangulo().intersects(etiquetas[i].getEtiquetaTamanhoReal())) {
                if (etiquetaActual == etiquetas[i]) {
                    etiquetas[i].dibujarEtiquetaActivaResaltada(g);
                } else {
                    etiquetas[i].dibujarEtiquetaInactivaResaltada(g);
                }
            } else {
                if (etiquetaActual == etiquetas[i]) {
                    etiquetas[i].dibujarEtiquetaActiva(g);
                } else {
                    etiquetas[i].dibujarEtiquetaInactiva(g);
                }
            }
        }
        etiquetaActual.dibujar(g, sd);
        DibujoOpciones.dibujarImagen(g, salirActual, Constantes.ANCHO_JUEGO - salirSinMouse.getWidth() - 2, 2);
    }

}
