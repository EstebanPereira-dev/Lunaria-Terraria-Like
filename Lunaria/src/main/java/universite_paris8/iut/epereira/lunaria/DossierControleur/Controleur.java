package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.application.Platform;
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
        Platform.runLater(() -> {
            tabJeu.requestFocus();
        });
        tabJeu.requestFocus();
        tabJeu.setOnKeyPressed(keyEvent -> {
            switch(keyEvent.getCode()) {
                case SPACE:
                    H.y.set(H.y.get() - H.getV());
                    System.out.println("1");
                    break;
                case Q:
                    H.x.set(H.x.get() - H.getV());
                    System.out.println("2");
                    break;
                case S:
                    H.y.set(H.y.get() + H.getV());
                    System.out.println("3");
                    break;
                case D:
                    H.x.set(H.x.get() + H.getV());
                    System.out.println("4");
                    break;
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
