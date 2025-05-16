package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;

public class GestionnaireMap {
    private Terrain tableau;
    private TilePane panneauDeTuile;
    private Image image;
    private Environement env;
    ImageView vue;

    public GestionnaireMap(TilePane panneauDeTuile, Environement env) {
        this.env = env;
        tableau = env.getTerrain();
        this.panneauDeTuile = panneauDeTuile;
        this.image = null;
        this.vue = null;
    }

    public void chargerTiles(Terrain terrain) {

        panneauDeTuile.getChildren().clear();

        int[][] mapData = terrain.getTableau();

        System.out.println("Dimensions du terrain: " + terrain.getWidth() + "x" + terrain.getHeight());

        for (int i = 0; i < terrain.getHeight(); i++) {
            for (int j = 0; j < terrain.getWidth(); j++) {
                    int tile = mapData[i][j];
                    Image sprite;

                    if (tile == 0)
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
                    else if (tile ==1)
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
                    else if (tile ==2)
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
                    else if (tile ==3)
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
                    else if (tile ==4)
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
                    else
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));

                    ImageView imageView = new ImageView(sprite);
                    imageView.setFitHeight(32);
                    imageView.setFitWidth(32);

                    panneauDeTuile.getChildren().add(imageView);

            }
        }
    }

    public Terrain getTableau() {
        return tableau;
    }
}