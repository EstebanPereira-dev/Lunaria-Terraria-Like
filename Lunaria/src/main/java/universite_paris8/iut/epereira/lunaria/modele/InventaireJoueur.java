package universite_paris8.iut.epereira.lunaria.modele;

import javafx.scene.layout.HBox;

public class InventaireJoueur {
    private Item[] listeditem;

    public InventaireJoueur(){
        listeditem = new Item[9];
    }



    //met un item dans l'inventaire, si l espace est occupé, on retourne l'item dans la position
    public Item addItem(int pos, Item item){
        Item retour = null;

        if(listeditem[pos] != null){
            retour = listeditem[pos];
        }
        listeditem[pos] = item;
        return retour;
    }

    //enlève l'item a l'emplacement
    public Item removeItem(int pos){
        if(listeditem[pos] == null){
            return null;
        }
        else{
            return listeditem[pos];
        }
    }

    //retourne la liste des item
    public Item[] getListeditem() {
        return listeditem;
    }
    //return un item dans l'inventaire
    public Item getIteminList(int pos){
        return listeditem[pos];
    }



}
