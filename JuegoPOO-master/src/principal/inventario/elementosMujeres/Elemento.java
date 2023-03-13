package principal.inventario.elementosMujeres;

import principal.Constantes;
import principal.GestorPrincipal;
import principal.inventario.Objeto;
import principal.maquinaestado.estado.logros.Mujer;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 * Items que el jugar recolecta al rededor del mapa que representan a las
 * mujeres y son logros
 *
 * @author Dylan
 */
public class Elemento extends Objeto {

    public static HojaSprites hojaConsumibles = new HojaSprites(Constantes.RUTA_OBJETOS, 32, false);

    public Elemento(int id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
    }

    public void asignarMujer(final Mujer mujer) {
        if (nombre.equals("Rosalind Franklin")) {
            mujer.setImagenBiografia(Constantes.BIOGRAFIAROSALIND);
        }
        if (nombre.equals("Jocelyn Bell")) {
            mujer.setImagenBiografia(Constantes.BIOGRAFIAJOCELYN);
        }
        if (nombre.equals("Equipo Computadoras Humanas")) {
            mujer.setImagenBiografia(Constantes.BIOGRAFIAE_C_U);
        }
        if (nombre.equals("Ada Lovelace")) {
            mujer.setImagenBiografia(Constantes.BIOGRAFIAADA_LOVELACE);
        }
        if (nombre.equals("Huelga en Islandia")) {
            mujer.setImagenBiografia(Constantes.BIOGRAFIAISLANDIA);
        }

    }

    public void cambiarEstado() {
        GestorPrincipal.ge.cambiarEstadoActual(7);
    }

    @Override
    public Sprite getSprite() {
        return hojaConsumibles.getSprite(id);
    }
}
