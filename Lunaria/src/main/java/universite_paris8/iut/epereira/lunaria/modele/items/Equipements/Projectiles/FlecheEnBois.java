package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Projectiles;

import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Projectile;

public class FlecheEnBois extends Projectile {
    public FlecheEnBois(String nom, int quantite, String description, int type) {
        super(nom, quantite, description, type);
        degat = 2;
    }
}
