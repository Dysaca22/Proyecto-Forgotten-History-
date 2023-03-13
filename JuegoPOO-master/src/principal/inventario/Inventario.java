package principal.inventario;

import java.util.ArrayList;
import principal.inventario.poderes.Cetro;
import principal.inventario.consumible.Consumible;
import principal.inventario.elementosMujeres.Elemento;

/**
 * Clase que representa a los elementos que ha recolectado el jugador durante el
 * juego
 *
 * @author Dylan
 */
public class Inventario {

    public final ArrayList<Objeto> objetos;

    public Inventario() {
        this.objetos = new ArrayList();
    }

    public void recogerObjetos(final ContenedorObjetos co) {
        for (Objeto objeto : co.getObjetos()) {
            if (objetoExiste(objeto)) {
                aumentarCantidadObjeto(objeto, objeto.getCantidad());
            } else {
                objetos.add(objeto);
            }
        }
    }

    /**
     *
     * @param objeto (objeto al cual se aumentara su cantidad)
     * @param cantidad (cantidad que se aumentara)
     * @return (booleano) si se incremento correctamente la cantidad del objeto
     */
    public boolean aumentarCantidadObjeto(final Objeto objeto, final int cantidad) {
        boolean incrementado = false;

        for (Objeto objetoActual : objetos) {
            if (objetoActual.getId() == objeto.getId()) {
                objetoActual.aumentarCantidad(cantidad);
                incrementado = true;
                break;
            }
        }
        return incrementado;
    }

    /**
     *
     * @param objeto (objeto que se comprobara existencia en inventario)
     * @return (booleano) determina si el objeto existe entre todos los objetos
     */
    public boolean objetoExiste(final Objeto objeto) {
        boolean existe = false;
        for (Objeto o : objetos) {
            if (o.getId() == objeto.getId()) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    /**
     * Objetos recolestados de tipo consumible
     *
     * @return (Array) objetos de tipo consumible
     */
    public ArrayList<Objeto> getObjetosConsumibles() {
        ArrayList<Objeto> consumibles = new ArrayList();
        for (Objeto objeto : objetos) {
            if (objeto instanceof Consumible) {
                consumibles.add(objeto);
            }
        }
        return consumibles;
    }

    /**
     * Objetos recolestados de tipo elemento (items mujer)
     *
     * @return (Array) objetos de tipo elemento (item mujer)
     */
    public ArrayList<Objeto> getObjetosElementos() {
        ArrayList<Objeto> elementos = new ArrayList();
        for (Objeto objeto : objetos) {
            if (objeto instanceof Elemento) {
                elementos.add(objeto);
            }
        }
        return elementos;
    }

    public Objeto getObjetoElemento(final int i) {
        ArrayList<Objeto> elementos = new ArrayList();
        for (Objeto objeto : objetos) {
            if (objeto instanceof Elemento) {
                elementos.add(objeto);
            }
        }
        return elementos.get(i);
    }

    /**
     * Objetos recolestados de tipo cetro
     *
     * @return (Array) objetos de tipo cetro
     */
    public ArrayList<Objeto> getObjetosCetro() {
        ArrayList<Objeto> armas = new ArrayList();
        for (Objeto objeto : objetos) {
            if (objeto instanceof Cetro) {
                armas.add(objeto);
            }
        }
        return armas;
    }

    public Objeto getObjeto(final int id) {
        for (Objeto objeto : objetos) {
            if (objeto.getId() == id) {
                return objeto;
            }
        }
        return null;
    }

}
