package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
import universite_paris8.iut.epereira.lunaria.vue.*;


import java.net.URL;
import java.util.*;
import java.util.List;

public class Controleur implements Initializable {
    @FXML
    private GridPane inventaireGridPane;  //gridPane qui contient les emplacement d'inventaire
    @FXML
    private Label ecu;
    @FXML
    private TilePane tilePaneId; //tilePane pour poesr nos bloc et les casser

    @FXML //Texte Pause quand on arrete le jeux
    private TextArea pauseID;

    @FXML //le Pane qui contient tout
    private Pane tabJeu;

    @FXML
    private ImageView imageInv1,imageInv2,imageInv3,imageInv4,imageInv5,imageInv6,imageInv7,imageInv8,imageInv9;

    //listener de l'inventaire
    private ObsInventaire obsInventaire;

    @FXML //Afficher le fond
    private ImageView background;

    @FXML
    private GridPane terrainGrid;

    //Barrre de vie du hero
    private BarresStatut barreDuHero;

    //lier acteur avec leur sprites et animation
    private Environement env;

    //Controler la map
    private VueTerrain gestionMap;
    private GestionTouches gestionTouches;
    private GestionSouris gestionSouris;
    private GestionInventaire gestionInventaire;
    private GestionBoucle gestionBoucle;
    private List<VueActeur> vuesActeurs = new ArrayList<>();
    private VueHero v;

    //game loop
    private Timeline gameLoop;

    @Override //initialization
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Création de l'environement de la taille de l'écren
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);
        v = new VueHero(env.getHero(),this);

        // Récupération du terrain
        Terrain terrain = env.getTerrain();
        // Création de l'observateur avec la grille d'affichage et la largeur du terrain
        ObsTerrain obsTerrain = new ObsTerrain(terrainGrid, terrain.getWidth());
        // Lier l'observateur à la liste observable
        terrain.getTableau().addListener(obsTerrain);
        // Affichage initial des tuiles dans la grille
        for (int i = 0; i < terrain.getTableau().size(); i++) {
            obsTerrain.updateTuile(i, terrain.getTableau().get(i));
        }
        //initialisation du gestionaire de la map
        gestionMap = new VueTerrain(env, this);
        gestionTouches= new GestionTouches(env,this);
        gestionSouris = new GestionSouris(env,this);
        gestionInventaire = new GestionInventaire(env,this);
        gestionBoucle = new GestionBoucle(env,this);

        //initialisation de la barre de vie du hero
        barreDuHero = new BarresStatut(env.getHero().getPv(), 200, 200,20);
        barreDuHero.setTranslateX(ConfigurationJeu.WIDTH_SCREEN - 230);
        barreDuHero.setTranslateY(30);
        tabJeu.getChildren().add(barreDuHero);

        // Ajouter d'abord la vue héros à la liste
        vuesActeurs.add(v);

        Hero hero = env.getHero();
        for (Acteur a : env.getActeurs()) {
            if (a != hero) { // Comparaison directe par référence, pas instanceof
                VueActeur vue = VueActeurFactory.creerVue(a, this);
                vuesActeurs.add(vue);
            }
        }

        //démarage du jeux
        configurerEvenements();
        gestionBoucle.creerBoucleDeJeu();
        Platform.runLater(() -> {
            //gestionMap.chargerTiles(env.getTerrain());
        });
        Platform.runLater(() -> {
            tabJeu.requestFocus();
            gestionBoucle.demarrer();
        });

        //ajout du listener sur l'observable liste de l'inventaire
        obsInventaire = new ObsInventaire(imageInv1,imageInv2,imageInv3,imageInv4,imageInv5,imageInv6,imageInv7,imageInv8,imageInv9);
        env.getHero().getInv().getListeditem().addListener(obsInventaire);

    }


    //pour chaque entré de touche
    private void configurerEvenements(){
        tabJeu.setOnKeyPressed(event -> gestionTouches.gererTouchePressee(event));
        tabJeu.setOnKeyReleased(event -> gestionTouches.gererToucheRelachee(event));
    }
    //chaque click de souris
    @FXML
    public void clicSouris(MouseEvent mouseEvent) {
        gestionSouris.clicDeSouris(mouseEvent);
    }
    @FXML
    public void gererPositionSouris(MouseEvent mouseEvent) {
        gestionSouris.gererPositionDeSouris(mouseEvent);
    }

    public GestionSouris getGestionSouris() {
        return gestionSouris;
    }

    //pour chaque action dans inventaire
    @FXML
    public void inv(ActionEvent event) {
    gestionInventaire.inv(event);
   }

    public TextArea getPauseID() {
        return pauseID;
    }

    public GestionInventaire getGestionInventaire() {
        return gestionInventaire;
    }

    public BarresStatut getBarreDeVieHero() {
        return barreDuHero;
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

    public TilePane getTilePaneId() {
        return tilePaneId;
    }

    public ImageView getBackground() {
        return background;
    }

    public List<VueActeur> getVuesActeurs() {
        return vuesActeurs;
    }

    public VueHero getV() {
        return v;
    }

    public Label getEcu() {
        return ecu;
    }

    public GestionBoucle getGestionBoucle() {
        return gestionBoucle;
    }

    public Pane getTabJeu() {
        return tabJeu;
    }

    public GridPane getTerrainGrid() {
        return terrainGrid;
    }
}