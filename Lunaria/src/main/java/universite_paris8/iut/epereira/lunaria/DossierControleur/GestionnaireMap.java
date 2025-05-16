package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;

public class GestionnaireMap {
    private Terrain tableau;
    private TilePane panneauDeTuile;
    private Environement env;

    // Préchargement des images (une seule fois)
    private Image imageVide;
    private Image imageTerre;
    private Image imageHerbe;
    private Image imageBuisson;
    private Image imageMur;
    private Image imageBois;

    public GestionnaireMap(TilePane panneauDeTuile, Environement env) {
        this.env = env;
        tableau = env.getTerrain();
        this.panneauDeTuile = panneauDeTuile;

        // Chargement des images une seule fois
        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
    }

    public void chargerTiles(Terrain terrain) {
        panneauDeTuile.getChildren().clear();

        int[][] mapData = terrain.getTableau();

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
                imageView.setFitHeight(env.getTAILLE_TUILE());
                imageView.setFitWidth(env.getTAILLE_TUILE());

                panneauDeTuile.getChildren().add(imageView);
            }
        }
    }

    public Terrain getTableau() {
        return tableau;
    }
}