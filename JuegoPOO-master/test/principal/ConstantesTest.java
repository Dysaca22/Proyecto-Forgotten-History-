package principal;

import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import principal.herramientas.CargadorRecursos;

/**
 *
 * @author Luis Evilla
 */
public class ConstantesTest {

    public ConstantesTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRutas() {
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("Verificando las rutas");
        assertThat("La ruta de los mapas es erronea", Constantes.RUTA_MAPA, is("/mapas/mapa1"));
        assertThat("La ruta del ratón es erronea", Constantes.RUTA_ICONO_RATON, is("/imagenes/iconos/iconoCursor.png"));
        assertThat("La ruta del personaje es erronea", Constantes.RUTA_PERSONAJE, is("/imagenes/hojas_Personajes/Santana.png"));
        assertThat("La ruta del enemigo zombie es erronea", Constantes.RUTA_ZOMBIE, is("/imagenes/hojas_de_enemigos/"));
        assertThat("La ruta del icono de la ventana es erronea", Constantes.RUTA_ICONO_VENTANA, is("/imagenes/iconos/iconV.png"));
        assertThat("La ruta de la imagen del logopito es erronea", Constantes.RUTA_LOGOTIPO, is("/imagenes/iconos/logo.png"));
        assertThat("La ruta del spreedSheet de los objetos es erronea", Constantes.RUTA_OBJETOS, is("/imagenes/hojas_de_objetos/1.png"));
        assertEquals("La ruta de la fuente de tipografia es erronea", Constantes.FUENTE_PIXEL,CargadorRecursos.cargarFuente("/fuentes/Crumbled-Pixels.ttf"));
    }
    
    @Test
    public void testLados() {
        assertEquals("El tamaño de un sprite deberia ser de 32", Constantes.LADO_SPRITE, 32);
        assertEquals("El tamaño de un Tile deberia ser de 32", Constantes.LADO_TILE, 32);
    }

}
