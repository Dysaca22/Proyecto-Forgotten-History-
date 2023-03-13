package principal.inventario;

import principal.inventario.poderes.DesArmado;
import principal.inventario.poderes.Poder;
import principal.inventario.consumible.Consumible;
import principal.inventario.elementosMujeres.Elemento;

/**
 * Registo completo de todos los objetos que hay en el juego y se le otorga un
 * id
 *
 * @author Dylan
 */
public class RegistroObjetos {

    private static int dificultad = 0;

    public static Objeto getObjeto(int idObjeto) {
        Objeto objeto = null;
        switch (idObjeto) {
            //Entre el 0 y el 499 son objetos consumibles
            //  De 500 en adelanete son poderes
            // Esto anterior es por su id

            //Consumibles
            case 0:
                objeto = new Consumible(idObjeto, "Libro", "Sin descripcion");
                break;
            case 1:
                objeto = new Consumible(idObjeto, "Botiquin", "Sin descripcion");
                break;
            //Elementos mujeres
            case 2:
                objeto = new Elemento(idObjeto, "Rosalind Franklin", "Sin descripcion");
                break;
            case 3:
                objeto = new Elemento(idObjeto, "Jocelyn Bell", "Sin descripcion");
                break;
            case 4:
                objeto = new Elemento(idObjeto, "Equipo Computadoras Humanas", "Sin descripcion");
                break;
            case 5:
                objeto = new Elemento(idObjeto, "Ada Lovelace", "Sin descripcion");
                break;
            case 6:
                objeto = new Elemento(idObjeto, "Huelga en Islandia", "Sin descripcion");
                break;
            //Poderes
            case 500:
                objeto = new Poder(idObjeto, "Cetro de Poder", "Sin descripcion", 12 - dificultad, 20 - dificultad, false, true, 0.7);
                break;
            case 599:
                objeto = new DesArmado(idObjeto, "manos", "Sin descripcion", 1, 1, false, false, 0);
                break;
        }
        return objeto;
    }

    public static void setDificultad(final int i) {
        dificultad = i;
    }
}
