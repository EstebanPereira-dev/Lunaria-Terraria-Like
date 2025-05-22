package universite_paris8.iut.epereira.lunaria.modele;
import universite_paris8.iut.epereira.lunaria.modele.Item;

import javafx.scene.layout.HBox;

public class InventaireJoueur {
    private Item[] listeditem;
    private int taille;


    public InventaireJoueur() {
        taille = 9;
        listeditem = new Item[taille];
        for (int i = 0; i < taille; i++) {
            listeditem[i] = null;
        }
    }


    public int trouverPremiereCaseVide() {
        for (int i = 0; i < listeditem.length; i++) {
            if (listeditem[i] == null) return i;
        }
        return -1; // inventaire plein
    }

    //met un item dans l'inventaire, si l espace est occupé, on retourne l'item dans la position
    public Item addItem(int pos, Item item) {
        Item retour = null;

        if (listeditem[pos] != null) {
            retour = listeditem[pos];
        }
        listeditem[pos] = item;
        return retour;
    }

    //enlève l'item a l'emplacement
    public void removeItem(int pos) {
        listeditem[pos] = null;
    }


    //retourne la liste des item
    public Item[] getListeditem() {
        return listeditem;
    }

    //return un item dans l'inventaire
    public Item getIteminList(int pos) {
        return listeditem[pos];
    }

    public int getTaille() {
        return taille;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("Inventaire:\n");
        for (Item item : listeditem) {
            if (item != null) {  // Ajout de cette vérification
                sb.append("- ").append(item.getNom()).append("\n");
            }
        }
        return sb.toString();
    }

/*public int getItemEquipe (){
        int i=0;
        while (!listeditem[i].estEquipe()){

        }
}*/

    public void equiperItem(int pos) {
        if (pos >= 0 && pos < taille && listeditem[pos] != null) {
            for (int i=0; i< listeditem.length;i++){
                if (listeditem[i].estEquipe())
                    desequiperItem(i);
            }
            listeditem[pos].setEquipe(true);
        }
    }
    public void desequiperItem(int pos) {
        if (pos >= 0 && pos < taille && listeditem[pos] != null) {
            listeditem[pos].setEquipe(false);
        }
    }

}



