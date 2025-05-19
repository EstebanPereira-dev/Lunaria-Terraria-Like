package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GestionnaireJeu {

    private Item tempItemSouris;
    private TextArea pauseID;
    private final Pane zoneJeu;
    private GridPane inventaireGridPane;
    private Environement env;

    private final Map<Acteur, Circle> sprites = new HashMap<>();

    private Timeline gameLoop;

    static Random rdm = new Random();
    public GestionnaireJeu(Pane zoneJeu, TextArea pauseID, GridPane inventaireGridPane, Environement env) {
        this.zoneJeu = zoneJeu;
        this.pauseID = pauseID;
        this.inventaireGridPane = inventaireGridPane;

        tempItemSouris = null;

        this.env = env;

        ajouterActeurVue(env.getHero());
        for (Acteur a : env.getActeurs()) {
            ajouterActeurVue(a);
        }


        configurerEvenements();
        creerBoucleDeJeu();

    }

    private void configurerEvenements() {
        zoneJeu.setFocusTraversable(true);
        zoneJeu.setOnKeyPressed(this::gererTouchePressee);
        zoneJeu.setOnKeyReleased(this::gererToucheRelachee);
        zoneJeu.setOnMouseClicked(this::gereInventaire);

    }

    private void gereInventaire(MouseEvent event){
        inventaireGridPane.getChildren().get(0).setOnMouseClicked(mouseEvent -> System.out.println("cc"));
        inventaireGridPane.getChildren().get(0).setStyle("-fx-background-color: Red");
    }

    private void gererTouchePressee(KeyEvent event) {

        switch (event.getCode()) {
            case SPACE, Z:
                env.getHero().getActions().set(0, true);
                break;
            case Q:
                env.getHero().getActions().set(3, true);
                break;
            case D:
                env.getHero().getActions().set(2, true);
                break;
            case ESCAPE:
                env.getHero().getActions().set(5, !env.getHero().getActions().get(5));
                if (env.getHero().getActions().get(5)) {
                    pauseID.setVisible(true);
                    arreter();
                } else {
                    demarrer();
                    pauseID.setVisible(false);
                }
                break;
            case I:
                if(env.getHero().getActions().get(5)){

                }
                else {
                    env.getHero().getActions().set(4, !env.getHero().getActions().get(4));
                    if (env.getHero().getActions().get(4)) {
                        inventaireGridPane.setVisible(true);
                        inventaireGridPane.setDisable(true);
                    } else {
                        inventaireGridPane.setVisible(false);
                        inventaireGridPane.setDisable(true);
                    }
                }
                break;
        }
    }

    private void gererToucheRelachee(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE, Z:
                env.getHero().getActions().set(0, false);
                break;
            case Q:
                env.getHero().getActions().set(3, false);
                break;
            case D:
                env.getHero().getActions().set(2, false);
                break;
        }
    }

    private void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(15), e -> miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    private void miseAJourJeu() {
        for (Acteur a : env.getActeurs()){
            a.deplacement();
        }
    }

    public Circle creerSprite(Acteur a) {
        Circle r = new Circle(8);
        if (a instanceof Hero) {
            r.setFill(javafx.scene.paint.Color.LIGHTGOLDENRODYELLOW);
            r.setId("Hero");
        } else {

            r.setFill(javafx.scene.paint.Color.RED);
            r.setId("Ennemis");
        }
        r.translateXProperty().bind(a.x);
        r.translateYProperty().bind(a.y);
        return r;
    }

    // Démarrage
    public void demarrer() {
        gameLoop.play();
    }

    // Pause
    public void arreter() {
        gameLoop.stop();
    }
    public void ajouterActeurVue(Acteur acteur) {
        Circle sprite = creerSprite(acteur);
        sprites.put(acteur, sprite);
        zoneJeu.getChildren().add(sprite);
    }

}