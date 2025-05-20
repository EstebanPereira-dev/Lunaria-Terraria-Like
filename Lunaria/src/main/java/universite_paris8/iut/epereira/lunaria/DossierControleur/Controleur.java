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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
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

    private final Map<Acteur, ImageView> sprites = new HashMap<>();

    private Image[] heroFrames;
    private Image[] ennemiFrames;

    private Environement env;
    private GestionnaireMap gestionMap;

    private Timeline gameLoop;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);

        tempItemSouris = null;

        prechargerImages();

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
    private void prechargerImages() {
        // Préchargement des images du héros (2 frames comme dans vos images)
        heroFrames = new Image[2];
        heroFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idle1.png"));
        heroFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idle2.png"));

        // Préchargement des images d'ennemi (peut être changé selon vos sprites ennemis)
        ennemiFrames = new Image[2];
        ennemiFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Adepte/idle1.png"));
        ennemiFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Adepte/idle1.png"));
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

        List<Acteur> acteursASupprimer = env.getActeursASupprimer();
        for (Acteur acteur : acteursASupprimer) {
            supprimerActeurVue(acteur);
        }

        env.supprimerActeursMarques();
        for (Acteur a : acteursCopie) {
            a.deplacement();
            if (a instanceof Ennemi)
                attaqueEnnemis();
        }


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
    public void attaqueEnnemis() {
        for (Acteur acteur : env.getActeurs()) {
            if (acteur instanceof Ennemi && !acteur.attackOnCooldown) {
                acteur.attaque();
                acteur.attackOnCooldown = true;

                final Acteur finalActeur = acteur;
                Timeline attackCooldownTimerEnnemi = new Timeline(
                        new KeyFrame(Duration.seconds(5), e -> {
                            if (!env.getActeursASupprimer().contains(finalActeur)) {
                                finalActeur.attackOnCooldown = false;
                            }
                        })
                );
                attackCooldownTimerEnnemi.setCycleCount(1);
                attackCooldownTimerEnnemi.play();
            }
        }
    }

    public ImageView creerSprite(Acteur a) {
        ImageView imageView = new ImageView();

        // Dimensions du sprite basées sur les images que vous avez partagées
        // Ajustez ces valeurs en fonction de la taille réelle de vos sprites
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);

        // Animation différente selon le type d'acteur
        if (a instanceof Hero) {
            // Définir l'image initiale
            imageView.setImage(heroFrames[0]);
            imageView.setId("Hero");

            // Animation simple alternant entre les deux frames
            final int[] frameIndex = {0};
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.millis(300), e -> {
                        frameIndex[0] = (frameIndex[0] + 1) % heroFrames.length;
                        imageView.setImage(heroFrames[frameIndex[0]]);
                    })
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        } else {
            // Pour les ennemis
            imageView.setImage(ennemiFrames[0]);
            imageView.setId("Ennemis");

            // Animation simple alternant entre les deux frames
            final int[] frameIndex = {0};
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.millis(300), e -> {
                        frameIndex[0] = (frameIndex[0] + 1) % ennemiFrames.length;
                        imageView.setImage(ennemiFrames[frameIndex[0]]);
                    })
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        }

        // Lier les propriétés de position
        // Centrer l'image sur les coordonnées de l'acteur
        imageView.translateXProperty().bind(a.x.subtract(imageView.getFitWidth() / 2));
        imageView.translateYProperty().bind(a.y.subtract(imageView.getFitHeight() / 2));

        return imageView;
    }

    // Démarrage
    public void demarrer() {
        gameLoop.play();
    }

    // Pause
    public void arreter() {
        gameLoop.stop();
    }

    public void supprimerActeurVue(Acteur acteur) {
        ImageView sprite = sprites.get(acteur);
        if (sprite != null) {
            tabJeu.getChildren().remove(sprite);
            sprites.remove(acteur);
        }
    }

    // Modifiez la méthode ajouterActeurVue
    public void ajouterActeurVue(Acteur acteur) {
        ImageView sprite = creerSprite(acteur);
        sprites.put(acteur, sprite);
        tabJeu.getChildren().add(sprite);
    }

}