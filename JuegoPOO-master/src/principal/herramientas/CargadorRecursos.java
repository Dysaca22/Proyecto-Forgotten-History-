//Obtener sonidos, imagenes, funetes
package principal.herramientas;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Contiene los métodos estaticos para cargar recursos multimedia como audios,
 * imagenes, textos, fuentes etc...
 * 
 */
public class CargadorRecursos {
    
    public static String separador = System.getProperty("file.separator");
    public static String BASEURL = System.getProperty("user.dir") + separador + "Resources";
    
    /**
     * Carga una imagen multimedia desde la ruta asignada y la hace solida(no
     * puede ser atravesada por el jugador".
     *
     * @param ruta ruta de la imagen ej: "/imagenes/hojas_Personajes/Santana.png"
     * @return imagen en forma de buffer (BufferedImage)
     */
    public static BufferedImage cargarImagenCompatibleOpaca(final String ruta) {

        Image imagen = null;

        try {
            imagen = ImageIO.read(new File(BASEURL + separador + ruta));
        } catch (IOException ex) {
            System.out.println(ruta);
            System.out.println("error al leer la imagen opaca");
            ex.printStackTrace();
        }

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        BufferedImage imagenAcelerada = gc.createCompatibleImage(imagen.getWidth(null), imagen.getHeight(null),
                Transparency.OPAQUE);

        Graphics g = imagenAcelerada.getGraphics();
        g.drawImage(imagen, 0, 0, null);
        g.dispose();

        return imagenAcelerada;
    }

    /**
     * Carga una imagen multimedia desde la ruta asignada y hace que el jugador
     * pueda atravesarla
     *
     * @param ruta ruta de la imagen ej: "/imagenes/iconos/iconoCursor.png"
     * @return imagen en forma de buffer (BufferedImage)
     */
    public static BufferedImage cargarImagenCompatibleTranslucida(final String ruta) {

        Image imagen = null;

        try {
            imagen = ImageIO.read(new File(BASEURL + separador + ruta));
        } catch (IOException ex) {
            System.out.println(ruta);
            System.out.println("error al leer la imagen translucida");
            ex.printStackTrace();
        }

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        BufferedImage imagenAcelerada = gc.createCompatibleImage(imagen.getWidth(null), imagen.getHeight(null),
                Transparency.TRANSLUCENT);

        Graphics g = imagenAcelerada.getGraphics();
        g.drawImage(imagen, 0, 0, null);
        g.dispose();
        return imagenAcelerada;
    }

    /**
     * Leer el texto para poder hacer los mapas
     *
     * @param ruta ruta del archivo ej: "/mapas/mapa1".
     * @return la información del mapa en forma de string.
     */
    public static String leerArchivoTexto(final String ruta) {

        String contenido = "";

        InputStream entradaBytes = ClassLoader.class.getResourceAsStream(ruta);
        BufferedReader lector = new BufferedReader(new InputStreamReader(entradaBytes));

        String linea;

        try {
            while ((linea = lector.readLine()) != null) {
                contenido += linea;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (entradaBytes != null) {
                    entradaBytes.close();
                }
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException ex) {
                System.out.println("error al leer archivo texto");
                ex.printStackTrace();
            }
        }
        return contenido;
    }

    /**
     * Carga una fuente desde un archivo
     *
     * @param ruta ruta del archivo de la fuente ej: "/fuentes/Crumbled-Pixels.ttf"
     * @return La fuente.
     */
    public static Font cargarFuente(final String ruta) {

        Font fuente = null;
        InputStream entradaBytes = ClassLoader.class.getResourceAsStream(ruta);
        try {
            fuente = Font.createFont(Font.TRUETYPE_FONT, entradaBytes);
        } catch (FontFormatException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Tamaño de la fuente
        fuente = fuente.deriveFont(11f);
        return fuente;
    }

    /**
     * Carga un audio desde un archivo
     *
     * @param ruta ruta del archivo del archivo de audio ej "/sonidos/MusicaIntro.wav"
     * @return La fuente.
     */
    public static Clip cargarSonido(final String ruta) {
        Clip clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(BASEURL + separador + ruta));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception ex) {
            System.out.println(ruta);
            System.out.println("Error al reproducir el sonido.");
            ex.printStackTrace();
        }
        return clip;
    }
}
