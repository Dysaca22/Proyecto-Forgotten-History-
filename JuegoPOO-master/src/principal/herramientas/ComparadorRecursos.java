package principal.herramientas;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import javax.sound.sampled.Clip;

/**
 * Comparador de recursos (imagenes, fuentes, audios)
 *
 * @author Luis Evilla
 */
public class ComparadorRecursos {

    public static float compararImagenes(BufferedImage fuente, BufferedImage imagen2) {
        float porcentaje = 0;

        try {
            // take buffer data from both image files 
            DataBuffer dbA = fuente.getData().getDataBuffer();
            int sizeA = dbA.getSize();

            DataBuffer dbB = imagen2.getData().getDataBuffer();
            int sizeB = dbB.getSize();

            int count = 0;

            // compare data-buffer objects
            if (sizeA == sizeB) {
                for (int i = 0; i < sizeA; i++) {
                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }
                }
                porcentaje = (count * 100) / sizeA;
            } else {
                System.out.println("Las imageens no tienen el mismo tamaño");
                porcentaje = 0;
            }
        } catch (Exception e) {
            System.out.println("Error extrayendo los datos de los recursos");
            e.printStackTrace();
        }

        return porcentaje;
    }

    public static boolean compararFuentes(Font fuente, Font fuente2) {
        boolean iguales = false;

        // take buffer data from both image files 
        String name = fuente.getName();
        String name2 = fuente2.getName();

        if (name.equals(name2)) {
            iguales = true;
        }

        return iguales;
    }

    public static boolean compararAudios(Clip audio, Clip audio2) {
        boolean iguales = false;

        try {
            int sizeA = audio.getBufferSize();

            int sizeB = audio2.getBufferSize();

            int count = 0;

            // compare data-buffer objects
            if (sizeA == sizeB) {
                iguales = true;
            } else {
                System.out.println("Los audios no tienen el mismo tamaño");
            }
        } catch (Exception e) {
            System.out.println("Error extrayendo los datos de los recursos");
            e.printStackTrace();
        }

        return iguales;
    }

}
