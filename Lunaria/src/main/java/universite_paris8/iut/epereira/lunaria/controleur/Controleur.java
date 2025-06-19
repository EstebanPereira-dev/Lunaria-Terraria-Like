package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.layout.VBox;
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
    private TilePane tilePaneCraft;

    @FXML
    private TilePane tilePaneMarchand;

    @FXML
    private VBox vbox1,vbox2,vbox3,vbox4;

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
    private LecteurAudio lecteurAudio;

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
        lecteurAudio = new LecteurAudio();
        v = new VueHero(env.getHero(), this);

        tilePaneInventaire.toFront();
        tilePaneCraft.toFront();

        LoadImage imgLoad = new LoadImage();
        ImageView img1,img2,img3,img4;

        img1 = new ImageView(imgLoad.selectImage(env.getListeCraft().get(0).getResultat()));
        img2 = new ImageView(imgLoad.selectImage(env.getListeCraft().get(1).getResultat()));
        img3 = new ImageView(imgLoad.selectImage(env.getListeCraft().get(2).getResultat()));
        img4 = new ImageView(imgLoad.selectImage(env.getListeCraft().get(3).getResultat()));

        img1.setOnMouseClicked(event -> craftHacheBois());
        img2.setOnMouseClicked(event -> craftPiocheBois());
        img3.setOnMouseClicked(event -> craftHachePierre());
        img4.setOnMouseClicked(event -> craftPiochePierre());

        img1.setFitHeight(48);
        img2.setFitHeight(48);
        img3.setFitHeight(48);
        img4.setFitHeight(48);

        img1.setFitWidth(48);
        img2.setFitWidth(48);
        img3.setFitWidth(48);
        img4.setFitWidth(48);

        vbox1.getChildren().add(img1);
        vbox2.getChildren().add(img2);
        vbox3.getChildren().add(img3);
        vbox4.getChildren().add(img4);

        vueCraft = new VueCraft(tilePaneCraft,env);
        vueCraft.init();


        //env.getCraftingList().addListener(new ObsCraft(tilePaneCraft,env));




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
        initialiserAudio();
        env.getEtatJour().addListener((e-> vueEnvironnement.setBackground()));

        //initialisation du gestionaire de la map
        gestionMap = new VueTerrain(env, this);
        gestionTouches = new GestionTouches(env, this);
        gestionSouris = new GestionSouris(env, this);
        gestionInventaire = new GestionInventaire(hero,true,hero.getInv(),tilePaneInventaire);
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
    }

    private void initialiserAudio() {
        // Musique initiale
        lecteurAudio.jouerSon("src/main/resources/universite_paris8/iut/epereira/lunaria/DossierMap/jour.wav");

        // Listener pour changement jour/nuit
        env.getEtatJour().addListener((obs, oldVal, newVal) -> {
            lecteurAudio.arreter();
            if (newVal) {
                lecteurAudio.jouerSon("src/main/resources/universite_paris8/iut/epereira/lunaria/DossierMap/jour.wav");
            } else {
                lecteurAudio.jouerSon("src/main/resources/universite_paris8/iut/epereira/lunaria/DossierMap/nuit.wav");
            }
        });
    }

    //pour chaque entré de touche
    private void configurerEvenements() {
        tabJeu.setOnKeyPressed(event -> gestionTouches.gererTouchePressee(event));
        tabJeu.setOnKeyReleased(event -> gestionTouches.gererToucheRelachee(event));
    }

    //methode barbar car pas de temps
    @FXML
    public void craftHacheBois(){
        System.out.println("entrer dans hachebois");
        System.out.println(env.getHero().getSouris());
        if (env.getHero().getSouris() == null){
            System.out.println("entrer dans hachebois if");
            env.getHero().setSouris(env.getListeCraft().get(0).crafting());
            env.getHero().setQuantiteItem(1);
        }
    }

    @FXML
    public void craftPiocheBois(){
        System.out.println("entrer dans pioche bois");
        if (env.getHero().getSouris() == null){

            System.out.println("entrer dans pioche bois if");
            env.getHero().setSouris(env.getListeCraft().get(1).crafting());
            env.getHero().setQuantiteItem(1);
        }
    }

    @FXML
    public void craftHachePierre(){
        System.out.println("entrer dans hache pierre");
        if (env.getHero().getSouris() == null){

            System.out.println("entrer dans hache pierre if");
            env.getHero().setSouris(env.getListeCraft().get(2).crafting());
            env.getHero().setQuantiteItem(1);
        }
    }

    @FXML
    public void craftPiochePierre(){
        System.out.println("entrer dans pioche pierre");
        if(env.getHero().getSouris() == null){

            System.out.println("entrer dans pioche pierre if");
            env.getHero().setSouris(env.getListeCraft().get(3).crafting());
            env.getHero().setQuantiteItem(1);
        }
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

    public TilePane getTilePaneCraft() {
        return tilePaneCraft;
    }
}
