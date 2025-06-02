package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import javax.sound.sampled.Control;


public class VueTerrain {
    private Terrain tableau;
    private Environement env;
    private Controleur controleur;

    private Image imageVide;
    private Image imageTerre;
    private Image imageHerbe;
    private Image imageBuisson;
    private Image imageMur;
    private Image imageBois;

    public VueTerrain(Environement env, Controleur controleur) {
        this.controleur = controleur;
        this.env = env;
        controleur.getTabJeu().setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        controleur.getTabJeu().setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);
        controleur.getTilePaneId().setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        controleur.getTilePaneId().setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        controleur.getBackground().setFitWidth(ConfigurationJeu.WIDTH_SCREEN);
        controleur.getBackground().setFitHeight(ConfigurationJeu.HEIGHT_SCREEN);

        tableau = env.getTerrain();

        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
    }

    public void chargerTiles(Terrain terrain) {
        controleur.getTilePaneId().getChildren().clear();
        int[][] mapData = terrain.getTableau();

        controleur.getTilePaneId().getChildren().setAll(genererTuilesDepuisMap(mapData, ConfigurationJeu.TAILLE_TUILE));

        System.out.println("Dimensions du terrain: " + terrain.getWidth() + "x" + terrain.getHeight());

        for (int i = 0; i < terrain.getHeight(); i++) {
            for (int j = 0; j < terrain.getWidth(); j++) {
                int tile = mapData[i][j];
                Image sprite;

                // Utilisation des images préchargées
                switch (tile) {
                    case 0:
                        sprite = imageVide;
                        break;
                    case 1:
                        sprite = imageTerre;
                        break;
                    case 2:
                        sprite = imageHerbe;
                        break;
                    case 3:
                        sprite = imageBuisson;
                        break;
                    case 4:
                        sprite = imageMur;
                        break;
                    default:
                        sprite = imageBois;
                        break;
                }

                ImageView imageView = new ImageView(sprite);
                imageView.setFitHeight(ConfigurationJeu.TAILLE_TUILE);
                imageView.setFitWidth(ConfigurationJeu.TAILLE_TUILE);

                controleur.getTilePaneId().getChildren().add(imageView);
            }
        }
    }

    private Image getImageTuile(int typeTuile){
        if (typeTuile==0)
            return imageVide;
        else if (typeTuile==1)
            return imageTerre;
        else if (typeTuile ==2)
            return imageHerbe;
        else if (typeTuile==3)
            return imageBuisson;
        else if (typeTuile ==4)
            return imageMur;
        else
            return imageBois;
    }

    public Terrain getTableau() {
        return tableau;
    }


    public ObservableList<Node> genererTuilesDepuisMap(int[][] map, int tileSize) {
        ObservableList<Node> liste = FXCollections.observableArrayList();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                int type = map[y][x];
                ImageView imageView = new ImageView(getImageTuile(type));
                imageView.setFitWidth(tileSize);
                imageView.setFitHeight(tileSize);
                liste.add(imageView);
            }
        }

        return liste;
    }


}