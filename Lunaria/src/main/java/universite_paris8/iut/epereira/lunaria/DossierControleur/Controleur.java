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
    private TilePane TilePaneID;
    @FXML
    private Pane tabJeu;
    private Environement env;
    private final Map<Acteur, Circle> sprites = new HashMap<>();
    private Acteur H;
    private Terrain terrain;

    private double vitesseY = 0;
    private final double GRAVITE = 0.2;
    private final double SAUT = -10;

    private boolean toucheGauche = false;
    private boolean toucheDroite = false;
    private boolean toucheEspace = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.env = new Environement(800, 600);
        this.H = new Hero(env);
        Circle h = creerSprite(H);
        sprites.put(H, h);
        tabJeu.getChildren().add(h);
        env.ajouter(H);


        Platform.runLater(() -> {
            tabJeu.requestFocus();
        });
        // EVENT TOUCHES PRESSE
        tabJeu.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE:
                    toucheEspace = true;
                    break;
                case Q:
                    toucheGauche = true;
                    break;
                case D:
                    toucheDroite = true;
                    break;
            }
        });

        // EVENT TOUCHE RELACHE
        tabJeu.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE:
                    toucheEspace = false;
                    break;
                case Q:
                    toucheGauche = false;
                    break;
                case D:
                    toucheDroite = false;
                    break;
            }
        });

        Timeline gameLoop = new Timeline(
                new KeyFrame(Duration.millis(16), e -> {
                    double limiteSol = tabJeu.getPrefHeight() * 0.9;
                    double posY = H.y.get();
                    boolean auSol = (posY + 10 >= limiteSol - 1);

                    // Saute si espace est pressé et au sol
                    if (toucheEspace && auSol) {
                        System.out.println("SAUT !");
                        vitesseY = SAUT;
                    }

                    // Application de la gravité
                    if (!auSol) {
                        vitesseY += GRAVITE;
                    } else if (vitesseY > 0) {
                        vitesseY = 0;
                        H.y.set(limiteSol - 10);
                    }

                    H.y.set(H.y.get() + vitesseY);

                    if (H.y.get() > limiteSol - 10) {
                        H.y.set(limiteSol - 10);
                        vitesseY = 0;
                    }


                    if (toucheGauche) {
                        H.x.set(H.x.get() - H.getV());
                    }
                    if (toucheDroite) {
                        H.x.set(H.x.get() + H.getV());
                    }
                })
        );
        gameLoop.setCycleCount(Animation.INDEFINITE); // PARAMETRE LA TIMELINE SUR L'INFINI
        gameLoop.play(); // LANCE LA TIMELINE

    }


       /* this.terrain = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1}
        }*/

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
        r.layoutYProperty().bind(a.getYProperty());
        r.layoutXProperty().bind(a.getXProperty());
        return r;
    }

}
