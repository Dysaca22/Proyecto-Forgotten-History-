package principal.mapas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import principal.Constantes;
import principal.ElementosPrincipales;
import principal.GestorPrincipal;
import static principal.GestorPrincipal.sd;
import principal.control.GestorControles;
import principal.entes.Enemigo;
import principal.entes.RegistroEnemigos;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoOpciones;
import principal.herramientas.EscaladorElementos;
import principal.inventario.ContenedorObjetos;
import principal.maquinaestado.estado.juego.GestorJuego;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

/**
 * Control mapa del juego
 *
 * @author Dylan
 */
public class MapaTiled {

    private final int anchoMapaTiled;
    private final int altoMapaTiled;
    private final Point coordenadaInicial;

    private final ArrayList<CapaSprites> capaSprites;
    private final ArrayList<CapaColisiones> capaColisiones;

    private final ArrayList<Rectangle> areasColisionLectura;
    private ArrayList<Rectangle> areasColisionActualizado;
    private final Sprite[] paletaSprite;

    private final ArrayList<ContenedorObjetos> contendoresObjetos;
    private final ArrayList<Enemigo> enemigos;

    private final BufferedImage pausaSinMouse = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.BOTONPAUSA);
    private final BufferedImage pausaConMouse = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.BOTONPAUSACONMOUSE);
    private BufferedImage pausaActual;
    private final Rectangle recPausa = new Rectangle(2, 2, pausaSinMouse.getWidth(), pausaSinMouse.getHeight());
    Rectangle posicionRaton = new Rectangle();

    private int dificultad = 0;

    private int contZombies;

    /**
     * Lectura de el archivo json del mapa
     *
     * @param ruta
     */
    public MapaTiled(final String ruta) {
        //Leer archivo de texto
        String contenido = CargadorRecursos.leerArchivoTexto(ruta);
        //Convertir a Json el texto
        JSONObject todoJSON = getObjetoJSON(contenido);
        //Ancho y alto
        anchoMapaTiled = getIntDelJSON(todoJSON, "width");
        altoMapaTiled = getIntDelJSON(todoJSON, "height");
        //Posicion inicial del personaje
        JSONObject puntoInicial = getObjetoJSON(todoJSON.get("start").toString());
        coordenadaInicial = new Point(getIntDelJSON(puntoInicial, "x"), getIntDelJSON(puntoInicial, "y"));
        //Capas
        JSONArray capas = getArrayJSON(todoJSON.get("layers").toString());
        capaSprites = new ArrayList();
        capaColisiones = new ArrayList();
        //Iniciar capas
        iniciarCapas(capas);
        //Combinar coliones en un solo array
        areasColisionLectura = new ArrayList();
        areasColisionActualizado = new ArrayList();
        establecerAreasColision();
        //Averiguar cuantos sprites hay en todas las capas
        JSONArray coleccionSprites = getArrayJSON(todoJSON.get("tilesets").toString());
        int totalSprites = 0;
        for (int i = 0; i < coleccionSprites.size(); i++) {
            JSONObject datoGroup = getObjetoJSON(coleccionSprites.get(i).toString());
            totalSprites += getIntDelJSON(datoGroup, "tilecount");
        }

        //Iniciar los sprites
        paletaSprite = new Sprite[totalSprites];
        //Asignar los sprites necesarios
        asignarSprites(coleccionSprites);

        //Obtener objetos en el mapa
        contendoresObjetos = new ArrayList();
        JSONArray coleccionObjetos = getArrayJSON(todoJSON.get("contenedores").toString());
        getObjetosMapa(coleccionObjetos);

        //Obtener enemigos en el mapa
        enemigos = new ArrayList();
        contZombies = 0;
        JSONArray coleccionEnemigos = getArrayJSON(todoJSON.get("enemigos").toString());
        getEnemigosMapa(coleccionEnemigos);
        pausaActual = pausaSinMouse;
    }

    public void actualizar() {
        actualizarAreasColision();
        actualizarRecogidaObjetos();
        actualizarEnemigos();
        contZombies++;
        if (contZombies == 480 - dificultad) {
            contZombies = 0;
            getGuardianesMapa();
        }
        posicionRaton = new Rectangle(EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).x,
                EscaladorElementos.escalarPuntoAbajo(sd.getRaton().getPosicion()).y, 1, 1);
        if (posicionRaton.intersects(recPausa)) {
            pausaActual = pausaConMouse;
            //Muestra el menu de pausa
            if (sd.getRaton().isClickIzquierdo()) {
                GestorPrincipal.ge.cambiarEstadoActual(5);
                GestorControles.teclado.menuPausa = true;
            }
        } else {
            pausaActual = pausaSinMouse;
        }
    }

    /**
     * Detectar y recoger objetos del mapa
     */
    private void actualizarRecogidaObjetos() {
        //Saber su en el mapa hay objetos por recoger
        if (!contendoresObjetos.isEmpty()) {
            //rectangulos que rodenan al contenedor y al jugador
            final Rectangle areaJugador = new Rectangle(ElementosPrincipales.jugador.getPosicionXINT(), ElementosPrincipales.jugador.getPosicionYINT(), Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
            for (int i = 0; i < contendoresObjetos.size(); i++) {
                final ContenedorObjetos contenedor = contendoresObjetos.get(i);
                final Rectangle posicionObjetoActual = new Rectangle(contenedor.getPosicion().x,
                        contenedor.getPosicion().y, Constantes.LADO_SPRITE / 2, Constantes.LADO_SPRITE / 2);

                if (areaJugador.intersects(posicionObjetoActual) && GestorControles.teclado.recoger
                        && GestorPrincipal.ge.getEstadoActual() instanceof GestorJuego && !contenedor.recogido()) {
                    ElementosPrincipales.inventario.recogerObjetos(contenedor);
                    contendoresObjetos.get(i).setCofre();
                }
            }
        }
    }

    /**
     * Obtener las colisiones del mapa
     */
    private void actualizarAreasColision() {
        if (!areasColisionActualizado.isEmpty()) {
            areasColisionActualizado.clear();
        }
        for (int i = 0; i < areasColisionLectura.size(); i++) {
            Rectangle rInicial = areasColisionLectura.get(i);
            int puntoX = rInicial.x - ElementosPrincipales.jugador.getPosicionXINT() + Constantes.MARGEN_X;
            int puntoY = rInicial.y - ElementosPrincipales.jugador.getPosicionYINT() + Constantes.MARGEN_Y;

            final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
            areasColisionActualizado.add(rFinal);
        }
    }

    private void actualizarEnemigos() {
        if (!enemigos.isEmpty()) {
            for (Enemigo enemigo : enemigos) {
                enemigo.actualizar(enemigos);
            }
        }
    }

    public void dibujar(final Graphics g) {
        for (int i = 0; i < capaSprites.size(); i++) {
            int[] spritesCapa = capaSprites.get(i).getSprites();
            for (int y = 0; y < altoMapaTiled; y++) {
                for (int x = 0; x < anchoMapaTiled; x++) {
                    int idSpriteActual = spritesCapa[x + y * anchoMapaTiled];
                    if (idSpriteActual != -1) {
                        int puntoX = x * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getPosicionXINT() + Constantes.MARGEN_X;
                        int puntoY = y * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getPosicionYINT() + Constantes.MARGEN_Y;

                        //Solo dibujar lo que esta dentro de la pantalla
                        if (puntoX < -Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                                || puntoY < -Constantes.LADO_SPRITE || puntoY > Constantes.ALTO_JUEGO - 55) {
                            continue;
                        }
                        DibujoOpciones.dibujarImagen(g, paletaSprite[idSpriteActual].getImagen(), puntoX, puntoY);
                    }
                }
            }
        }

        for (int i = 0; i < contendoresObjetos.size(); i++) {
            ContenedorObjetos contenedor = contendoresObjetos.get(i);
            int puntoX = contenedor.getPosicion().x - ElementosPrincipales.jugador.getPosicionXINT() + Constantes.MARGEN_X;
            int puntoY = contenedor.getPosicion().y - ElementosPrincipales.jugador.getPosicionYINT() + Constantes.MARGEN_Y;

            if (puntoX < -Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                    || puntoY < -Constantes.LADO_SPRITE || puntoY > Constantes.ALTO_JUEGO - 55) {
                continue;
            }
            contenedor.dibujar(g, puntoX + 8, puntoY + 8);
        }

        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigo = enemigos.get(i);
            int puntoX = (int) enemigo.getPosicionX() - ElementosPrincipales.jugador.getPosicionXINT() + Constantes.MARGEN_X;
            int puntoY = (int) enemigo.getPosicionY() - ElementosPrincipales.jugador.getPosicionYINT() + Constantes.MARGEN_Y;

            if (puntoX < -Constantes.LADO_SPRITE || puntoX > Constantes.ANCHO_JUEGO
                    || puntoY < -Constantes.LADO_SPRITE || puntoY > Constantes.ALTO_JUEGO - 55) {
                continue;
            }
            enemigo.dibujar(g, puntoX, puntoY);
        }
        DibujoOpciones.dibujarImagen(g, pausaActual, 2, 2);
    }

    /**
     * Generar de manera aleatoria los guardianes(enemigos) en el mapa
     */
    private void getGuardianesMapa() {
        Random num = new Random();
        int xEnemigo = num.nextInt(2208) + 1;
        int yEnemigo = num.nextInt(2208) + 1;
        Point posicionEnemigo = new Point(xEnemigo, yEnemigo);
        Rectangle r = new Rectangle(xEnemigo, yEnemigo, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
        for (Rectangle rectangle : areasColisionLectura) {
            if (!(r.intersects(rectangle))) {
                Enemigo enemigo = RegistroEnemigos.getEnemigo(1);
                enemigo.setPosicion(posicionEnemigo.x, posicionEnemigo.y);
                enemigos.add(enemigo);
                return;
            }
        }
    }

    /**
     * Ubica los enemigos leidos en el mapa
     *
     * @param coleccionEnemigos (enemigos)
     */
    private void getEnemigosMapa(JSONArray coleccionEnemigos) {
        for (int i = 0; i < coleccionEnemigos.size(); i++) {
            JSONObject datosEnemigo = getObjetoJSON(coleccionEnemigos.get(i).toString());
            int idEnemigo = getIntDelJSON(datosEnemigo, "id");
            int xEnemigo = getIntDelJSON(datosEnemigo, "x");
            int yEnemigo = getIntDelJSON(datosEnemigo, "y");
            Point posicionEnemigo = new Point(xEnemigo, yEnemigo);
            Enemigo enemigo = RegistroEnemigos.getEnemigo(idEnemigo);
            enemigo.setPosicion(posicionEnemigo.x, posicionEnemigo.y);
            enemigos.add(enemigo);
        }
    }

    /**
     * Ubica los objetos en el mapa
     *
     * @param coleccionObjetos (cofres)
     */
    private void getObjetosMapa(JSONArray coleccionObjetos) {
        for (int i = 0; i < coleccionObjetos.size(); i++) {
            JSONObject datosObjeto = getObjetoJSON(coleccionObjetos.get(i).toString());
            int xObjeto = getIntDelJSON(datosObjeto, "x");
            int yObjeto = getIntDelJSON(datosObjeto, "y");
            int[] idObjetos = new int[2];
            int[] cantidadObjetos = new int[2];
            for (int j = 1; j <= 2; j++) {
                idObjetos[j - 1] = getIntDelJSON(datosObjeto, ("id" + j));
                cantidadObjetos[j - 1] = getIntDelJSON(datosObjeto, ("cantidad" + j));
            }
            ContenedorObjetos contenedor = new ContenedorObjetos(new Point(xObjeto, yObjeto), idObjetos, cantidadObjetos);
            contendoresObjetos.add(contenedor);
        }
    }

    /**
     * Ubica los sprites en el mapa
     *
     * @param coleccionSprites
     */
    private void asignarSprites(JSONArray coleccionSprites) {
        for (int i = 0; i < coleccionSprites.size(); i++) {
            JSONObject datosGrupo = getObjetoJSON(coleccionSprites.get(i).toString());
            String nombreImagen = datosGrupo.get("source").toString();
            HojaSprites hoja = new HojaSprites("/imagenes/hojas_de_texturas/" + nombreImagen, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE, false);

            int primerSprite = getIntDelJSON(datosGrupo, "firstgid") - 1;
            int ultimoSprite = primerSprite + getIntDelJSON(datosGrupo, "tilecount") - 1;

            for (int j = 0; j < capaSprites.size(); j++) {
                CapaSprites capaActual = capaSprites.get(j);
                int[] spritesCapa = capaActual.getSprites();

                for (int k = 0; k < spritesCapa.length; k++) {
                    int idSpriteActual = spritesCapa[k];
                    if (idSpriteActual >= primerSprite && idSpriteActual <= ultimoSprite) {
                        if (paletaSprite[idSpriteActual] == null) {
                            paletaSprite[idSpriteActual] = hoja.getSprite(idSpriteActual - primerSprite);
                        }
                    }
                }
            }
        }
    }

    /**
     * Actualizar las areas del colision con respecto a la posicion del jugador
     */
    private void establecerAreasColision() {
        for (int i = 0; i < capaColisiones.size(); i++) {
            Rectangle[] rectangulos = capaColisiones.get(i).getColisionables();
            for (int j = 0; j < rectangulos.length; j++) {
                areasColisionLectura.add(rectangulos[j]);
            }
        }
    }

    /**
     * Inicia las capas leidas
     *
     * @param capas
     */
    private void iniciarCapas(JSONArray capas) {
        for (int i = 0; i < capas.size(); i++) {
            JSONObject datosCapa = getObjetoJSON(capas.get(i).toString());
            int anchoCapa = getIntDelJSON(datosCapa, "width");
            int altoCapa = getIntDelJSON(datosCapa, "height");
            int xCapa = getIntDelJSON(datosCapa, "x");
            int yCapa = getIntDelJSON(datosCapa, "y");
            String tipo = datosCapa.get("type").toString();

            switch (tipo) {
                case "tilelayer":
                    JSONArray sprites = getArrayJSON(datosCapa.get("data").toString());
                    int[] spritesCapa = new int[sprites.size()];
                    for (int j = 0; j < sprites.size(); j++) {
                        int codigoSprite = Integer.parseInt(sprites.get(j).toString());
                        spritesCapa[j] = codigoSprite - 1;
                    }
                    this.capaSprites.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spritesCapa));
                    break;
                case "objectgroup":
                    JSONArray rectangulos = getArrayJSON(datosCapa.get("objects").toString());
                    Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];
                    for (int j = 0; j < rectangulos.size(); j++) {
                        JSONObject datosRectangulos = getObjetoJSON(rectangulos.get(j).toString());
                        int x = getIntDelJSON(datosRectangulos, "x");
                        int y = getIntDelJSON(datosRectangulos, "y");
                        int ancho = getIntDelJSON(datosRectangulos, "width");
                        int alto = getIntDelJSON(datosRectangulos, "height");

                        if (x == 0) {
                            x = 1;
                        }
                        if (y == 0) {
                            y = 1;
                        }
                        if (ancho == 0) {
                            ancho = 1;
                        }
                        if (alto == 0) {
                            alto = 1;
                        }

                        Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
                        rectangulosCapa[j] = rectangulo;
                    }
                    this.capaColisiones.add(new CapaColisiones(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
                    break;
            }
        }
    }

    /**
     * Lee los objetos del archivo json
     *
     * @param codigoJSON (codigo objetos)
     * @return objeto
     */
    private JSONObject getObjetoJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONObject objetoJSON = null;
        try {
            Object recuperado = lector.parse(codigoJSON);
            objetoJSON = (JSONObject) recuperado;
        } catch (ParseException ex) {
            System.out.println("Posición: " + ex.getPosition());
            System.out.println(ex.getMessage());
        }

        return objetoJSON;
    }

    /**
     * Lee los array del archivo json
     *
     * @param codigoJSON (codigo arrays)
     * @return array
     */
    private JSONArray getArrayJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONArray arrayJSON = null;
        try {
            Object recuperado = lector.parse(codigoJSON);
            arrayJSON = (JSONArray) recuperado;
        } catch (ParseException ex) {
            System.out.println("Posición: " + ex.getPosition());
            System.out.println(ex.getMessage());
        }

        return arrayJSON;
    }

    private int getIntDelJSON(final JSONObject objetoJSON, final String clave) {
        return (int) Double.parseDouble(objetoJSON.get(clave).toString());
    }

    public Point getCoordenadaInicial() {
        return coordenadaInicial;
    }

    public ArrayList<Rectangle> getAreasColisionJugador() {
        return areasColisionActualizado;
    }

    /**
     * Obtiene los bordes o limites del mapa
     *
     * @param posicionX (posicion del jugador x)
     * @param posicionY (posicion del jugador y)
     * @return rectangulo del borde
     */
    public Rectangle getBordes(final int posicionX, final int posicionY) {

        int x = Constantes.MARGEN_X - posicionX + ElementosPrincipales.jugador.getAnchoJugador();
        int y = Constantes.MARGEN_Y - posicionY + ElementosPrincipales.jugador.getAltoJugador();

        int anchoRectangulo = this.anchoMapaTiled * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getAnchoJugador() * 2;
        int altoRectangulo = this.altoMapaTiled * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.getAltoJugador() * 2;

        return new Rectangle(x, y, anchoRectangulo, altoRectangulo);
    }

    public void setDificultad(final int dificultad) {
        this.dificultad = dificultad;
    }

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }

}
