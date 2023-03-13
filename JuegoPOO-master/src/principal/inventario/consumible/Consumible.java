package principal.inventario.consumible;

import principal.Constantes;
import principal.inventario.Objeto;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 * Items con que el jugador puede interactuar como el botiquin o el libro
 *
 * @author Dylan
 */
public class Consumible extends Objeto {

    public static HojaSprites hojaConsumibles = new HojaSprites(Constantes.RUTA_OBJETOS, 32, false);

    public Consumible(int id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
    }

    @Override
    public Sprite getSprite() {
        return hojaConsumibles.getSprite(id);
    }

}
