package principal.maquinaestado.estado.menuinicial.itemsMenu;

import java.io.File;
import java.util.ArrayList;

/**
 * Clase que guarda las patidas jugadas. Todavia en desarrollo
 *
 * @author Dylan
 */
public class CargarPartida {

    private final ArrayList<File> partidas;

    public CargarPartida() {
        partidas = new ArrayList();
    }

    public ArrayList<File> getPartidas() {
        return partidas;
    }

}
