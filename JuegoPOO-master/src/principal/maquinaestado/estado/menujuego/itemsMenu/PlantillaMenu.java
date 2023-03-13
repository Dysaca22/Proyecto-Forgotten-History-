package principal.maquinaestado.estado.menujuego.itemsMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import principal.Constantes;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoOpciones;

/**
 * Complementa la clase plantilla otorgando forma al menu y actualizando la
 * forma con respecto a las acciones del usuario
 *
 * @author Dylan
 */
public abstract class PlantillaMenu {

    protected final String nombreEtiqueta;
    protected final Rectangle etiqueta;

    protected final int margenGeneral = 12;
    protected final FormaMenu formaMenu;

    public PlantillaMenu(final String nombreEtiqueta, final Rectangle etiqueta, FormaMenu formaMenu) {

        this.formaMenu = formaMenu;
        this.nombreEtiqueta = nombreEtiqueta;
        this.etiqueta = etiqueta;
    }

    public abstract void actualizar();

    public abstract void dibujar(final Graphics g, final SuperficieDibujo sd);

    public void dibujarEtiquetaInactiva(final Graphics g) {
        DibujoOpciones.dibujarRectRelleno(g, etiqueta, Color.BLACK);
        DibujoOpciones.dibujarString(g, nombreEtiqueta, etiqueta.x + 15, etiqueta.y + margenGeneral, Color.WHITE);
    }

    public void dibujarEtiquetaActiva(final Graphics g) {
        DibujoOpciones.dibujarRectRelleno(g, etiqueta, Color.BLACK);
        final Rectangle activo = new Rectangle(etiqueta.x, etiqueta.y, 5, etiqueta.height);
        DibujoOpciones.dibujarRectRelleno(g, activo, Constantes.COLOR_VERDE_CLARO);

        DibujoOpciones.dibujarString(g, nombreEtiqueta, etiqueta.x + 15, etiqueta.y + margenGeneral, Color.WHITE);
    }

    public void dibujarEtiquetaInactivaResaltada(final Graphics g) {
        DibujoOpciones.dibujarRectRelleno(g, etiqueta, Color.LIGHT_GRAY);
        DibujoOpciones.dibujarRectRelleno(g, new Rectangle(etiqueta.x, etiqueta.y - 1, etiqueta.width, 1), Constantes.COLOR_VERDE_CLARO);
        DibujoOpciones.dibujarRectRelleno(g, new Rectangle(etiqueta.x + etiqueta.width - 1, etiqueta.y, 1, etiqueta.height), Constantes.COLOR_VERDE_CLARO);
        DibujoOpciones.dibujarString(g, nombreEtiqueta, etiqueta.x + 15, etiqueta.y + margenGeneral, Color.BLACK);
    }

    public void dibujarEtiquetaActivaResaltada(final Graphics g) {
        DibujoOpciones.dibujarRectRelleno(g, etiqueta, Color.LIGHT_GRAY);
        final Rectangle activo = new Rectangle(etiqueta.x, etiqueta.y, 5, etiqueta.height);
        DibujoOpciones.dibujarRectRelleno(g, activo, new Color(0x2ff40a));
        DibujoOpciones.dibujarRectRelleno(g, new Rectangle(etiqueta.x, etiqueta.y - 1, etiqueta.width, 1), Constantes.COLOR_VERDE_CLARO);
        DibujoOpciones.dibujarRectRelleno(g, new Rectangle(etiqueta.x + etiqueta.width - 1, etiqueta.y, 1, etiqueta.height), Constantes.COLOR_VERDE_CLARO);
        DibujoOpciones.dibujarString(g, nombreEtiqueta, etiqueta.x + 15, etiqueta.y + margenGeneral, Color.BLACK);
    }

    public String getNombreEtiqueta() {
        return nombreEtiqueta;
    }

    public Rectangle getEtiqueta() {
        return etiqueta;
    }

    public Rectangle getEtiquetaTamanhoReal() {
        return new Rectangle(etiqueta.x * (int) Constantes.FACTOR_ESCALADO_X, etiqueta.y * (int) Constantes.FACTOR_ESCALADO_Y, etiqueta.width * (int) Constantes.FACTOR_ESCALADO_X, etiqueta.height * (int) Constantes.FACTOR_ESCALADO_Y);
    }
}
