package principal.entes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.herramientas.CalcularDistancia;
import principal.herramientas.DibujoOpciones;
import principal.sonido.GestorSonido;

/**
 * Clase general de todos los enemigos que hay en el mapa
 *
 * @author Dylan
 */
public abstract class Enemigo {

    private final GestorSonido lamento;
    private final long duracionLamento;
    private long lamentoSiguinete = 0;

    private final int id;
    private double posicionX;
    private double posicionY;
    private final String nombre;
    private final int vidaMaxima;
    private int vidaActual;
    private final double radioDistancia;

    protected boolean enMovimiento;
    protected int direccion;

    protected double velocidad = 0.5;

    protected int ataqueMin;
    protected int ataqueMax;

    public Enemigo(int id, String nombre, int vidaMaxima, final String rutaLamento, final double radioDistancia) {
        this.id = id;
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.posicionX = 0;
        this.posicionY = 0;
        this.radioDistancia = radioDistancia;

        this.lamento = new GestorSonido(rutaLamento);
        this.duracionLamento = lamento.getDuracion();

        enMovimiento = false;
        direccion = 0;

        ataqueMin = 20;
        ataqueMax = 60;
    }

    public void actualizar(ArrayList<Enemigo> enemigos) {
        if (lamentoSiguinete > 0) {
            lamentoSiguinete -= 1000000 / 60;
        }
        double distancia = CalcularDistancia.getDistanciaEntrePuntos(new Point(ElementosPrincipales.jugador.getPosicionXINT(), ElementosPrincipales.jugador.getPosicionYINT()),
                new Point((int) this.getPosicionX(), (int) this.getPosicionY()));
        if (distancia < radioDistancia) {
            moverANodoSiguiente(enemigos);
        } else {
            enMovimiento = false;
        }
    }

    /**
     * Permite a los enemigos existentes que se muevan al rededor del mapa para
     * obtaculizar el avance del jugador
     *
     * @param enemigos
     */
    private void moverANodoSiguiente(ArrayList<Enemigo> enemigos) {
        int ultimaDireccion = -1;
        //Mirar si un enemigo no atasque otro enemigo
        for (Enemigo enemigo : enemigos) {
            if (enemigo.equals(this)) {
                continue;
            }
            if (enemigo.getArea().intersects(this.getAreaFutura())
                    || (enemigo.getArea().intersects(ElementosPrincipales.jugador.getArea()) && this.getAreaFutura().intersects(ElementosPrincipales.jugador.getArea()))) {
                ultimaDireccion = direccion;
                switch (ultimaDireccion) {
                    case 0:
                        posicionY -= velocidad;
                        break;
                    case 1:
                        posicionY += velocidad;
                        break;
                    case 2:
                        posicionX -= velocidad;
                        break;
                    case 3:
                        posicionX += velocidad;
                        break;
                }
            }
        }
        int xNodoSigueinte = ElementosPrincipales.jugador.getPosicionXINT();
        int yNodoSigueinte = ElementosPrincipales.jugador.getPosicionYINT();

        if (posicionX < xNodoSigueinte && !enColisionArriba(-1)) {
            posicionX += velocidad;
            direccion = 2;
        } else if (posicionX > xNodoSigueinte && !enColisionAbajo(1)) {
            posicionX -= velocidad;
            direccion = 3;
        } else if (posicionY < yNodoSigueinte && !enColisionIzquierda(-1)) {
            posicionY += velocidad;
            direccion = 0;
        } else if (posicionY > yNodoSigueinte && !enColisionDerecha(1)) {
            posicionY -= velocidad;
            direccion = 1;
        }
        enMovimiento = true;
    }

    /**
     * @param velocidadY
     * @return (booleano) si su limite superior esta en colision con algun
     * objeto colisionable
     */
    private boolean enColisionArriba(int velocidadY) {

        for (int r = 0; r < ElementosPrincipales.mapa.getAreasColisionJugador().size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.getAreasColisionJugador().get(r);

            int origenX = area.x;
            int origenY = area.y + velocidadY * (int) (velocidad + 0.6) + 3 * (int) (velocidad + 0.6);

            Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            Rectangle LIMITE_ARRIBA = new Rectangle(getAreaDisparo().x, getAreaDisparo().y + 16, 16, 1);
            if (LIMITE_ARRIBA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param velocidadY
     * @return (booleano) si su limite inferior esta en colision con algun
     * objeto colisionable
     */
    private boolean enColisionAbajo(int velocidadY) {

        for (int r = 0; r < ElementosPrincipales.mapa.getAreasColisionJugador().size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.getAreasColisionJugador().get(r);

            int origenX = area.x;
            int origenY = area.y + velocidadY * (int) (velocidad + 0.6) - 3 * (int) (velocidad + 0.6);

            Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            Rectangle LIMITE_ABAJO = new Rectangle(getAreaDisparo().x, getAreaDisparo().y + Constantes.LADO_SPRITE, 16, 1);
            if (LIMITE_ABAJO.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param velocidadX
     * @return (booleano) si su limite izquierdo esta en colision con algun
     * objeto colisionable
     */
    private boolean enColisionIzquierda(int velocidadX) {

        for (int r = 0; r < ElementosPrincipales.mapa.getAreasColisionJugador().size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.getAreasColisionJugador().get(r);

            int origenX = area.x + velocidadX * (int) (velocidad + 0.6) + 3 * (int) (velocidad + 0.6);
            int origenY = area.y;

            Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            Rectangle LIMITE_IZQUIERDA = new Rectangle(getAreaDisparo().x, getAreaDisparo().y + 16, 1, Constantes.LADO_SPRITE / 2);
            if (LIMITE_IZQUIERDA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param velocidadX
     * @return (booleano) si su limite derecho esta en colision con algun objeto
     * colisionable
     */
    private boolean enColisionDerecha(int velocidadX) {

        for (int r = 0; r < ElementosPrincipales.mapa.getAreasColisionJugador().size(); r++) {
            final Rectangle area = ElementosPrincipales.mapa.getAreasColisionJugador().get(r);

            int origenX = area.x + velocidadX * (int) (velocidad + 0.6) - 3 * (int) (velocidad + 0.6);
            int origenY = area.y;

            Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            Rectangle LIMITE_DERECHA = new Rectangle(getAreaDisparo().x + 16, getAreaDisparo().y + 16, 1, Constantes.LADO_SPRITE / 2);
            if (LIMITE_DERECHA.intersects(areaFutura)) {
                return true;
            }
        }
        return false;
    }

    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        if (vidaActual > 0) {
            dibujarBarraVida(g, puntoX, puntoY);
        }
    }

    /**
     * Dibuja la barra de vida del enemigo
     * @param g (graficos)
     * @param puntoX (posicion en x del enemigo)
     * @param puntoY (posicion en y del enemigo)
     */
    private void dibujarBarraVida(final Graphics g, final int puntoX, final int puntoY) {
        DibujoOpciones.dibujarRectRelleno(g, puntoX + 4, puntoY - 5, (Constantes.LADO_SPRITE - 8) * (int) vidaActual / vidaMaxima, 2, Color.red);
    }

    /**
     * Disminuye la vida del enemigo
     * @param ataqueDado (el daño que le hizo el jugador al enemigo)
     */
    public void quitarVida(float ataqueDado) {
        //Reproducir Sonido
        if (lamentoSiguinete <= 0) {
            lamento.reproducir();
            lamentoSiguinete = duracionLamento * 3;
        }

        if (vidaActual - ataqueDado <= 0) {
            vidaActual = 0;
        } else {
            vidaActual -= ataqueDado;
        }
    }

    public void setPosicion(final double posicionX, final double posicionY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public int getId() {
        return id;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    /**
     * Devuelve un rectangulo que es usado para detectar las coliciones del
     * enemigo
     *
     * @return rectangulo
     */
    public Rectangle getArea() {
        final int puntoX = (int) posicionX - ElementosPrincipales.jugador.getPosicionXINT() + Constantes.MARGEN_X;
        final int puntoY = (int) posicionY - ElementosPrincipales.jugador.getPosicionYINT() + Constantes.MARGEN_Y;
        return new Rectangle(puntoX + 8, puntoY, Constantes.LADO_SPRITE / 2, Constantes.LADO_SPRITE);
    }

    /**
     * Devuelve un rectangulo que es usado para detectar a otros enemigos cercanos y no superponerce
     * @return rectangulo
     */
    public Rectangle getAreaFutura() {
        return new Rectangle(getArea().x - 6, getArea().y - 6, getArea().width + 12, getArea().height + 10);
    }

    /**
     * Devuelve un rectangulo que es usado para detectar si un ataque del enemigo puede hacer danho al enemigo
     * @return rectangulo
     */
    public Rectangle getAreaDisparo() {
        return new Rectangle(getArea().x, getArea().y, getArea().width, getArea().height);
    }

}
