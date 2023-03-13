package principal.herramientas;

//Generar los comentarios que salen cuando el mouse esta sobre un objeto
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import principal.Constantes;
import principal.graficos.SuperficieDibujo;

/**
 * Genera y dibuja los comentarios que ocurren cuando el mouse pasa sobre un
 * elemento
 *
 * @author Dylan
 */
public class GeneradorComentario {

    public static Point generarComentario(final Point puntoInicial) {
        final int x = puntoInicial.x;
        final int y = puntoInicial.y;
        final Point centroCanvas = new Point(Constantes.CENTRO_VENTANA_X, Constantes.CENTRO_VENTANA_Y);
        final Point centroCanvasEscalado = new Point(EscaladorElementos.escalarPuntoArriba(centroCanvas).x, EscaladorElementos.escalarPuntoArriba(centroCanvas).y);
        //Distancia del cursor queremos generar el comentario
        final int margenCursor = 5;
        final Point puntoFinal = new Point();

        //Posicionar bien el comentario dependiendo de los bordes
        if (x <= centroCanvasEscalado.x) {
            if (y <= centroCanvasEscalado.y) {
                //Genera el comentario a la derecha, abajo
                puntoFinal.x = x + Constantes.LADO_CURSOR + margenCursor;
                puntoFinal.y = y + Constantes.LADO_CURSOR + margenCursor;
            } else {
                //Genera el comentario a la derecha, arriba
                puntoFinal.x = x + Constantes.LADO_CURSOR + margenCursor;
                puntoFinal.y = y - Constantes.LADO_CURSOR - margenCursor;
            }
        } else {
            if (y <= centroCanvasEscalado.y) {
                //Genera el comentario a la izquierda, abajo
                puntoFinal.x = x - Constantes.LADO_CURSOR - margenCursor;
                puntoFinal.y = y + Constantes.LADO_CURSOR + margenCursor;
            } else {
                //Genera el comentario a la izquierda, arriba
                puntoFinal.x = x - Constantes.LADO_CURSOR - margenCursor;
                puntoFinal.y = y - Constantes.LADO_CURSOR - margenCursor;
            }
        }

        return puntoFinal;
    }

    //Saber a donde debe mirar el comentario
    public static String obtenerPosicionComentario(final Point puntoInicial) {
        final int x = puntoInicial.x;
        final int y = puntoInicial.y;
        final Point centroCanvas = new Point(Constantes.CENTRO_VENTANA_X, Constantes.CENTRO_VENTANA_Y);
        final Point centroCanvasEscalado = new Point(EscaladorElementos.escalarPuntoArriba(centroCanvas).x, EscaladorElementos.escalarPuntoArriba(centroCanvas).y);
        String posicion = "";

        if (x <= centroCanvasEscalado.x) {
            if (y <= centroCanvasEscalado.y) {
                //Genera el comentario a la derecha, abajo
                posicion = "no";
            } else {
                //Genera el comentario a la derecha, arriba
                posicion = "so";
            }
        } else {
            if (y <= centroCanvasEscalado.y) {
                //Genera el comentario a la izquierda, abajo
                posicion = "ne";
            } else {
                //Genera el comentario a la izquierda, arriba
                posicion = "se";
            }
        }
        return posicion;
    }

    public static void dibujarComentario(final Graphics g, final SuperficieDibujo sd, final String infoComentario) {
        final Point posicionRaton = sd.getRaton().getPosicion();
        final Point posicionComentario = GeneradorComentario.generarComentario(posicionRaton);
        final String direccionComentario = GeneradorComentario.obtenerPosicionComentario(posicionRaton);
        final Point posicionComentarioEscalado = EscaladorElementos.escalarPuntoAbajo(posicionComentario);
        final int ancho = MedidorString.medirAnchoPixeles(g, infoComentario);
        final int alto = MedidorString.medirAltoPixeles(g, infoComentario);
        //Separacion de las letras de los bordes
        final int margenFuente = 2;

        Rectangle comentario = null;
        switch (direccionComentario) {
            case "no":
                comentario = new Rectangle(posicionComentarioEscalado.x, posicionComentarioEscalado.y, ancho + margenFuente * 2, alto);
                break;
            case "ne":
                comentario = new Rectangle(posicionComentarioEscalado.x - ancho, posicionComentarioEscalado.y, ancho + margenFuente * 2, alto);
                break;
            case "so":
                comentario = new Rectangle(posicionComentarioEscalado.x, posicionComentarioEscalado.y - alto, ancho + margenFuente * 2, alto);
                break;
            case "se":
                comentario = new Rectangle(posicionComentarioEscalado.x - ancho, posicionComentarioEscalado.y - alto, ancho + margenFuente * 2, alto);
                break;
        }

        DibujoOpciones.dibujarRectRelleno(g, comentario, Color.PINK);
        //La posicion Y se le suma y resto todo esto porque las palabras se dibujan desde la punta izquierda-inferior, mientras que otras cosas se dibujan desde la punta derecha-superior
        DibujoOpciones.dibujarString(g, infoComentario, new Point(comentario.x + margenFuente, comentario.y + comentario.height - margenFuente), Color.BLACK);
    }
}
