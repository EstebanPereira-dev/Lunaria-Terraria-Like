package universite_paris8.iut.epereira.lunaria.modele.items;

import universite_paris8.iut.epereira.lunaria.modele.Item;

public class Ressource extends Item {
    protected int quantite;
    public Ressource(String nom, int quantite, String description, int type) {
        super(nom, description, type);
        this.quantite = quantite;
    }
}
