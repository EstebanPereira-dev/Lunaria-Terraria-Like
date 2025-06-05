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
import universite_paris8.iut.epereira.lunaria.vue.VueActeurFactory;
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

    @FXML
    private ImageView imageInv1,imageInv2,imageInv3,imageInv4,imageInv5,imageInv6,imageInv7,imageInv8,imageInv9;

    //listener de l'inventaire
    private ObsInventaire obsInventaire;

    @FXML //Afficher le fond
    private ImageView background;

    //Barrre de vie du hero
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





    @Override //initialization
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Création de l'environement de la taille de l'écren
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);

        //initialisation du gestionaire de la map
        gestionMap = new VueTerrain(env, this);
        gestionTouches= new GestionTouches(env,this);
        gestionSouris = new GestionSouris(env,this);
        gestionInventaire = new GestionInventaire(env,this);
        gestionBoucle = new GestionBoucle(env,this);

        //initialisation de la barre de vie du hero
        barreDeVieHero = new BarreDeVie(env.getHero().getPv(), 200, 20);
        barreDeVieHero.setTranslateX(ConfigurationJeu.WIDTH_SCREEN - 230);
        barreDeVieHero.setTranslateY(30);
        tabJeu.getChildren().add(barreDeVieHero);

        //ajouter tous les acteurs dans la vue
        for (Acteur a : env.getActeurs()) {
            VueActeur vue = VueActeurFactory.creerVue(a, this);
            vuesActeurs.add(vue);
        }

        //démarage du jeux
        configurerEvenements();
        gestionBoucle.creerBoucleDeJeu();
        Platform.runLater(() -> {
            gestionMap.chargerTiles(env.getTerrain());
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

    public TilePane getTilePaneId() {
        return tilePaneId;
    }

    public ImageView getBackground() {
        return background;
    }

    public List<VueActeur> getVuesActeurs() {
        return vuesActeurs;
    }

    public GestionBoucle getGestionBoucle() {
        return gestionBoucle;
    }

    public Pane getTabJeu() {
        return tabJeu;
    }
}