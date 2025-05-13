package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
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
    private Pane tabJeu;
    private Environement env;
    private final Map<Acteur, Circle> sprites = new HashMap<>();
    private Acteur H;
    private Terrain terrain;
    private double vitesseY = 0;
    private final double GRAVITE = 0.2;
    private final double SAUT = -5;



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
                tabJeu.setOnKeyPressed(keyEvent -> {
                    switch (keyEvent.getCode()) {
                        case SPACE:
                            double limiteSol = tabJeu.getPrefHeight() * 0.9;
                            if (H.y.get() >= limiteSol - 11) {
                                vitesseY = SAUT;
                                H.y.set(H.y.get() + vitesseY);
                            }
                            break;

                        case Q:
                            H.x.set(H.x.get() - H.getV());
                            break;
                        case D:
                            H.x.set(H.x.get() + H.getV());
                            break;
                    }
                });

        Timeline graviteTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), e -> {
                    double limiteSol = tabJeu.getPrefHeight() * 0.9;

                    double posY = H.y.get(); // position actuelle du héros

                    if (posY + 10 < limiteSol) {
                        vitesseY += GRAVITE;
                        H.y.set(posY + vitesseY);
                    } else {
                        H.y.set(limiteSol - 10);
                        vitesseY = 0;
                    }
                })
        );
        graviteTimeline.setCycleCount(Animation.INDEFINITE); // PARAMETRE LA TIMELINE SUR L'INFINI
        graviteTimeline.play(); // LANCE LA TIMELINE


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
        r.setLayoutY(a.getYProperty().get());
        a.getYProperty().addListener((obs, oldVal, newVal) -> {
            r.setLayoutY(newVal.doubleValue());
        });

        r.layoutXProperty().bind(a.getXProperty());
        return r;
    }

}
