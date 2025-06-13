package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;

public class GestionInventaire {

    private Environement env;
    private Controleur controleur;

    //garde quel slot de l'inventaire est selectioné
    private int isSelectedInHand;


    //boolean popur savoir si l'inventaire est ouvert ou pas.
    private boolean inventaireBooleanOvert;

    public GestionInventaire(Environement env, Controleur controleur) {
        this.env = env;
        this.controleur = controleur;
        isSelectedInHand = 0;
    }

    public void initInventaire(TilePane paneInv){
        for(int i = 0; i < 45; i++){
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png")));
            img.setFitHeight(48);
            img.setFitWidth(48);
            img.setFocusTraversable(true);
            //img.setStyle("-fx-border-color: yellow");



            VBox warpper = new VBox(img);
            warpper.setPadding(new Insets(2.5));
            warpper.setStyle("-fx-border-color: blue;");
            warpper.setId(""+i);
            warpper.setFocusTraversable(true);
            paneInv.getChildren().add(i,warpper);
        }
    }

    public void initPane(TilePane tilePane, ObsInventaire obsInventaire){
        tilePane.setHgap(1);
        tilePane.setVgap(1);
        initInventaire(tilePane);
        //ajout du listener sur l'observable liste de l'inventaire
        obsInventaire = new ObsInventaire(tilePane,env);
        env.getHero().getInv().getListeditem().addListener(obsInventaire);
    }

    //selectionne l'item en appuyant dans l'inventaire ou sur les touche 1-9
    public void selectItem(int i) {
        System.out.println("enter selectitem");
        if (!inventaireBooleanOvert) {
            // Vérifier si la case contient un item avant d'essayer de l'équiper
            if (env.getHero().getInv().getListeditem().get(i) != null) {

                if(controleur.getTilePaneInventaire().getChildren().get(i).getStyle().equals("-fx-border-color: blue")){
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
                controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: blue");

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
        int index = Integer.parseInt(vbox.getId());
       // System.out.println("index vbox " + index);
        if(env.getHero().getInv().getListeditem().get(index) == null && env.getHero().getSouris() == null){
            //System.out.println("entrer case null");
        }
        else {
            if(event.getButton() == MouseButton.PRIMARY){
                //System.out.println("entrer even.getButton == primary");
                env.clickPrimaire(index);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                //System.out.println(" entrer even.getButton == secondary");
                if(env.getHero().getInv().getListeditem().get(index) != null){
                    //System.out.println("entrer souris plein");
                    env.clickSecondairePlein(index);
                } else if (env.getHero().getInv().getListeditem().get(index) == null) {
                    //System.out.println("entrer souris vide");
                    env.clickSecondaireVide(index);
                }
                else {
                    System.out.println("error, souris null avec quantite ou quantite avec souris non null");
                }
            }
        }


    }


    //faire apparaitre/disparaitre l'inventaire
    public void setInvVisible(Boolean bool) {
//        controleur.getInventaireGridPane().setVisible(bool);
//        controleur.getInventaireGridPane().setDisable(!bool);
//        for (int i = 0; i < 9; i++) {
//            controleur.getInventaireGridPane().getChildren().get(i).setVisible(bool);
//        }
        controleur.getTilePaneInventaire().setVisible(bool);
        controleur.getTilePaneInventaire().setDisable(!bool);
    }

    public void setInventaireBooleanOvert(boolean inventaireBooleanOvert) {
        this.inventaireBooleanOvert = inventaireBooleanOvert;
    }

    public Boolean getInventaireBooleanOvert(){
        return inventaireBooleanOvert;
    }

    public void mettreAJourAffichage() {
        // Vérifier si l'item actuellement sélectionné existe encore
        if (env.getHero().getInv().getListeditem().get(isSelectedInHand) == null) {
            // Déselectionner visuellement
            //controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: white");
            controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: white");

            // Chercher le prochain item équipé (s'il y en a un)
            int nouvelItemEquipe = env.getHero().getInv().getItemEquipe();
            if (nouvelItemEquipe != -1) {
                isSelectedInHand = nouvelItemEquipe;
                //controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: yellow");
                controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: yellow;");
            }
        }

        for(int i = 0; i < env.getHero().getInv().getTaille(); i++){
            if(env.getHero().getInv().getQuantite()[i] == 0 && env.getHero().getInv().getListeditem().get(i) != null){
                env.getHero().getInv().getListeditem().set(i,null);
            }
            if(env.getHero().getInv().getQuantite()[i] < 0 && env.getHero().getInv().getListeditem().get(i) != null){
                System.out.println("\n\n\n\n QUANTITE IMPOSSIBLE\nQUANTITE NEGATIF\nQUANTITE IMPOSSIBLE\nQUANTITE NEGATIF\n\n\n\n");
            }
        }
    }
}