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
    private GridPane inventaireGridPane;  //gridPane qui contient les emplacement d'inventaire

    @FXML
    private TilePane tilePaneId; //tilePane pour poesr nos bloc et les casser

    @FXML //pouor afficher les item dans l'inventaire
    private ImageView imageInv1,imageInv2,imageInv3,imageInv4,imageInv5,imageInv6,imageInv7,imageInv8,imageInv9;

    @FXML //Texte Pause quand on arrete le jeux
    private TextArea pauseID;

    @FXML //le Pane qui contient tout
    private Pane tabJeu;

    @FXML //Afficher le fond
    private ImageView background;

    //contient l'item dans la souris
    private Item tempItemSouris;

    //lier acteur avec leur sprites
    private final Map<Acteur, Circle> sprites = new HashMap<>();

    //Environement modele
    private Environement env;

    //Controler la map
    private GestionnaireMap gestionMap;

    //boolean popur savoir si l'inventaire est ouvert ou pas.
    private boolean inventaireBooleanOvert = true;

    //game loop
    private Timeline gameLoop;



    @Override //initialization
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Création de l'environement de la taille de l'écren
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);

        //initialiser ce que contient la souris a null
        tempItemSouris = null;

        //appelle de la methode serInvVisible
        setInvVisible(false);

        //régler les taille des different Pane
        tabJeu.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tabJeu.setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);

        tilePaneId.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tilePaneId.setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        background.setFitWidth(ConfigurationJeu.WIDTH_SCREEN);
        background.setFitHeight(ConfigurationJeu.HEIGHT_SCREEN);

        //initialisation du gestionaire de la map
        gestionMap = new GestionnaireMap(tilePaneId, env);

        //ajouter tous les acteurs dans la vue
        ajouterActeurVue(env.getHero());
        for (Acteur a : env.getActeurs()) {
            ajouterActeurVue(a);
        }

        //démarage du jeux
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


    //faire apparaitre/disparaitre l'inventaire
    private void setInvVisible(Boolean bool) {
        inventaireGridPane.setVisible(bool);
        inventaireGridPane.setDisable(!bool);
        for(int i = 0;i<9;i++){
            inventaireGridPane.getChildren().get(i).setVisible(bool);
        }
    }

    //pour chaque entré de touche
    private void configurerEvenements() {
        tabJeu.setOnKeyPressed(this::gererTouchePressee);
        tabJeu.setOnKeyReleased(this::gererToucheRelachee);
    }

    //gestionaire de l'inventaire
    @FXML
    public void inv(ActionEvent event) {
        Node source = (Node) event.getSource();
        Integer row = GridPane.getRowIndex(source);
        Integer col = GridPane.getColumnIndex(source);


        Item temp = env.getHero().getInv().getIteminList(row * 3 + col);

        //vérifie si l'emplacement sur le quel on click est occupé
        //si occupé, echangé ce que contient la souris avec le contenue de l'emplacement
        //sinon, juste placer ce que contient la souris dans l'emplacement
        if (temp != null) {
            Item switch2 = temp;
            env.getHero().getInv().addItem(row * 3 + col, tempItemSouris);
            tempItemSouris = switch2;
        } else {
            env.getHero().getInv().addItem(row * 3 + col, tempItemSouris);
        }


        //charger ce que contient la case par une image de l'item, vide si null

        //imageInv1.setImage(new Image("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png"));

        //((Node) event.getSource()).setStyle("-fx-background-image: url("+getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png")+")");
        //((Node) event.getSource()).setOpacity(1.0);

        //inventaireGridPane.getChildren().get(row * 3 + col).setStyle();


    }

    @FXML//si la souris est cliquer attaquer
    public void clicSouris() {
        attaqueHero();
    }

    @FXML //rien
    public void plusClicSouris() {
    }

    //gere quand les touche sont appuyé, faire une action
    private void gererTouchePressee(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE, Z: //sauter
                env.getHero().getActions().set(0, true);
                break;
            case Q: //aller a gauche
                env.getHero().getActions().set(3, true);
                break;
            case D: //aller a droite
                env.getHero().getActions().set(2, true);
                break;
            case ESCAPE: //mettre le jeux en pause
                env.getHero().getActions().set(5, !env.getHero().getActions().get(5));
                if (env.getHero().getActions().get(5)) {
                    pauseID.setVisible(true);
                    arreter();
                } else {
                    demarrer();
                    pauseID.setVisible(false);
                }
                break;
            case I: //ouvrire ou fermer l'inventaire
                if (env.getHero().getActions().get(5)) {

                } else {
                    env.getHero().getActions().set(4, !env.getHero().getActions().get(4));
                    if(inventaireBooleanOvert){
                        setInvVisible(true);
                        inventaireBooleanOvert = !inventaireBooleanOvert;
                    }
                    else{
                        setInvVisible(false);
                        inventaireBooleanOvert = !inventaireBooleanOvert;
                    }

                }
                break;
        }
    }
    //gere quand la touche est relacher
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

    //création de la boucle de jeux
    private void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(15), e -> miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    //gere ce qui se passe toute les tick
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

    //à commenter
    public void attaqueHero() {
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

    //à commenter
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

    //supprime tout les sprites mort
    public void supprimerActeurVue(Acteur acteur) {
        Circle sprite = sprites.get(acteur);
        if (sprite != null) {
            tabJeu.getChildren().remove(sprite);
            sprites.remove(acteur);
        }
    }

    //crée un sprite
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

    //ajoute un acteur dans la vue
    public void ajouterActeurVue(Acteur acteur) {
        Circle sprite = creerSprite(acteur);
        sprites.put(acteur, sprite);
        tabJeu.getChildren().add(sprite);
    }
}