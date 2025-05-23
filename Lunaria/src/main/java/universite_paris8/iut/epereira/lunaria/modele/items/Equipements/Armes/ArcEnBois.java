package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Armes;

import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Arme;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Projectiles.FlecheEnBois;

public class ArcEnBois extends Arme {
    public ArcEnBois(String nom, String description) {
        super("Arc en bois", "Un arc tout ce qu'il y'a de plus banal",6);
        degat = 3;
    }
}
