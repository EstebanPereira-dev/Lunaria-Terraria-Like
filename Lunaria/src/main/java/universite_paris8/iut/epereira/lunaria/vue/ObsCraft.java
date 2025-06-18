package universite_paris8.iut.epereira.lunaria.vue;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Craft.Craft;
import universite_paris8.iut.epereira.lunaria.modele.Item;



public class ObsCraft implements ListChangeListener<Craft> {
    private ScrollPane scrollPane;
    private LoadImage librairiImage;

    public ObsCraft(ScrollPane scrollPane){
        this.scrollPane = scrollPane;
        librairiImage = new LoadImage();
    }




    @Override
    public void onChanged(Change<? extends Craft> change) {
        while(change.next()){

        }
    }
}
