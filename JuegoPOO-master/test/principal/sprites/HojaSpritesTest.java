package principal.sprites;

import java.awt.image.BufferedImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import principal.herramientas.ComparadorRecursos;

/**
 *
 * @author Luis Evilla
 */
public class HojaSpritesTest {

    private HojaSprites hoja;
    private HojaSprites hoja2;

    public HojaSpritesTest() {
        String separador = System.getProperty("file.separator");
        
        this.hoja = new HojaSprites(separador + "imagenes" + separador + "hojas_Personajes" + separador + "Santana.png", 32, false);
        this.hoja2 = new HojaSprites(separador + "imagenes" + separador + "hojas_Personajes" + separador + "SantanaDisparo.png", 32, false);
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Verificando SpriteSheet y sprites");
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
    public void testCargarHoja() {
        assertNotNull("Error cargando una hoja", this.hoja);
        assertNotSame("Error está arrojando una misma instancia", hoja, hoja2);
    }

    @Test
    public void testCargarSprite() {
        Sprite sprite = hoja.getSprite(0);
        Sprite sprite2 = hoja.getSprite(1);
        assertNotNull("Error cargando sprite de la hoja", sprite);
        assertNotSame("Error está arrojando una misma instancia", sprite, sprite2);
    }

    @Test
    public void testObtenerImagenSprite() {
        float similitud = 0;
        
        BufferedImage imagen = this.hoja.getSprite(0).getImagen();
        BufferedImage imagen2 = this.hoja.getSprite(1).getImagen();
        
        assertNotNull("Error obeteniendo la imagen del sprite", imagen);
        assertNotNull("Error obeteniendo la imagen del sprite", imagen2);
        
        //Si el porcentaje de similitud no es 100, significa que las imagenes son distintas
        similitud = ComparadorRecursos.compararImagenes(imagen, imagen2);
        assertNotEquals("Error, las imagenes no deben ser iguales", 100, (int) (similitud));
        
        similitud = ComparadorRecursos.compararImagenes(imagen, imagen);
        assertEquals("Error, deberia retornar las mismas imagenes", 100, (int) (similitud));
    }

}
