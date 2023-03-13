package principal.herramientas;

import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luis Evilla
 */
public class CalcularDistanciaTest {
    
    public CalcularDistanciaTest() {
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

    /**
     * Test of getDistanciaEntrePuntos method, of class CalcularDistancia.
     */
    @Test
    public void testGetDistanciaEntrePuntos() {
        System.out.println("getDistanciaEntrePuntos diferentes");
        Point punto1 = new Point(320, 160);
        Point punto2 = new Point(640, 320);
        double expResult = 357.77087639996637;
        double result = CalcularDistancia.getDistanciaEntrePuntos(punto1, punto2);
        assertEquals("La distancia entre ambos puntos no es correcta", expResult, result, 0.0);
    }
    
    /**
     * Test of getDistanciaEntrePuntos method, of class CalcularDistancia.
     */
    @Test
    public void testGetDistanciaEntrePuntosIguales() {
        System.out.println("getDistanciaEntrePuntos iguales");
        Point punto1 = new Point(320, 160);
        Point punto2 = new Point(320, 160);
        double expResult = 0;
        double result = CalcularDistancia.getDistanciaEntrePuntos(punto1, punto2);
        assertEquals("La distancia entre ambos puntos no es correcta", expResult, result, 0.0);
    }
}
