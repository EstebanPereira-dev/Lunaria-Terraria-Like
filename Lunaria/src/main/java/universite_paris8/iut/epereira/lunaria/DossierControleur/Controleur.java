package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

import java.awt.event.TextEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;






public class Controleur implements Initializable {
    @FXML
    private TilePane TilePaneID;
    @FXML
    private Pane tabJeu;
    private Environement env;
    private final Map<Acteur, Circle> sprites = new HashMap<>();
    private Acteur H;
    private Terrain terrain;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.env = new Environement(800, 600);
        this.H = new Hero(env);
        Circle h = creerSprite(H);
        sprites.put(H, h);
        tabJeu.getChildren().add(h);
        env.ajouter(H);
        tabJeu.setFocusTraversable(true);
        tabJeu.requestFocus();
        tabJeu.setOnKeyPressed(keyEvent -> {
            if (H instanceof Hero) {
                if (keyEvent.getText().equalsIgnoreCase("z")) {
                    H.getYProperty().set(H.getYProperty().get() - H.getV());
                } else if (keyEvent.getText().equalsIgnoreCase("q")) {
                    H.getXProperty().set(H.getXProperty().get() - H.getV());
                } else if (keyEvent.getText().equalsIgnoreCase("s")) {
                    H.getYProperty().set(H.getYProperty().get() + H.getV());
                } else if (keyEvent.getText().equalsIgnoreCase("d")) {
                    H.getXProperty().set(H.getXProperty().get() + H.getV());
                }
            }

        });
       /* this.terrain = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1}
        }*/
    }

    public Circle creerSprite(Acteur a){
        Circle r=new Circle(10);
        if (a instanceof Hero) {
            r.setFill(Color.LIGHTGOLDENRODYELLOW);
            r.setId("Hero");
        }
        else{
            r.setFill(Color.RED);
            r.setId("Ennemis");
        }
        r.layoutXProperty().bind(a.getXProperty());
        r.layoutYProperty().bind(a.getYProperty());
        return r;
    }

}
