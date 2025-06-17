package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;



public class VueCraft {
    private ScrollPane scrollPane;

    public VueCraft(ScrollPane scrollPane){
        this.scrollPane = scrollPane;
    }

    public void init(){
        VBox vbox = new VBox();
        vbox.setMaxWidth(187);
        scrollPane.setContent(vbox);
    }
}
