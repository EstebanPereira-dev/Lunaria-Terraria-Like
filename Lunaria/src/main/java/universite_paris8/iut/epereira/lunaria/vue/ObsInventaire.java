package universite_paris8.iut.epereira.lunaria.vue;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Inventaire;
import universite_paris8.iut.epereira.lunaria.modele.Item;


public class ObsInventaire implements ListChangeListener<Item> {
    private TilePane tilePaneInv;
    private LoadImage librairiImage;

    public ObsInventaire(TilePane tilePaneInv) {
        this.tilePaneInv = tilePaneInv;
        librairiImage = new LoadImage();

    }

    /*
    quand la liste de l'inventaire change on update en prennant l'id de l item pour avoir la bonne image
    et on prend la position dans l'inventaire pour poser sur la bonne imageView
     */
    @Override
    public void onChanged(Change change) {
        while (change.next()) {

            VBox vbox = (VBox) tilePaneInv.getChildren().get(change.getFrom());
            ImageView imageView = (ImageView) vbox.getChildren().get(0);
            imageView.setImage(librairiImage.selectImage(((Item) change.getList().get(change.getFrom()))));

        }
    }

}
