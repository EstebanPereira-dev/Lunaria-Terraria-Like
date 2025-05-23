package universite_paris8.iut.epereira.lunaria.modele.items.Equipements;

import universite_paris8.iut.epereira.lunaria.modele.Item;

public class Projectile extends Item {
    protected int quantite;
    protected int degat;
    public Projectile(String nom, int quantite, String description, int id) {
        super(nom, description, false,id);
        this.quantite = quantite;
    }

    public int getDegat() {
        return degat;
    }
}
