package principal.entes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import principal.control.GestorControles;
import principal.herramientas.DibujoOpciones;
import principal.inventario.RegistroObjetos;
import principal.inventario.poderes.Cetro;
import principal.inventario.poderes.DesArmado;
import principal.sprites.HojaSprites;

/**
 * Elemento que controla el usuario
 *
 * @author Dylan
 */
public class Jugador {

    private ArrayList<Double> puntoX;
    private ArrayList<Double> puntoY;
    private ArrayList<Integer> direcciones;
    private ArrayList<Rectangle> rectanguloDisparo;
    private int tiempoSpriteDisparo;
    private boolean disparando;
    private int ultimoDisparo;

    private double posicionX;
    private double posicionY;

    private double velocidadMovimiento = 0.7;
    private boolean enMovimiento;
    private int animacion;
    private int a;
    private int d;

    private int direccion;

    private HojaSprites hs;
    private final HojaSprites hojaPoder;
    private final HojaSprites disparo;
    private boolean cambio;
    private BufferedImage imagenActual;

    private final int anchoJugador = 16;
    private final int altoJugador = 16;

    private final Rectangle LIMITE_ARRIBA = new Rectangle(Constantes.CENTRO_VENTANA_X - 25, Constantes.CENTRO_VENTANA_Y - anchoJugador + 9, anchoJugador, 1);
    private final Rectangle LIMITE_ABAJO = new Rectangle(Constantes.CENTRO_VENTANA_X - 25, Constantes.CENTRO_VENTANA_Y - 2, anchoJugador, 1);
    private final Rectangle LIMITE_IZQUIERDA = new Rectangle(Constantes.CENTRO_VENTANA_X - 25, Constantes.CENTRO_VENTANA_Y - altoJugador - 2, 1, altoJugador - 7);
    private final Rectangle LIMITE_DERECHA = new Rectangle(Constantes.CENTRO_VENTANA_X - 9, Constantes.CENTRO_VENTANA_Y - altoJugador - 2, 1, altoJugador - 7);

    private int resistencia = 300;
    private final int resistenciaTotal = 300;
    private int recuperacion = 100;
    private boolean recuperado = true;

    public int limitePeso = 100;
    public int pesoActual = 1;

    private final AlmacenEquipo ae;
    private ArrayList<Rectangle> alcanceArma;

    private boolean spawning;
    private boolean visible;
    private static int spawnTime = 0;
    //Parpadear o titilar
    private static int flickerTime = 0;

    private int vida;
    private boolean muerto;

    public Jugador() {
        ultimoDisparo = 0;
        puntoX = new ArrayList();
        puntoY = new ArrayList();
        direcciones = new ArrayList();
        rectanguloDisparo = new ArrayList();
        tiempoSpriteDisparo = 60;
        disparando = false;

        this.posicionX = ElementosPrincipales.mapa.getCoordenadaInicial().getX();
        this.posicionY = ElementosPrincipales.mapa.getCoordenadaInicial().getY();
        this.enMovimiento = false;
        direccion = 0;

        this.hs = new HojaSprites(Constantes.RUTA_PERSONAJE, Constantes.LADO_SPRITE, false);
        this.hojaPoder = new HojaSprites(Constantes.RUTA_PERSONAJEMEGAPODER, Constantes.LADO_SPRITE, false);
        this.disparo = new HojaSprites(Constantes.RUTA_PERSONAJEDISPARO, Constantes.LADO_SPRITE, false);

        imagenActual = hs.getSprite(direccion, 0).getImagen();
        animacion = 0;
        a = 0;
        d = 4;

        ae = new AlmacenEquipo((Cetro) RegistroObjetos.getObjeto(599));
        alcanceArma = new ArrayList();

        vida = 1000;
        visible = true;
        muerto = false;
        cambio = false;
    }

    /**
     * Renueva las "balas" del cetro Determina cuando debe estar titilando el
     * personaje por renacer Actualiza la variable que determina que Sprite se
     * dibujara del jugador
     */
    public void actualizar() {
        ultimoDisparo++;
        if (ultimoDisparo == 400) {
            puntoX = new ArrayList();
            puntoY = new ArrayList();
            direcciones = new ArrayList();
            rectanguloDisparo = new ArrayList();
        }

        if (vida == 0) {
            muerto = true;
            GestorPrincipal.ge.cambiarEstadoActual(3);
        }
        if (spawning) {

            flickerTime++;
            spawnTime++;

            if (flickerTime > Constantes.FLICKER_TIME) {

                visible = !visible;
                flickerTime = 0;
            }

            if (spawnTime > Constantes.SPAWNING_TIME) {
                spawning = false;
                visible = true;
                muerto = false;
                spawnTime = 0;
                flickerTime = 0;
            }
        }

        if (animacion < 32767) {

            animacion++;
        } else {
            animacion = 0;
        }
        gestionarVelocidadResistencia();
        enMovimiento = false;
        determinarDireccion();
        animar();
        actualizarAtaques();
        actualizarCetro();
        cambiarHojaSprite();
        actualizarSpriteDisparo();
    }

    /**
     * Comprobar si el disparo del poder colisiono con algun enemigo Elemina del
     * mapa los enemigos que ya no tienen vida
     */
    private void comprobarTiro() {
        for (Rectangle rectangle : rectanguloDisparo) {
            for (Enemigo enemigo : ElementosPrincipales.mapa.getEnemigos()) {
                if (rectangle.intersects(enemigo.getAreaDisparo())) {
                    ElementosPrincipales.jugador.getAlmacenEquipo().getCetro().atacar(enemigo);
                }
            }
        }
        Iterator<Enemigo> iterador = ElementosPrincipales.mapa.getEnemigos().iterator();
        while (iterador.hasNext()) {
            Enemigo enemigo = iterador.next();
            if (enemigo.getVidaActual() <= 0) {
                iterador.remove();
            }
        }
    }

    /**
     * Le otorga al disparo un rectangulo que le permite luego comparar para
     * saber si colisiono con algun enemigo
     */
    private void actualizarRectangulosDisparo() {
        int i = 0;
        for (Rectangle rectangle : rectanguloDisparo) {
            double numY = puntoY.get(i);
            double numX = puntoX.get(i);
            switch (direcciones.get(i)) {
                case 0:
                    rectanguloDisparo.set(i, new Rectangle((int) numX + 11, (int) numY + 26, 5, 7));
                    break;
                case 1:
                    rectanguloDisparo.set(i, new Rectangle((int) numX + 17, (int) numY, 5, 7));
                    break;
                case 2:
                    rectanguloDisparo.set(i, new Rectangle((int) numX + 20, (int) numY + 23, 7, 5));
                    break;
                case 3:
                    rectanguloDisparo.set(i, new Rectangle((int) numX + 4, (int) numY + 21, 7, 5));
                    break;
            }
            i++;
        }
    }

    /**
     * Mueve en la direccion correcta el disparo del jugador
     */
    public void actualizarSpriteDisparo() {
        int i = 0;
        for (double p : puntoX) {
            double numY = puntoY.get(i);
            double numX = puntoX.get(i);
            switch (direcciones.get(i)) {
                case 0:
                    if (direccion == 1 && enMovimiento) {
                        numY += 1 + velocidadMovimiento;
                    } else {
                        numY += 1;
                    }
                    if (direccion == 2 && enMovimiento) {
                        numX -= velocidadMovimiento;
                    } else if (direccion == 3 && enMovimiento) {
                        numX += velocidadMovimiento;
                    }
                    break;
                case 1:
                    if (direccion == 0 && enMovimiento) {
                        numY -= 1 + velocidadMovimiento;
                    } else {
                        numY -= 1;
                    }
                    if (direccion == 2 && enMovimiento) {
                        numX -= velocidadMovimiento;
                    } else if (direccion == 3 && enMovimiento) {
                        numX += velocidadMovimiento;
                    }
                    break;
                case 2:
                    if (direccion == 3 && enMovimiento) {
                        numX += 1 + velocidadMovimiento;
                    } else {
                        numX += 1;
                    }
                    if (direccion == 0 && enMovimiento) {
                        numY -= velocidadMovimiento;
                    } else if (direccion == 1 && enMovimiento) {
                        numY += velocidadMovimiento;
                    }
                    break;
                case 3:
                    if (direccion == 2 && enMovimiento) {
                        numX -= 1 + velocidadMovimiento;
                    } else {
                        numX -= 1;
                    }
                    if (direccion == 0 && enMovimiento) {
                        numY -= velocidadMovimiento;
                    } else if (direccion == 1 && enMovimiento) {
                        numY += velocidadMovimiento;
                    }
                    break;
            }
            puntoY.set(i, numY);
            puntoX.set(i, numX);
            i++;
        }
    }

    /**
     * Compueba si el usuario apreto la tecla de ESPACIO para generar un disparo
     */
    public void actualizarAtaques() {
        if (ElementosPrincipales.jugador.getAlcanceArma().isEmpty() || ElementosPrincipales.jugador.getAlmacenEquipo().getCetro() instanceof DesArmado) {
            return;
        }
        if (GestorControles.teclado.ataque) {
            if (tiempoSpriteDisparo >= 50) {
                disparando = true;
                switch (direccion) {
                    case 0:
                        imagenActual = disparo.getSprite(direccion, 0).getImagen();
                        break;
                    case 1:
                        imagenActual = disparo.getSprite(0, direccion).getImagen();
                        break;
                    case 2:
                        imagenActual = disparo.getSprite(1, 0).getImagen();
                        break;
                    case 3:
                        imagenActual = disparo.getSprite(1, 1).getImagen();
                        break;
                }
                if (tiempoSpriteDisparo == 60) {
                    ultimoDisparo = 0;
                    final double centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE;
                    final double centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE;
                    puntoX.add(centroX);
                    puntoY.add(centroY);
                    direcciones.add(direccion);
                    ElementosPrincipales.jugador.getAlmacenEquipo().getCetro().sonidoDisparo();
                    double numY = puntoY.get(puntoY.size() - 1);
                    double numX = puntoX.get(puntoX.size() - 1);
                    switch (direcciones.get(direcciones.size() - 1)) {
                        case 0:
                            rectanguloDisparo.add(new Rectangle((int) numX + 11, (int) numY + 26, 5, 7));
                            break;
                        case 1:
                            rectanguloDisparo.add(new Rectangle((int) numX + 17, (int) numY, 5, 7));
                            break;
                        case 2:
                            rectanguloDisparo.add(new Rectangle((int) numX + 20, (int) numY + 23, 7, 5));
                            break;
                        case 3:
                            rectanguloDisparo.add(new Rectangle((int) numX + 4, (int) numY + 21, 7, 5));
                            break;
                    }
                }
            }
        }
        comprobarTiro();
        actualizarRectangulosDisparo();

        if (disparando) {
            tiempoSpriteDisparo--;
            if (tiempoSpriteDisparo == 0) {
                disparando = false;
                tiempoSpriteDisparo = 60;
            }
        }
    }

    /**
     * Identificar el cetro que tiene actualmente el jugador
     */
    private void actualizarCetro() {
        if (!(ae.getCetro() instanceof DesArmado)) {
            calcularAlcance();
            ae.getCetro().actualizar();
        }
    }

    private void calcularAlcance() {
        if (!(ae.getCetro() instanceof DesArmado)) {
            alcanceArma = ae.getCetro().getAlcance(this);
        }
    }

    /**
     * Cambia la imagen del jugador si activo un centro
     */
    private void cambiarHojaSprite() {
        if (ae.getCetro() instanceof Cetro && !(ae.getCetro() instanceof DesArmado)) {
            hs = new HojaSprites(Constantes.RUTA_PERSONAJEPODER, Constantes.LADO_SPRITE, false);
            cambio = true;
        }
    }

    /**
     * Controla la resistencia de correr del jugador
     */
    private void gestionarVelocidadResistencia() {

        if (GestorControles.teclado.run && resistencia > 0) {
            velocidadMovimiento = 1.2;
            recuperado = false;
            recuperacion = 0;
        } else {
            velocidadMovimiento = 0.7;
            if (!recuperado && recuperacion < 100) {
                recuperacion++;
            }
            if (recuperacion == 100 && resistencia < resistenciaTotal) {
                resistencia++;
            }
        }
    }

    /**
     * Determina la direccion a donde esta mirando el jugador para moverse
     * correctamente y no de forma diagonal
     */
    private void determinarDireccion() {

        final int velocidadX = getVelocidadX();
        final int velocidadY = getVelocidadY();

        if (velocidadX == 0 && velocidadY == 0) {
            return;
        }
        if ((velocidadX != 0 && velocidadY == 0) || (velocidadX == 0 && velocidadY != 0)) {
            mover(velocidadX, velocidadY);
        } else {
            if (velocidadX == -1 && velocidadY == -1) {
                if (GestorControles.teclado.left.getUltimaPulsacion() > GestorControles.teclado.up.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
            if (velocidadX == -1 && velocidadY == 1) {
                if (GestorControles.teclado.left.getUltimaPulsacion() > GestorControles.teclado.down.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
            if (velocidadX == 1 && velocidadY == -1) {
                if (GestorControles.teclado.right.getUltimaPulsacion() > GestorControles.teclado.up.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
            if (velocidadX == 1 && velocidadY == 1) {
                if (GestorControles.teclado.right.getUltimaPulsacion() > GestorControles.teclado.down.getUltimaPulsacion()) {
                    mover(velocidadX, 0);
                } else {
                    mover(0, velocidadY);
                }
            }
        }
    }

    /**
     * Dependiendo de la tecla presionada detecta a donde se dirigira el jugador
     *
     * @return direccion en x
     */
    private int getVelocidadX() {

        int velocidadX = 0;

        if (GestorControles.teclado.left.isPulsada() && !GestorControles.teclado.right.isPulsada()) {
            velocidadX = -1;
        } else if (GestorControles.teclado.right.isPulsada() && !GestorControles.teclado.left.isPulsada()) {
            velocidadX = 1;
        }
        return velocidadX;
    }

    /**
     * Dependiendo de la tecla presionada detecta a donde se dirigira el jugador
     *
     * @return direccion en y
     */
    private int getVelocidadY() {

        int velocidadY = 0;

        if (GestorControles.teclado.up.isPulsada() && !GestorControles.teclado.down.isPulsada()) {
            velocidadY = -1;
        } else if (GestorControles.teclado.down.isPulsada() && !GestorControles.teclado.up.isPulsada()) {
            velocidadY = 1;
        }
        return velocidadY;
    }

    /**
     * Permite el el jugador se mueva por el mapa
     *
     * @param velocidadX
     * @param velocidadY
     */
    private void mover(final int velocidadX, final int velocidadY) {

        enMovimiento = true;

        cambiarDireccion(velocidadX, velocidadY);

        if (!fueraMapa(velocidadX, velocidadY)) {

            if (velocidadX == -1 && !enColisionIzquierda(velocidadX)) {
                posicionX += velocidadX * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
            if (velocidadX == 1 && !enColisionDerecha(velocidadX)) {
                posicionX += velocidadX * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
            if (velocidadY == -1 && !enColisionArriba(velocidadY)) {
                posicionY += velocidadY * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
            if (velocidadY == 1 && !enColisionAbajo(velocidadY)) {
                posicionY += velocidadY * velocidadMovimiento;
                if (GestorControles.teclado.run && resistencia > 0) {
                    resistencia--;
                }
            }
        }
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
            int origenY = area.y + velocidadY * (int) (velocidadMovimiento + 0.6) + 3 * (int) (velocidadMovimiento + 0.6);

            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            if (LIMITE_ARRIBA.intersects(areaFutura)) {
                enMovimiento = false;
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
            int origenY = area.y + velocidadY * (int) (velocidadMovimiento + 0.6) - 3 * (int) (velocidadMovimiento + 0.6);

            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            if (LIMITE_ABAJO.intersects(areaFutura)) {
                enMovimiento = false;
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

            int origenX = area.x + velocidadX * (int) (velocidadMovimiento + 0.6) + 3 * (int) (velocidadMovimiento + 0.6);
            int origenY = area.y;

            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            if (LIMITE_IZQUIERDA.intersects(areaFutura)) {
                enMovimiento = false;
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

            int origenX = area.x + velocidadX * (int) (velocidadMovimiento + 0.6) - 3 * (int) (velocidadMovimiento + 0.6);
            int origenY = area.y;

            final Rectangle areaFutura = new Rectangle(origenX, origenY, area.width, area.height);

            if (LIMITE_DERECHA.intersects(areaFutura)) {
                enMovimiento = false;
                return true;
            }
        }
        return false;
    }

    /**
     * @param velocidadX, velocidadY
     * @return (booleano) si el jugador va a salir del mapa
     */
    private boolean fueraMapa(final int velocidadX, final int velocidadY) {

        int posicionFuturaX = (int) posicionX + velocidadX * (int) (velocidadMovimiento + 0.6);
        int posicionFuturaY = (int) posicionY + velocidadY * (int) (velocidadMovimiento + 0.6);

        final Rectangle bordesMapa = ElementosPrincipales.mapa.getBordes(posicionFuturaX, posicionFuturaY);

        final boolean FUERA;

        if (LIMITE_ARRIBA.intersects(bordesMapa) || LIMITE_ABAJO.intersects(bordesMapa) || LIMITE_IZQUIERDA.intersects(bordesMapa)
                || LIMITE_DERECHA.intersects(bordesMapa)) {

            FUERA = false;
        } else {
            FUERA = true;
        }

        return FUERA;
    }

    /**
     * Detecta a donde esta mirando el jugador
     *
     * @param velocidadX
     * @param velocidadY
     */
    private void cambiarDireccion(final int velocidadX, final int velocidadY) {

        if (velocidadX == -1) {
            direccion = 3;
        } else if (velocidadX == 1) {
            direccion = 2;
        }
        if (velocidadY == -1) {
            direccion = 1;
        } else if (velocidadY == 1) {
            direccion = 0;
        }
    }

    /**
     * Le otorga a la variable de imagenActual el valor de imagen que se debe
     * mostrar en pantalla, con respecto a la dirección a la que mire
     *
     */
    private void animar() {

        if (enMovimiento) {
            GestorControles.teclado.poder = false;
            if (animacion % 10 == 0) {
                a++;
                if (a >= 4) {
                    a = 0;
                }
            }
            switch (a) {
                case 0:
                    imagenActual = hs.getSprite(direccion, 1).getImagen();
                    break;
                case 1:
                    imagenActual = hs.getSprite(direccion, 0).getImagen();
                    break;
                case 2:
                    imagenActual = hs.getSprite(direccion, 2).getImagen();
                    break;
                case 3:
                    imagenActual = hs.getSprite(direccion, 0).getImagen();
                    break;
            }
        } else {
            if (cambio) {
                if (!GestorControles.teclado.poder) {
                    a = 0;
                    d = 0;
                }
                if (GestorControles.teclado.poder) {
                    if (animacion % 13 == 0) {
                        a++;
                        if (a >= 3) {
                            a = 0;
                            d++;
                            if (d >= 3) {
                                d = 0;
                            }
                        }
                    }
                    imagenActual = hojaPoder.getSprite(d, a).getImagen();
                    direccion = 0;
                } else {
                    imagenActual = hs.getSprite(direccion, 0).getImagen();
                }
            } else {
                imagenActual = hs.getSprite(direccion, 0).getImagen();
            }
        }
    }

    /**
     * Si el jugador muere o aparece por primera vez, le otorga nueva vida
     */
    public void renacer() {
        this.posicionX = ElementosPrincipales.mapa.getCoordenadaInicial().x;
        this.posicionY = ElementosPrincipales.mapa.getCoordenadaInicial().y;
        this.direccion = 0;
        vida = 1000;
        spawning = true;
    }

    public void dibujar(Graphics g) {
        if (!visible) {
            return;
        }

        final int centroX = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE;
        final int centroY = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE;

        DibujoOpciones.dibujarImagen(g, imagenActual, centroX, centroY);

        int i = 0;
        for (double p : puntoX) {
            double numY = puntoY.get(i);
            int y = (int) numY;
            switch (direcciones.get(i)) {
                case 0:
                    DibujoOpciones.dibujarImagen(g, disparo.getSprite(2, 0).getImagen(), (int) p, y);
                    break;
                case 1:
                    DibujoOpciones.dibujarImagen(g, disparo.getSprite(2, 1).getImagen(), (int) p, y);
                    break;
                case 2:
                    DibujoOpciones.dibujarImagen(g, disparo.getSprite(3, 0).getImagen(), (int) p, y);
                    break;
                case 3:
                    DibujoOpciones.dibujarImagen(g, disparo.getSprite(3, 1).getImagen(), (int) p, y);
                    break;
            }
            i++;
        }
    }

    public void setPosicionX(double posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicionY(double posicionY) {
        this.posicionY = posicionY;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public int getPosicionXINT() {
        return (int) posicionX;
    }

    public int getPosicionYINT() {
        return (int) posicionY;
    }

    public Rectangle getLIMITE_ARRIBA() {
        return LIMITE_ARRIBA;
    }

    public int getAnchoJugador() {
        return anchoJugador;
    }

    public int getAltoJugador() {
        return altoJugador;
    }

    public int getResistencia() {
        return resistencia;
    }

    public int getResistenciaTotal() {
        return resistenciaTotal;
    }

    public AlmacenEquipo getAlmacenEquipo() {
        return ae;
    }

    public int getDireccion() {
        return direccion;
    }

    public ArrayList<Rectangle> getAlcanceArma() {
        return alcanceArma;
    }

    /**
     * Devuelve un rectangulo que es usado para detectar las coliciones del
     * enemigo
     *
     * @return rectangulo
     */
    public Rectangle getArea() {
        return new Rectangle(Constantes.CENTRO_VENTANA_X - Constantes.LADO_SPRITE + 8, Constantes.CENTRO_VENTANA_Y - Constantes.LADO_SPRITE + 10, Constantes.LADO_SPRITE / 2, Constantes.LADO_SPRITE - 10);
    }

    /**
     * Reduce la vida del jugador
     *
     * @param bajoVida (numero de vida que se le restara al jugador )
     */
    public void reducirVida(final int bajoVida) {
        if (vida - bajoVida < 0) {
            vida = 0;
        } else {
            vida -= bajoVida;
        }
    }

    /**
     * Aumenta la vida del jugador
     *
     * @param subeVida (numero de vida que se le sumara al jugador )
     */
    public void aumentarVida(final int subeVida) {
        if (vida + subeVida > 1000) {
            vida = 1000;
        } else {
            vida += subeVida;
        }
    }

    public int getVida() {
        return vida;
    }

    public boolean isMuerto() {
        return muerto;
    }

}
