package principal.graficos;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import principal.Constantes;
import principal.herramientas.CargadorRecursos;

/**
 * Ventana donde se muestra el juego
 *
 * @author Dylan
 */
public class Ventana extends JFrame {

    private static final long serialVersionUID = 189259498287192152L;

    private final String titulo;
    private final ImageIcon icono;

    public Ventana(final String titulo, final SuperficieDibujo sd) {

        this.titulo = titulo;

        BufferedImage imagen = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.RUTA_ICONO_VENTANA);
        this.icono = new ImageIcon(imagen);

        configurarVentana(sd);
    }

    private void configurarVentana(final SuperficieDibujo sd) {

        setTitle(titulo);
        setIconImage(icono.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        add(sd, BorderLayout.CENTER);
        //Quitar los bordes de la ventana
        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
