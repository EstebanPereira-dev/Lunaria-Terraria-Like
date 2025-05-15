package universite_paris8.iut.epereira.lunaria.modele;

import javafx.scene.layout.HBox;

public class InventaireJoueur {
    private Item[][] listeditem;

    public InventaireJoueur(){
    }

    public Item addItem(HBox invcase ){

        switch (invcase.getId()){
            case "case1":
                System.out.println("case1");
                break;
            case "case2":
                System.out.println("case2");
                break;
            case "case3":
                System.out.println("case3");
                break;
            case "case4":
                System.out.println("case4");
                break;
        }
        return null;
    }
}
