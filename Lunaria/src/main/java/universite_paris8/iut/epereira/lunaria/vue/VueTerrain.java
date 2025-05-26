package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class VueTerrain {
    private Terrain tableau;
    private TilePane panneauDeTuile;
    private Environement env;
    private  ImageView[][] vueTuiles;

    private Image imageVide;
    private Image imageTerre;
    private Image imageHerbe;
    private Image imageBuisson;
    private Image imageMur;
    private Image imageBois;

    public VueTerrain(TilePane panneauDeTuile, Environement env) {
        this.env = env;
        tableau = env.getTerrain();
        this.panneauDeTuile = panneauDeTuile;

        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
        this.vueTuiles= new ImageView[this.env.getWidth()][this.env.getHeight()];
    }

    public void chargerTiles(Terrain terrain) {
        panneauDeTuile.getChildren().clear();
        int[][] mapData = terrain.getTableau();

        panneauDeTuile.getChildren().setAll(genererTuilesDepuisMap(mapData, ConfigurationJeu.TAILLE_TUILE));

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

                panneauDeTuile.getChildren().add(imageView);
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

    // Ancienne facon de rafraichir (avant observable liste) ==> inutile mais on sait jamais
  /*  public void RafraichirTuile(int x, int y){
        int tuile= this.tableau.getTableau()[y][x];
        Image sprite = getImageTuile(tuile);
        vueTuiles[y][x].setImage(sprite);
        chargerTiles(this.tableau);
    }*/

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