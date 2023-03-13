package principal;

import principal.entes.Jugador;
import principal.inventario.Inventario;
import principal.mapas.MapaTiled;

/**
 * Guarda la información actual del jugador(nivel de vida,
 * resistencia etc..), del mapa(cofres, enemigos presentes en el, posición de enemigos
 * y jugador etc...), y la información detallada del inventario del jugador(consumibles, y equipo).
 * 
 * Su principal función es servir como intermediario entre los elementos principales,
 * mencionados anteriormente y las demás clases, permitiendo manipular y acceder a la información de estos,
 * y usar sus métodos.
 */
public class ElementosPrincipales {

    public static MapaTiled mapa = new MapaTiled("/mapas/apocalypse.json");
    public static Jugador jugador = new Jugador();
    public static Inventario inventario = new Inventario();
}
