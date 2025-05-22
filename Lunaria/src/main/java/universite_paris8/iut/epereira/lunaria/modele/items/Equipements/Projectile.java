package universite_paris8.iut.epereira.lunaria.modele.items.Equipements;

import universite_paris8.iut.epereira.lunaria.modele.Item;

public class Projectile extends Item {
    protected int quantite;
    protected int degat;
    public Projectile(String nom, int quantite, String description, int type) {
        super(nom, description, type);
        this.quantite = quantite;
    }

    public int getDegat() {
        return degat;
    }
}
