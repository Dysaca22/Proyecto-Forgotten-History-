package principal.entes;

/**
 * La clase contene el registor de los enemigos disponibles en el juego Por
 * ahora solo contiene uno
 *
 * @author Dylan
 */
public class RegistroEnemigos {

    private static int dificultad = 0;

    public static Enemigo getEnemigo(final int idEnemigo) {
        Enemigo enemigo = null;

        switch (idEnemigo) {
            case 1:
                enemigo = new Guardian(idEnemigo, "Guardian", 40 + dificultad, "/sonidos/Enemigo.wav");
                break;
            default:
                System.out.println("No existe el enemigo");
                break;
        }

        return enemigo;
    }

    public static void setDificultad(int i) {
        dificultad = i;
    }
}
