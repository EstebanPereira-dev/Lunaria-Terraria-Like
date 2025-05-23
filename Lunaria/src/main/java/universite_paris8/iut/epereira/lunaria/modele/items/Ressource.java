package universite_paris8.iut.epereira.lunaria.modele.items;

import universite_paris8.iut.epereira.lunaria.modele.Item;

public class Ressource extends Item {
    protected int quantite;
    public Ressource(String nom, int quantite, String description, boolean peutEtrePlace,int id) {
        super(nom, description,peutEtrePlace,id);
        this.quantite = quantite;
    }
}
