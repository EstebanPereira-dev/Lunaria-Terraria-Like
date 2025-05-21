package universite_paris8.iut.epereira.lunaria.modele;

import java.util.Random;

public class ConfigurationJeu {

    static public Random rdm = new Random();

    public static final int TAILLE_TUILE = 16;

    public static final int WIDTH_TILES = 80;
    public static final int HEIGHT_TILES = 50;

    public static final int WIDTH_SCREEN = WIDTH_TILES * TAILLE_TUILE;
    public static final int HEIGHT_SCREEN = HEIGHT_TILES * TAILLE_TUILE;

}