package principal.entes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.herramientas.DibujoOpciones;
import principal.sonido.GestorSonido;
import principal.sprites.HojaSprites;

/**
 * Un tipo de enemigo en el mapa
 * @author Dylan
 */
public class Guardian extends Enemigo {

    private static HojaSprites hojaZombie;

    private int animacion;
    private static int contadorAtaque;
    private int a;

    GestorSonido ataque;

    private BufferedImage imagenActual;

    public Guardian(int id, String nombre, int vidaMaxima, final String rutaLamento) {
        super(id, nombre, vidaMaxima, rutaLamento, 6 * Constantes.LADO_SPRITE);

        if (hojaZombie == null) {
            hojaZombie = new HojaSprites(Constantes.RUTA_ZOMBIE + id + ".png", Constantes.LADO_SPRITE, false);
        }

        ataque = new GestorSonido("/sonidos/Golpe.wav");

        animacion = 0;
        contadorAtaque = 0;
        a = 0;

        imagenActual = hojaZombie.getSprite(direccion, 0).getImagen();
    }

    /**
     * Actualiza la variable que determina que Sprite se dibujara del guardian
     * Determina si el guardian esta en colision con el jugador y le permite
     * quitarle vida
     *
     * @param enemigos (guardianes en el mapa)
     */
    @Override
    public void actualizar(ArrayList<Enemigo> enemigos) {
        super.actualizar(enemigos);
        if (animacion < Integer.MAX_VALUE) {
            animacion++;
        } else {
            animacion = 0;
        }
        if (getAreaDisparo().intersects(ElementosPrincipales.jugador.getArea())) {
            if (contadorAtaque == 0 && !ElementosPrincipales.jugador.isMuerto()) {
                ataque.detenerSonido();
                Random r = new Random();
                int num = r.nextInt(ataqueMax - ataqueMin) + ataqueMin;
                ElementosPrincipales.jugador.reducirVida(num);
                ataque.reproducir();
                contadorAtaque = 500;
            }
        }
        if (contadorAtaque > 0) {
            contadorAtaque--;
        }
        animar();
    }

    @Override
    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        super.dibujar(g, puntoX, puntoY);
        DibujoOpciones.dibujarImagen(g, imagenActual, puntoX, puntoY);
    }

    /**
     * Le otorga a la variable de imagenActual el valor de imagen que se debe
     * mostrar en pantalla, con respecto a la dirección a la que mire
     *
     */
    private void animar() {

        if (enMovimiento) {
            if (animacion % 10 == 0) {
                a++;
                if (a >= 4) {
                    a = 0;
                }
            }
            switch (a) {
                case 0:
                    imagenActual = hojaZombie.getSprite(direccion, 1).getImagen();
                    break;
                case 1:
                    imagenActual = hojaZombie.getSprite(direccion, 0).getImagen();
                    break;
                case 2:
                    imagenActual = hojaZombie.getSprite(direccion, 2).getImagen();
                    break;
                case 3:
                    imagenActual = hojaZombie.getSprite(direccion, 0).getImagen();
                    break;
            }
        } else {
            imagenActual = hojaZombie.getSprite(direccion, 0).getImagen();
        }
    }

}
