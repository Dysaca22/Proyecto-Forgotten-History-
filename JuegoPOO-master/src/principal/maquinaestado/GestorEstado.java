//Que estado estamos y que se dibuje a si mismo (que mapa estamos)
package principal.maquinaestado;

import java.awt.Graphics;
import principal.ElementosPrincipales;
import principal.control.GestorControles;
import principal.graficos.SuperficieDibujo;
import principal.inventario.Objeto;
import principal.inventario.elementosMujeres.Elemento;
import principal.maquinaestado.estado.GameOver;
import principal.maquinaestado.estado.Instrucciones;
import principal.maquinaestado.estado.Pausa;
import principal.maquinaestado.estado.juego.GestorJuego;
import principal.maquinaestado.estado.logros.Logro;
import principal.maquinaestado.estado.logros.Mujer;
import principal.maquinaestado.estado.menuinicial.MenuInicio;
import principal.maquinaestado.estado.menuinicial.itemsMenu.Ajustes;
import principal.maquinaestado.estado.menujuego.GestorMenu;

/**
 * Gestiona los estados de la aplicación, para alternar entre las diferentes
 * vistas, tales como el menú de pausa, de logoros alcanzados, menú de ajustes,
 * o juego corriendo.<br>
 *
 * Cuenta con un arreglo de estados predefinidos en el constructor los cuales
 * son entre los cuales se podra alternar el videoJuego. 
 *
 * En caso de querer agregar una nueva vista, sección o menú se debe agregar
 * esta, a la lista de estados
 */
public class GestorEstado {

    private int ultimoTamanho = 0;

    private EstadoJuego[] estados;
    private EstadoJuego estadoActual;

    private Logro logro;

    public GestorEstado(final SuperficieDibujo sd) {

        iniciarEstados(sd);
        iniciarEstadoActual();
    }

    private void iniciarEstados(final SuperficieDibujo sd) {

        //Añadir e iniciar los demas estados a medida que los creemos
        estados = new EstadoJuego[9];
        estados[0] = new MenuInicio(sd);
        estados[1] = new GestorJuego();
        estados[2] = new GestorMenu(sd);
        estados[3] = new GameOver(sd);
        estados[4] = new Ajustes(sd);
        estados[5] = new Pausa(sd);
        estados[6] = new Logro(sd);
        estados[7] = new Mujer(sd);
        estados[8] = new Instrucciones();

        logro = (Logro) estados[6];
    }

    private void iniciarEstadoActual() {
        estadoActual = estados[0];
    }

    /**
     * Actualiza la información de la vista actual, y decide cual debe
     * activarse, según las acciones del jugador.
     */
    public void actualizar() {
        estadoActual.actualizar();
        if (estadoActual instanceof GestorJuego || estadoActual instanceof Logro) {
            if (!ElementosPrincipales.inventario.getObjetosElementos().isEmpty()) {
                if (GestorControles.teclado.num1 && ElementosPrincipales.inventario.getObjetosElementos().size() >= 1) {
                    Elemento e = (Elemento) ElementosPrincipales.inventario.getObjetosElementos().get(0);
                    e.asignarMujer((Mujer) estados[7]);
                    GestorControles.teclado.num1 = true;
                    ((Mujer) estados[7]).setNumSalida(1);
                    estadoActual = estados[7];
                }
                if (GestorControles.teclado.num2 && ElementosPrincipales.inventario.getObjetosElementos().size() >= 2) {
                    Elemento e = (Elemento) ElementosPrincipales.inventario.getObjetosElementos().get(1);
                    e.asignarMujer((Mujer) estados[7]);
                    GestorControles.teclado.num2 = true;
                    ((Mujer) estados[7]).setNumSalida(2);
                    estadoActual = estados[7];
                }
                if (GestorControles.teclado.num3 && ElementosPrincipales.inventario.getObjetosElementos().size() >= 3) {
                    Elemento e = (Elemento) ElementosPrincipales.inventario.getObjetosElementos().get(2);
                    e.asignarMujer((Mujer) estados[7]);
                    GestorControles.teclado.num3 = true;
                    ((Mujer) estados[7]).setNumSalida(3);
                    estadoActual = estados[7];
                }
                if (GestorControles.teclado.num4 && ElementosPrincipales.inventario.getObjetosElementos().size() >= 4) {
                    Elemento e = (Elemento) ElementosPrincipales.inventario.getObjetosElementos().get(3);
                    e.asignarMujer((Mujer) estados[7]);
                    GestorControles.teclado.num4 = true;
                    ((Mujer) estados[7]).setNumSalida(4);
                    estadoActual = estados[7];
                }
                if (GestorControles.teclado.num5 && ElementosPrincipales.inventario.getObjetosElementos().size() >= 5) {
                    Elemento e = (Elemento) ElementosPrincipales.inventario.getObjetosElementos().get(4);
                    e.asignarMujer((Mujer) estados[7]);
                    GestorControles.teclado.num5 = true;
                    ((Mujer) estados[7]).setNumSalida(5);
                    estadoActual = estados[7];
                }
            }
        }
        if (!(estadoActual instanceof Mujer)) {
            GestorControles.teclado.num1 = false;
            GestorControles.teclado.num2 = false;
            GestorControles.teclado.num3 = false;
            GestorControles.teclado.num4 = false;
            GestorControles.teclado.num5 = false;
        }

        if (!ElementosPrincipales.inventario.getObjetosElementos().isEmpty()) {
            if (ultimoTamanho != ElementosPrincipales.inventario.getObjetosElementos().size()) {
                logro.setMujer(ElementosPrincipales.inventario.getObjetosElementos().size());
                Objeto objeto = ElementosPrincipales.inventario.getObjetoElemento(ElementosPrincipales.inventario.getObjetosElementos().size() - 1);
                switch (ElementosPrincipales.inventario.getObjetosElementos().size()) {
                    case 1:
                        logro.setNombre1(objeto.getNombre());
                        break;
                    case 2:
                        logro.setNombre2(objeto.getNombre());
                        break;
                    case 3:
                        logro.setNombre3(objeto.getNombre());
                        break;
                    case 4:
                        logro.setNombre4(objeto.getNombre());
                        break;
                    case 5:
                        logro.setNombre5(objeto.getNombre());
                        break;
                }
                ultimoTamanho = ElementosPrincipales.inventario.getObjetosElementos().size();
            }
        }
    }

    public void dibujar(final Graphics g) {

        estadoActual.dibujar(g);
    }

    /**
     * Cambia la vista actual, y hace que el nuevoEstado se dibuje en pantalla.
     *
     * @param nuevoEstado Estado el cual aparecera en pantalla
     */
    public void cambiarEstadoActual(final int nuevoEstado) {
        if (estadoActual instanceof GameOver || estadoActual instanceof Ajustes || estadoActual instanceof Pausa) {
            if (estados[nuevoEstado] instanceof MenuInicio) {
                MenuInicio mi = (MenuInicio) estados[nuevoEstado];
                mi.setTiempoEspera(10);
            }
        }
        if (estadoActual instanceof Logro) {
            if (estados[nuevoEstado] instanceof Pausa) {
                Pausa p = (Pausa) estados[nuevoEstado];
                p.setTiempoEspera();
            }
        }
        if (estadoActual instanceof MenuInicio) {
            if (estados[nuevoEstado] instanceof Ajustes) {
                Ajustes a = (Ajustes) estados[nuevoEstado];
                a.setTiempoEspera();
            }
        }
        estadoActual = estados[nuevoEstado];
    }

    public EstadoJuego getEstadoActual() {
        return estadoActual;
    }

    public EstadoJuego getEstado(final int i) {
        return estados[i];
    }

}
