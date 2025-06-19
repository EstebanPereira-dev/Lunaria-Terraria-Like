package universite_paris8.iut.epereira.lunaria.vue;

import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import javafx.scene.layout.GridPane;

public class ObsTerrain implements ListChangeListener<Integer> {

    private final GridPane grilleAffichage;

    private final Image imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
    private final Image imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
    private final Image imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
    private final Image imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
    private final Image imagePierre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
    private final Image imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buche.png"));
    private final Image imagePlanche = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Planche.png"));
    private final Image imageFeuille = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Feuille.png"));
    private final int width;

    public ObsTerrain(GridPane grilleAffichage, int width) {
        this.grilleAffichage = grilleAffichage;
        this.width = width;
    }

    @Override
    public void onChanged(Change<? extends Integer> change) {
        while (change.next()) {
            for (int i = change.getFrom(); i < change.getTo(); i++) {
                updateTuile(i, change.getList().get(i));

            }
        }
    }
    public void updateTuile(int index, int type) {
        Image sprite;

        switch (type) {
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
                sprite = imagePierre;
                break;
            case 5:
                sprite = imageBois;
                break;
            case 6:
                sprite = imagePlanche;
                break;
            case 7:
                sprite = imageFeuille;
                break;
            default:
                sprite = imageVide;
                break;
        }

        ImageView imageView = new ImageView(sprite);
        imageView.setFitWidth(ConfigurationJeu.TAILLE_TUILE);
        imageView.setFitHeight(ConfigurationJeu.TAILLE_TUILE);

        int x = index % width;
        int y = index / width;

        // Supprimer l'ancienne tuile s'il y en a une
        var enfants = grilleAffichage.getChildren();

        // On parcourt tous les enfants pour trouver ceux qui sont à la position (x, y)
        for (int i = enfants.size() - 1; i >= 0; i--) { // on boucle à l’envers pour pouvoir supprimer sans problème
            var noeud = enfants.get(i);
            Integer col = GridPane.getColumnIndex(noeud);
            Integer row = GridPane.getRowIndex(noeud);

            // Vérifier que la position n’est pas nulle et correspond à (x, y)
            if (col != null && row != null && col == x && row == y) {
                // Supprimer ce nœud
                enfants.remove(i);
            }
        }

        // Ajouter la nouvelle image à la position (x, y)
        grilleAffichage.add(imageView, x, y);
    }

}

