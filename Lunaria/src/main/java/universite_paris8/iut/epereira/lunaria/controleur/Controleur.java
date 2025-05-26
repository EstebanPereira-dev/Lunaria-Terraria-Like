package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.vue.VueActeur;
import universite_paris8.iut.epereira.lunaria.vue.VueTerrain;

import java.net.URL;
import java.util.*;
import java.util.List;

public class Controleur implements Initializable {
    @FXML
    private GridPane inventaireGridPane;  //gridPane qui contient les emplacement d'inventaire

    @FXML
    private TilePane tilePaneId; //tilePane pour poesr nos bloc et les casser

    @FXML //Texte Pause quand on arrete le jeux
    private TextArea pauseID;

    @FXML //le Pane qui contient tout
    private Pane tabJeu;

    @FXML //Afficher le fond
    private ImageView background;


    private BarreDeVie barreDeVieHero;
    //lier acteur avec leur sprites et animation

    private Environement env;

    //Controler la map
    private VueTerrain gestionMap;
    private GestionTouches gestionTouches;
    private GestionSouris gestionSouris;
    private GestionInventaire gestionInventaire;
    private GestionBoucle gestionBoucle;
    private List<VueActeur> vuesActeurs = new ArrayList<>();

    //game loop
    private Timeline gameLoop;

    public double dernierePosX = -1;
    public double dernierePosY = -1;

    //gestionnaire de souris


    @Override //initialization
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Création de l'environement de la taille de l'écren
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);
        //régler les taille des different Pane
        tabJeu.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tabJeu.setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);
        tilePaneId.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tilePaneId.setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        background.setFitWidth(ConfigurationJeu.WIDTH_SCREEN);
        background.setFitHeight(ConfigurationJeu.HEIGHT_SCREEN);

        //initialisation du gestionaire de la map
        gestionMap = new VueTerrain(tilePaneId, env);
        gestionTouches= new GestionTouches(env,this);
        gestionSouris = new GestionSouris(env,this);
        gestionInventaire = new GestionInventaire(env,this);
        gestionBoucle = new GestionBoucle(env,this);

        barreDeVieHero = new BarreDeVie(env.getHero().getPv(), 200, 20);

        barreDeVieHero.setTranslateX(ConfigurationJeu.WIDTH_SCREEN - 230);
        barreDeVieHero.setTranslateY(30);
        tabJeu.getChildren().add(barreDeVieHero);
        //ajouter tous les acteurs dans la vue
        for (Acteur a : env.getActeurs()) {
            VueActeur vue = new VueActeur(a, this);
            vuesActeurs.add(vue);
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

    private void chargerItemDansMain() {
    }

    //pour chaque entré de touche
    private void configurerEvenements(){
        tabJeu.setOnKeyPressed(event -> gestionTouches.gererTouchePressee(event));
        tabJeu.setOnKeyReleased(event -> gestionTouches.gererToucheRelachee(event));
    }
    @FXML
    public void clicSouris(MouseEvent mouseEvent) {
        gestionSouris.clicDeSouris(mouseEvent);
    }
   @FXML
    public void inv(ActionEvent event) {
    gestionInventaire.inv(event);
   }

    //création de la boucle de jeux
    private void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(15), e -> gestionBoucle.miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    // Démarrage
    public void demarrer() {
        gameLoop.play();
    }

    // Pause
    public void arreter() {
        gameLoop.stop();
    }

    public TextArea getPauseID() {
        return pauseID;
    }

    public GestionInventaire getGestionInventaire() {
        return gestionInventaire;
    }

    public BarreDeVie getBarreDeVieHero() {
        return barreDeVieHero;
    }

    public VueTerrain getGestionMap() {
        return gestionMap;
    }

    public GridPane getInventaireGridPane() {
        return inventaireGridPane;
    }

    public Environement getEnv() {
        return env;
    }

    public VueActeur getVueActeur(Acteur acteur) {
        for (VueActeur vue : vuesActeurs) {
            if (vue.getActeur() == acteur) {
                return vue;
            }
        }
        return null;
    }

    public List<VueActeur> getVuesActeurs() {
        return vuesActeurs;
    }

    public Pane getTabJeu() {
        return tabJeu;
    }
}