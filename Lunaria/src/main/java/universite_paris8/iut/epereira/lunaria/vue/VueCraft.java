package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Craft.Craft;
import universite_paris8.iut.epereira.lunaria.modele.Environement;


public class VueCraft {
    private TilePane tilePane;
    private Environement env;

    public VueCraft(TilePane tilePane, Environement env){
        this.tilePane = tilePane;
        this.env = env;
    }

    public void init(){
//        VBox vbox = new VBox();
//        vbox.setMaxWidth(75);
//        vbox.setStyle("-fx-border-color: red");
//        vbox.setOnMouseClicked(event -> {
//            onClickedCraft(event);
//        });
//        tilePane.getChildren().add(vbox);


    }


    private void onClickedCraft(MouseEvent event) {
        Node clicked = (Node) event.getTarget();
        HBox hbox;
        if(clicked instanceof ImageView || clicked instanceof Label){
            hbox = (HBox) clicked.getParent();
        }
        else{
            hbox = (HBox) clicked;
        }
        int id = Integer.parseInt(hbox.getId());
        if(env.getHero().getSouris() == null || env.getHero().getSouris().getId() == id  ){
            int index = 0;
            for(Craft i: env.getCraftingList()){
                if(i.getResultat().getId() == id){
                    if(env.getHero().getSouris() == null){
                        env.getHero().setSouris(i.crafting());
                        env.getHero().setQuantiteItem(1);
                    }
                    else{
                        if(env.getHero().getSouris().getStackMax() < env.getHero().getQuantiteItem() ){
                            i.craftable();
                            env.getHero().setQuantiteItem(env.getHero().getQuantiteItem()+1);
                        }
                    }
                }
            }
        }
    }
}
