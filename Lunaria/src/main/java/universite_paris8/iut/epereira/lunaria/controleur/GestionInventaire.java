package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;

public class GestionInventaire {

    private Environement env;
    private Controleur controleur;

    //garde quel slot de l'inventaire est selectioné
    private int isSelectedInHand;

    //contient l'item dans la souris
    private Item tempItemSouris;

    //boolean popur savoir si l'inventaire est ouvert ou pas.
    private boolean inventaireBooleanOvert;

    public GestionInventaire(Environement env, Controleur controleur) {
        this.env = env;
        this.controleur = controleur;
        isSelectedInHand = 0;
    }

    //selectionne l'item en appuyant dans l'inventaire ou sur les touche 1-9
    public void selectItem(int i) {
        System.out.println("enter selectitem");
        if (!inventaireBooleanOvert) {
            // Vérifier si la case contient un item avant d'essayer de l'équiper
            if (env.getHero().getInv().getListeditem().get(i) != null) {

                if(controleur.getTilePaneInventaire().getChildren().get(i).getStyle().equals("-fx-border-color: grey")){
                    controleur.getTilePaneInventaire().getChildren().get(i).setStyle("-fx-border-color: yellow");
                } else if (controleur.getTilePaneInventaire().getChildren().get(i).getStyle().equals("-fx-border-color: grey")) {
                    controleur.getTilePaneInventaire().getChildren().get(i).setStyle("-fx-border-color: yellow");
                }

                // Déselectionner l'ancien item si différent
                if (isSelectedInHand != i) {
                    controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-border-color: grey");
                }

                isSelectedInHand = i;
                env.getHero().getInv().equiperItem(i);
            } else {
                // Si la case est vide, déselectionner tout
                controleur.getTilePaneInventaire().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: grey");

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
    public void inv(ActionEvent event) {
        Node source = (Node) event.getSource();
        Integer row = GridPane.getRowIndex(source);
        Integer col = GridPane.getColumnIndex(source);




        //Item temp = env.getHero().getInv().getIteminList(row * 3 + col);

        //vérifie si l'emplacement sur le quel on click est occupé
        //si occupé, echangé ce que contient la souris avec le contenue de l'emplacement
        //sinon, juste placer ce que contient la souris dans l'emplacement
        //if (temp != null) {
        //  Item switch2 = temp;
        //env.getHero().getInv().addItem(row * 3 + col, tempItemSouris);
        //tempItemSouris = switch2;
        // } else {
        //   env.getHero().getInv().addItem(row * 3 + col, tempItemSouris);
        // }
        selectItem(row * 9 + col);


        //charger ce que contient la case par une image de l'item, vide si null

        //imageInv1.setImage(new Image("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png"));

        //((Node) event.getSource()).setStyle("-fx-background-image: url("+getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png")+")");
        //((Node) event.getSource()).setOpacity(1.0);

        //inventaireGridPane.getChildren().get(row * 3 + col).setStyle();


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