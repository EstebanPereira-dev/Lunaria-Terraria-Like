package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.vue.ObsInventaire;

public class GestionInventaire{

    private Environement env;
    private Controleur controleur;
    private boolean hero;
    Image imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
    Image imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
    Image imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
    Image imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
    Image imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
    Image imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
    Image imagePiocheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/PiocheEnBois.png"));
    Image imageHacheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/HacheEnBois.png"));
    Image imageViandeMouton = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/ViandeMouton.png"));

    boolean cooldownInv;

    //garde quel slot de l'inventaire est selectioné
    private int isSelectedInHand;


    //boolean popur savoir si l'inventaire est ouvert ou pas.
    private boolean inventaireBooleanOvert;

    public GestionInventaire(Environement env, Controleur controleur, boolean hero) {
        this.env = env;
        this.controleur = controleur;
        isSelectedInHand = 0;
        cooldownInv = true;
        this.hero = hero;
    }

    public void cooldown(){
        cooldownInv = false;
        Timeline invCooldown = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    cooldownInv = true;
                })
        );
        invCooldown.setCycleCount(1);
        invCooldown.play();
    }


    public Image selectImage(Item id){
        Image sprite;
        if(id == null){
            sprite = imageVide;
        }
        else{
            switch (id.getId()){
                case 1:
                    sprite = imageTerre;
                    break;

                case 2:
                    sprite = imageHerbe;
                    break;

                case 3:
                    sprite = imageBuisson;
                    break;

                case 4:
                    sprite = imageMur;
                    break;

                case 5:
                    sprite = imageBois;
                    break;

                case 30:
                    sprite = imageHacheEnBois;
                    break;

                case 31:
                    sprite = imagePiocheEnBois;
                    break;

                case 40:
                    sprite = imageViandeMouton;
                    break;

                default:
                    sprite = imageVide;
                    break;
            }
        }
        return sprite;
    }

    public void initInventaire(TilePane paneInv) {
        for (int i = 0; i < env.getHero().getInv().getTaille(); i++) {
            Label labelQuantite = new Label();
            labelQuantite.textProperty().bind(env.getHero().getInv().getQuantite()[i].asString());
            labelQuantite.setStyle("-fx-background-color: grey");

            labelQuantite.setPrefSize(48,16);

            ImageView img = new ImageView(selectImage(env.getHero().getInv().getListeditem().get(i)));
            img.setFitHeight(48);
            img.setFitWidth(48);
            img.setFocusTraversable(true);
            //img.setStyle("-fx-border-color: yellow");


            VBox warpper = new VBox(img);
            warpper.getChildren().add(labelQuantite);


            warpper.setPadding(new Insets(2.5));
            warpper.setStyle("-fx-border-color: blue;");
            warpper.setId("" + i);
            warpper.setFocusTraversable(true);
            paneInv.getChildren().add(i, warpper);



            warpper.setOnMouseClicked(event -> {
                System.out.println("entere warpper mouseClicked");
                inv(event,warpper);
            });

            img.setOnMouseClicked(event -> {
                System.out.println("entrer img mouseClicked");
                inv(event, (VBox) img.getParent());
            });


        }
    }

    public void initPane(TilePane tilePane, ObservableList<Item> liste) {
        tilePane.setHgap(1);
        tilePane.setVgap(1);
        initInventaire(tilePane);
        //ajout du listener sur l'observable liste de l'inventaire
        ObsInventaire obsInventaire = new ObsInventaire(tilePane,env);
        liste.addListener(obsInventaire);
    }

    //selectionne l'item en appuyant dans l'inventaire ou sur les touche 1-9
    public void selectItem(int i) {
        System.out.println("enter selectitem");
        if (!inventaireBooleanOvert) {
            // Vérifier si la case contient un item avant d'essayer de l'équiper
            if (env.getHero().getInv().getListeditem().get(i) != null) {

                if (controleur.getTilePaneInventaire().getChildren().get(i).getStyle().equals("-fx-border-color: blue")) {
                    controleur.getTilePaneInventaire().getChildren().get(i).setStyle("-fx-border-color: yellow");
                } else if (controleur.getTilePaneInventaire().getChildren().get(i).getStyle().equals("-fx-border-color: blue")) {
                    controleur.getTilePaneInventaire().getChildren().get(i).setStyle("-fx-border-color: yellow");
                }

                // Déselectionner l'ancien item si différent
                if (isSelectedInHand != i) {
                    controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-border-color: blue");
                }

                isSelectedInHand = i;
                env.getHero().getInv().equiperItem(i);
            } else {
                // Si la case est vide, déselectionner tout
                controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-border-color: blue");

                System.out.println("Case vide, aucun item à sélectionner");
            }
        } else {
            // Mode inventaire ouvert
            if (env.getHero().getInv().getListeditem().get(isSelectedInHand) != null) {
                env.getHero().getInv().equiperItem(isSelectedInHand);
            }
        }
    }


    //place les item de la souris a l'inventaire
    public void inv(MouseEvent event, VBox vbox) {
        System.out.println(cooldownInv);
        if(cooldownInv){
            System.out.println("entrer dnas inv");
            int index = Integer.parseInt(vbox.getId());
            // System.out.println("index vbox " + index);
            if (env.getHero().getInv().getListeditem().get(index) == null && env.getHero().getSouris() == null) {
                System.out.println("entrer case null");
            } else {
                if (event.getButton() == MouseButton.PRIMARY) {
                    System.out.println("entrer even.getButton == primary");
                    env.clickPrimaire(index);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println(" entrer even.getButton == secondary");
                    if (env.getHero().getSouris() != null) {
                        //System.out.println("entrer souris plein");
                        env.clickSecondairePlein(index);
                    } else if (env.getHero().getSouris() == null) {
                        //System.out.println("entrer souris vide");
                        env.clickSecondaireVide(index);
                    } else {
                        System.out.println("error, souris null avec quantite ou quantite avec souris non null");
                    }
                }
            }
            cooldown();
        }


    }


    //faire apparaitre/disparaitre l'inventaire
    public void setInvVisible(Boolean bool) {
        controleur.getTilePaneInventaire().setVisible(bool);
        controleur.getTilePaneInventaire().setDisable(!bool);
        controleur.getCraftPane().setVisible(bool);
        controleur.getCraftPane().setDisable(!bool);
    }

    public void setInventaireBooleanOvert(boolean inventaireBooleanOvert) {
        this.inventaireBooleanOvert = inventaireBooleanOvert;
    }

    public Boolean getInventaireBooleanOvert() {
        return inventaireBooleanOvert;
    }

    public void mettreAJourAffichage() {
        // Vérifier si l'item actuellement sélectionné existe encore
        if (env.getHero().getInv().getListeditem().get(isSelectedInHand) == null) {
            // Déselectionner visuellement
            //controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: white");
            controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-border-color: blue");

            // Chercher le prochain item équipé (s'il y en a un)
            int nouvelItemEquipe = env.getHero().getInv().getItemEquipe();
            if (nouvelItemEquipe != -1) {
                isSelectedInHand = nouvelItemEquipe;
                //controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: yellow");
                controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: yellow;");
            }
        }

        for (int i = 0; i < env.getHero().getInv().getTaille(); i++) {
            if (env.getHero().getInv().getQuantite()[i].getValue() == 0 && env.getHero().getInv().getListeditem().get(i) != null) {
                env.getHero().getInv().getListeditem().set(i, null);
            }
            if (env.getHero().getInv().getQuantite()[i].getValue() < 0 && env.getHero().getInv().getListeditem().get(i) != null) {
                System.out.println("\n\n\n\n QUANTITE IMPOSSIBLE\nQUANTITE NEGATIF\nQUANTITE IMPOSSIBLE\nQUANTITE NEGATIF\n\n\n\n");
            }
        }
    }
}