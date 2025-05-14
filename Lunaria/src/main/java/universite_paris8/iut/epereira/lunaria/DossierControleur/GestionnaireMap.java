package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;


public class GestionnaireMap {
    private Terrain carte;
    private TilePane panneauDeTuile;
    private Image image;
    ImageView vue;

    public GestionnaireMap(TilePane panneauDeTuile){
        this.carte.setTerrain(new int[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1}
        });
        this.image=null;
        this.vue=null;
    }

    public void chargerTiles(Terrain terrain){
        for (int i =0; i<terrain.getWidth();i++){
            for (int j=0;j<terrain.getHeight();j++){
                int tile = carte.getTerrain()[i][j];
                Image sprite;
                if (tile==0){
                    sprite= new Image (getClass().getResourceAsStream("/src/main/resources/universite_paris8/iut/epereira/lunaria/DossierMap/Blue.png"));
                }
                else {
                    sprite =new Image(getClass().getResourceAsStream("/src/main/resources/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
                }
                ImageView imageView= new ImageView(sprite);
                imageView.setFitHeight(7);
                imageView.setFitWidth(7);
                panneauDeTuile.getChildren().add(imageView);
            }
        }
    }

    public Terrain getCarte() {
        return carte;
    }
}
