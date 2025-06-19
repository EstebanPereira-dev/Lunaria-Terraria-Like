package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Inventaire;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.vue.LoadImage;
import universite_paris8.iut.epereira.lunaria.vue.ObsInventaire;

public class GestionInventaire{

    private Controleur controleur;
    private boolean isHero;
    private LoadImage librairiImage;
    private Inventaire inv;
    private boolean cooldownInv;
    private TilePane tilePane;
    private Hero hero;

    //garde quel slot de l'inventaire est selectioné
    private int isSelectedInHand;


    //boolean popur savoir si l'inventaire est ouvert ou pas.
    private boolean inventaireBooleanOvert;

    public GestionInventaire(Hero hero, boolean isHero, Inventaire inv, TilePane tilePane, Controleur controleur){
        this.controleur = controleur;
        isSelectedInHand = 0;
        cooldownInv = true;
        this.isHero = isHero;
        this.hero = hero;
        librairiImage = new LoadImage();
        this.inv = inv;
        this.tilePane = tilePane;
        initPane();
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


    public void initInventaire() {
        for (int i = 0; i < inv.getTaille(); i++) {
            Label labelQuantite = new Label();

            //env.getHero().getInv().getQuantite()[i].addListener(((observableValue, number, t1) -> env.updateCraft()));
            labelQuantite.textProperty().bind(inv.getQuantite()[i].asString());
            labelQuantite.setStyle("-fx-background-color: grey");


            labelQuantite.setPrefSize(48,16);


            ImageView img = new ImageView(librairiImage.selectImage(inv.getListeditem().get(i)));
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
            tilePane.getChildren().add(i, warpper);



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

    public void initPane() {
        tilePane.setHgap(1);
        tilePane.setVgap(1);
        initInventaire();
        //ajout du listener sur l'observable liste de l'inventaire
        ObsInventaire obsInventaire = new ObsInventaire(tilePane);
        inv.getListeditem().addListener(obsInventaire);
        tilePane.setVisible(false);
        tilePane.setDisable(true);
        controleur.getTilePaneCraft().setVisible(false);
        controleur.getTilePaneCraft().setDisable(true);
    }

    //selectionne l'item en appuyant dans l'inventaire ou sur les touche 1-9
    public void selectItem(int i) {
        System.out.println("enter selectitem");
        if (!inventaireBooleanOvert && isHero) {
            // Vérifier si la case contient un item avant d'essayer de l'équiper
            if (inv.getListeditem().get(i) != null) {

                if ( tilePane.getChildren().get(i).getStyle().equals("-fx-border-color: blue")) {
                    tilePane.getChildren().get(i).setStyle("-fx-border-color: yellow");
                } else if (tilePane.getChildren().get(i).getStyle().equals("-fx-border-color: blue")) {
                    tilePane.getChildren().get(i).setStyle("-fx-border-color: yellow");
                }

                // Déselectionner l'ancien item si différent
                if (isSelectedInHand != i) {
                    tilePane.getChildren().get(isSelectedInHand).setStyle("-fx-border-color: blue");
                }

                isSelectedInHand = i;
                inv.equiperItem(i);
            } else {
                // Si la case est vide, déselectionner tout
                tilePane.getChildren().get(isSelectedInHand).setStyle("-fx-border-color: blue");

                System.out.println("Case vide, aucun item à sélectionner");
            }
        } else {
            // Mode inventaire ouvert
            if (inv.getListeditem().get(isSelectedInHand) != null) {
                inv.equiperItem(isSelectedInHand);
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
            if (inv.getListeditem().get(index) == null && hero.getSouris() == null ) {
                System.out.println("entrer case null");
            } else {
                if (event.getButton() == MouseButton.PRIMARY && isHero) {
                    System.out.println("entrer even.getButton == primary");
                    hero.clickPrimaire(index);
                } else if (event.getButton() == MouseButton.SECONDARY && isHero) {
                    System.out.println(" entrer even.getButton == secondary");
                    if (hero.getSouris() != null) {
                        //System.out.println("entrer souris plein");
                        hero.clickSecondairePlein(index);
                    } else if (hero.getSouris() == null) {
                        //System.out.println("entrer souris vide");
                        hero.clickSecondaireVide(index);
                    } else {
                        System.out.println("error, souris null avec quantite ou quantite avec souris non null");
                    }
                }
            }
            if(!isHero){

            }

            cooldown();
        }


    }


    //faire apparaitre/disparaitre l'inventaire
    public void setInvVisible(Boolean bool) {
        controleur.getTilePaneInventaire().setVisible(bool);
        controleur.getTilePaneInventaire().setDisable(!bool);
        controleur.getTilePaneCraft().setVisible(bool);
        controleur.getTilePaneCraft().setDisable(!bool);
    }

    public void setInventaireBooleanOvert(boolean inventaireBooleanOvert) {
        this.inventaireBooleanOvert = inventaireBooleanOvert;
    }

    public Boolean getInventaireBooleanOvert() {
        return inventaireBooleanOvert;
    }

    public void mettreAJourAffichage() {
        // Vérifier si l'item actuellement sélectionné existe encore
        if (inv.getListeditem().get(isSelectedInHand) == null) {
            // Déselectionner visuellement
            //controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: white");
            tilePane.getChildren().get(isSelectedInHand).setStyle("-fx-border-color: blue");

            // Chercher le prochain item équipé (s'il y en a un)
            int nouvelItemEquipe = inv.getItemEquipe();
            if (nouvelItemEquipe != -1) {
                isSelectedInHand = nouvelItemEquipe;
                //controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: yellow");
                tilePane.getChildren().get(isSelectedInHand).setStyle("-fx-background-color: yellow;");
            }
        }

        for (int i = 0; i < inv.getTaille(); i++) {
            if (inv.getQuantite()[i].getValue() == 0 && inv.getListeditem().get(i) != null) {
                inv.getListeditem().set(i, null);
            }
            if (inv.getQuantite()[i].getValue() < 0 && inv.getListeditem().get(i) != null) {
                System.out.println("\n\n\n\n QUANTITE IMPOSSIBLE\nQUANTITE NEGATIF\nQUANTITE IMPOSSIBLE\nQUANTITE NEGATIF\n\n\n\n");
            }
        }
    }


}