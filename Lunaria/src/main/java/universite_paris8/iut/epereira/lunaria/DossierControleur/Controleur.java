package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.net.URL;
import java.util.*;

public class Controleur implements Initializable {
    @FXML
    private GridPane inventaireGridPane;
    @FXML
    private TilePane tilePaneId;
    @FXML
    private TextArea pauseID;
    @FXML
    private Pane tabJeu;
    @FXML
    private ImageView background;

    private Item tempItemSouris;

    private final Map<Acteur, Circle> sprites = new HashMap<>();

    private Environement env;
    private GestionnaireMap gestionMap;

    private Timeline gameLoop;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);

        tempItemSouris = null;


        tabJeu.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tabJeu.setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);

        tilePaneId.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tilePaneId.setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        background.setFitWidth(ConfigurationJeu.WIDTH_SCREEN);
        background.setFitHeight(ConfigurationJeu.HEIGHT_SCREEN);

        gestionMap = new GestionnaireMap(tilePaneId, env);

        ajouterActeurVue(env.getHero());
        for (Acteur a : env.getActeurs()) {
            ajouterActeurVue(a);
        }


        configurerEvenements();
        creerBoucleDeJeu();
        Platform.runLater(() -> {
            gestionMap.chargerTiles(env.getTerrain());
        });

        Platform.runLater(() -> {
            tabJeu.requestFocus();
            demarrer();
        });
    }

    private void configurerEvenements() {
        tabJeu.setOnKeyPressed(this::gererTouchePressee);
        tabJeu.setOnKeyReleased(this::gererToucheRelachee);

    }

    @FXML
    public void inv(ActionEvent event) {
        Node source = (Node) event.getSource();
        Integer row = GridPane.getRowIndex(source);
        Integer col = GridPane.getColumnIndex(source);


        Item temp = env.getHero().getInv().getIteminList(row*3+col);

        if( temp != null){
            Item switch2 = temp;
            env.getHero().getInv().addItem(row*3+col,tempItemSouris);
            tempItemSouris = switch2;
        }
        else{
            env.getHero().getInv().addItem(row*3+col,tempItemSouris);
        }

        //inventaireGridPane.getChildren().get(row*3+col).setStyle("-fx-background-image: ");

    }
    @FXML
    public void clicSouris() {
        attaqueHero();
    }

    @FXML
    public void plusClicSouris(){
        //env.getHero().getActions().set(6, false);
        // METTEZ CE QUE VOUS VOULEZ ICI
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
                if (env.getHero().getActions().get(5)) {

                } else {
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
        List<Acteur> acteursCopie = new ArrayList<>(env.getActeurs());

        for (Acteur a : acteursCopie) {
            a.deplacement();
            attaqueEnnemis();
        }

        List<Acteur> acteursASupprimer = env.getActeursASupprimer();
        for (Acteur acteur : acteursASupprimer) {
            supprimerActeurVue(acteur);
        }
        env.supprimerActeursMarques();
    }
    public void attaqueHero(){
        if (!env.getHero().attackOnCooldown) {
            env.getHero().getActions().set(6, true);
            env.getHero().attaque();

            Platform.runLater(() -> {
                env.getHero().getActions().set(6, false);
            });

            env.getHero().attackOnCooldown = true;

            Timeline attackCooldownTimer = new Timeline(
                    new KeyFrame(Duration.seconds(3), e -> {
                        env.getHero().attackOnCooldown = false;
                        System.out.println("Attaque dispo");
                    })
            );
            attackCooldownTimer.setCycleCount(1);
            attackCooldownTimer.play();

        } else {
            System.out.println("COOLDOWN");
        }
    }
    public void attaqueEnnemis(){
        if (!env.getActeur("A1").attackOnCooldown) {
            env.getActeur("A1").attaque();
            System.out.println("attaque" + env.getHero().getPv());
            env.getActeur("A1").attackOnCooldown = true;

            Timeline attackCooldownTimerEnnemi = new Timeline(
                    new KeyFrame(Duration.seconds(5), e -> {
                        env.getActeur("A1").attackOnCooldown = false;
                    })
            );
            attackCooldownTimerEnnemi.setCycleCount(1);
            attackCooldownTimerEnnemi.play();
        }
    }
    public void supprimerActeurVue(Acteur acteur) {
        Circle sprite = sprites.get(acteur);
        if (sprite != null) {
            tabJeu.getChildren().remove(sprite);
            sprites.remove(acteur);
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
        tabJeu.getChildren().add(sprite);
    }
}