package principal.inventario;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoOpciones;

/**
 * Son aquellos cofres que se encuentran en el mapa y contienen uno o mas
 * elementos en su interior
 *
 * @author Dylan
 */
public class ContenedorObjetos {

    private BufferedImage sprite = CargadorRecursos.cargarImagenCompatibleTranslucida("/imagenes/CofreCerrado.png");

    private final Point posicion;
    private final Objeto[] objetos;
    private boolean recogido;

    public ContenedorObjetos(final Point posicion, final int[] objetos, final int[] cantidades) {
        this.posicion = posicion;
        this.objetos = new Objeto[objetos.length];
        for (int i = 0; i < objetos.length; i++) {
            this.objetos[i] = RegistroObjetos.getObjeto(objetos[i]);
            this.objetos[i].aumentarCantidad(cantidades[i]);
        }
        recogido = false;
    }

    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        DibujoOpciones.dibujarImagen(g, sprite, puntoX, puntoY);
    }

    public Point getPosicion() {
        return posicion;
    }

    public Objeto[] getObjetos() {
        return objetos;
    }

    public void setCofre() {
        sprite = CargadorRecursos.cargarImagenCompatibleTranslucida("/imagenes/CofreAbierto.png");
        recogido = true;
    }

    public boolean recogido() {
        return recogido;
    }

}
