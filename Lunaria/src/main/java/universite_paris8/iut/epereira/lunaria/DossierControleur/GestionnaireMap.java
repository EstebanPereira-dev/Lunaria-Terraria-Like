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

    public GestionnaireMap(TilePane panneauDeTuile, Terrain carte) {
        this.carte = carte;
        this.panneauDeTuile = panneauDeTuile;
        this.image = null;
        this.vue = null;
    }

    public void chargerTiles(Terrain terrain) {

        if (panneauDeTuile == null || terrain == null || terrain.getTerrain() == null) {
            System.err.println("Erreur: panneauDeTuile ou terrain est null");
            return;
        }

        panneauDeTuile.getChildren().clear();

        int[][] mapData = terrain.getTerrain();

        System.out.println("Dimensions du terrain: " + terrain.getWidth() + "x" + terrain.getHeight());

        for (int i = 0; i < terrain.getHeight(); i++) {
            for (int j = 0; j < terrain.getWidth(); j++) {
                try {
                    int tile = mapData[i][j];
                    Image sprite;

                    // Vérifiez les ressources et utilisez le bon chemin
                    if (tile == 0)
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Ciel.png"));
                     else
                        sprite = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));


                    ImageView imageView = new ImageView(sprite);
                    imageView.setFitHeight(12);
                    imageView.setFitWidth(19);

                    panneauDeTuile.getChildren().add(imageView);

                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de la tuile [" + i + "," + j + "]: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public Terrain getCarte() {
        return carte;
    }
}