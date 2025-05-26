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

    public Item selectItem(int i) {

        if (!inventaireBooleanOvert) {


            //System.out.println(inventaireGridPane.getChildren().get(i).getStyle());

            if (controleur.getInventaireGridPane().getChildren().get(i).getStyle().equals("-fx-background-color: white")) {
                //System.out.println("change en jaune");
                controleur.getInventaireGridPane().getChildren().get(i).setStyle("-fx-background-color: yellow");
                //System.out.println(inventaireGridPane.getChildren().get(i).getStyle());
            } else {
                if (controleur.getInventaireGridPane().getChildren().get(i).getStyle().toString().equals("-fx-background-color: yellow")) {
                    //System.out.println("change en blanc");
                    controleur.getInventaireGridPane().getChildren().get(i).setStyle("-fx-background-color: white");
                    return null;
                    //System.out.println(inventaireGridPane.getChildren().get(i).getStyle());
                }
            }
            if (isSelectedInHand != i) {
                controleur.getInventaireGridPane().getChildren().get(isSelectedInHand).setStyle("-fx-background-color: white");
            }
            isSelectedInHand = i;
            return env.getHero().getInv().getIteminList(i);
        }
        else{
            return env.getHero().getInv().getIteminList(isSelectedInHand);
        }

    }

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
        selectItem(row * 3 + col);


        //charger ce que contient la case par une image de l'item, vide si null

        //imageInv1.setImage(new Image("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png"));

        //((Node) event.getSource()).setStyle("-fx-background-image: url("+getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png")+")");
        //((Node) event.getSource()).setOpacity(1.0);

        //inventaireGridPane.getChildren().get(row * 3 + col).setStyle();


    }


    //faire apparaitre/disparaitre l'inventaire
    public void setInvVisible(Boolean bool) {
        controleur.getInventaireGridPane().setVisible(bool);
        controleur.getInventaireGridPane().setDisable(!bool);
        for (int i = 0; i < 9; i++) {
            controleur.getInventaireGridPane().getChildren().get(i).setVisible(bool);
        }
    }



    public void setInventaireBooleanOvert(boolean inventaireBooleanOvert) {
        this.inventaireBooleanOvert = inventaireBooleanOvert;
    }

    public Boolean getInventaireBooleanOvert(){
        return inventaireBooleanOvert;
    }
}
