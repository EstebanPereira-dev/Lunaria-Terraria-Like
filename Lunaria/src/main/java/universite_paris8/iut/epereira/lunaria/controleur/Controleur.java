package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.vue.*;


import java.net.URL;
import java.util.*;
import java.util.List;

public class Controleur implements Initializable {
//    @FXML
//    private GridPane inventaireGridPane;  //gridPane qui contient les emplacement d'inventaire

    @FXML
    private TilePane tilePaneInventaire;

    @FXML
    private Label ecu;

    @FXML
    private TilePane tilePaneId; //tilePane pour poesr nos bloc et les casser

    @FXML //Texte Pause quand on arrete le jeux
    private TextArea pauseID;

    @FXML //le Pane qui contient tout
    private Pane tabJeu;

    @FXML
    private ScrollPane craftPane;

    @FXML
    private TilePane tilePaneMarchand;

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
    private GestionInventaire gestionInventaireMarchand;
    private GestionBoucle gestionBoucle;
    private List<VueActeur> vuesActeurs = new ArrayList<>();
    private VueHero v;

    //game loop
    private Timeline gameLoop;

    //Vue Environnement
    private VueEnvironnement vueEnvironnement;

    private CameraJeu camera;

    private VueCraft vueCraft;



    // Getter pour accéder à la caméra
    public CameraJeu getCamera() {
        return camera;
    }
    @Override //initialization
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Création de l'environement
        env = new Environement(ConfigurationJeu.WIDTH_TILES * ConfigurationJeu.TAILLE_TUILE, ConfigurationJeu.HEIGHT_TILES * ConfigurationJeu.TAILLE_TUILE);
        // CRÉER LA CAMÉRA
        double largeurCarteComplete = ConfigurationJeu.WIDTH_TILES * ConfigurationJeu.TAILLE_TUILE; // 100 * 16 = 1600
        double hauteurCarteComplete = ConfigurationJeu.HEIGHT_TILES * ConfigurationJeu.TAILLE_TUILE; // 100 * 16 = 1600
        camera = new CameraJeu(terrainGrid, tabJeu, env.getHero(), largeurCarteComplete, hauteurCarteComplete);

        // Récupération du terrain
        Terrain terrain = env.getTerrain();

        // 2. Configuration du terrain (observateur)
        ObsTerrain obsTerrain = new ObsTerrain(terrainGrid, terrain.getWidth());
        terrain.getTableau().addListener(obsTerrain);
        for (int i = 0; i < terrain.getTableau().size(); i++) {
            obsTerrain.updateTuile(i, terrain.getTableau().get(i));
        }

//        vueCraft = new VueCraft(craftPane);
//        vueCraft.init();



        // 3. Créer les vues d'acteurs
        v = new VueHero(env.getHero(), this);
        vuesActeurs.add(v);

        Hero hero = env.getHero();
        for (Acteur a : env.getActeurs()) {
            if (a != hero) {
                VueActeur vue = VueActeurFactory.creerVue(a, this);
                vuesActeurs.add(vue);
            }
        }

        // 4. Centrer la caméra sur le héros
        camera.centrerSurHero();

        // 5. Initialiser les autres gestionnaires
        this.vueEnvironnement = new VueEnvironnement(this);
        gestionMap = new VueTerrain(env, this);
        gestionTouches = new GestionTouches(env, this);
        gestionSouris = new GestionSouris(env, this);
        gestionInventaire = new GestionInventaire(env, this, true);
        gestionBoucle = new GestionBoucle(env, this);

        // 6. Éléments d'interface
        barreDuHero = new BarresStatut(env.getHero().getPv(), 200, 200, 20);
        barreDuHero.setTranslateX(ConfigurationJeu.WIDTH_SCREEN - 230);
        barreDuHero.setTranslateY(30);
        tabJeu.getChildren().add(barreDuHero); // Directement au tabJeu, pas au conteneur monde

        // 7. Démarrage du jeu
        configurerEvenements();
        gestionBoucle.creerBoucleDeJeu();
        Platform.runLater(() -> {
            tabJeu.requestFocus();
            gestionBoucle.demarrer();
        });

        initTilePaneInv(tilePaneInventaire, hero.getInv().getListeditem());
    }

    public void initTilePaneInv(TilePane tilePane, ObservableList<Item> liste) {
        gestionInventaire.initPane(tilePane, liste);
    }


    //pour chaque entré de touche
    private void configurerEvenements() {
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


    public TilePane getTilePaneInventaire() {
        return tilePaneInventaire;
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

    public VueEnvironnement getVueEnvironnement() {
        return vueEnvironnement;
    }

    public ScrollPane getCraftPane() {
        return craftPane;
    }
}
