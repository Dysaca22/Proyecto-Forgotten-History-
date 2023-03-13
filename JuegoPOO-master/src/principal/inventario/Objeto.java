package principal.inventario;

import java.awt.Rectangle;
import principal.ElementosPrincipales;
import principal.sprites.Sprite;

/**
 * Calse general de objetos
 * @author Dylan
 */
public abstract class Objeto {

    protected final int id;
    protected final String nombre;
    protected final String descripcion;
    protected int cantidad;
    protected int cantidadMaxima;

    protected Rectangle posicionEnMenu;
    protected Rectangle posicionFlotante;

    public Objeto(final int id, final String nombre, final String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;

        cantidad = 0;
        cantidadMaxima = 99;

        posicionEnMenu = new Rectangle(0, 0, 0, 0);
        posicionFlotante = new Rectangle(0, 0, 0, 0);
    }

    public Objeto(final int id, final String nombre, final String descripcion, final int cantidad) {
        this(id, nombre, descripcion);
        if (cantidad <= cantidadMaxima) {
            this.cantidad = cantidad;
        }
    }

    public abstract Sprite getSprite();

    /**
     * Aumentar la cantidad del objeto
     *
     * @param incremento (aumento)
     * @return (booleano) si se incremento o no correctamente
     */
    public boolean aumentarCantidad(final int incremento) {
        boolean incrementado = false;

        if (cantidad + incremento <= cantidadMaxima) {
            cantidad += incremento;
            incrementado = true;
        }
        return incrementado;
    }

    /**
     * Reduce la cantidad del objeto
     *
     * @param reduccion (disminucion)
     * @return (booleano) si se redujo o no correctamente
     */
    public boolean reducirCantidad(final int reduccion) {
        boolean reducido = false;

        if (this.getNombre().equals("Botiquin")) {
            if (cantidad - reduccion >= 0 && ElementosPrincipales.jugador.getVida() < 1000) {
                cantidad -= reduccion;
                ElementosPrincipales.jugador.aumentarVida(100);
                reducido = true;
            }
        } else if (cantidad - reduccion >= 0) {
            cantidad -= reduccion;
            reducido = true;
        }

        return reducido;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Rectangle getPosicionEnMEnu() {
        return posicionEnMenu;
    }

    public Rectangle getPosicionFlotante() {
        return posicionFlotante;
    }

    public void setPosicionEnMenu(Rectangle posicionEnMenu) {
        this.posicionEnMenu = posicionEnMenu;
    }

    public void setPosicionFlotante(Rectangle posicionFlotante) {
        this.posicionFlotante = posicionFlotante;
    }

}
