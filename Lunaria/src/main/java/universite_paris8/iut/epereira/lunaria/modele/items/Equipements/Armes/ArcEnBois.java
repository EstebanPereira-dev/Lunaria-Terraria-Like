package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Armes;

import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Arme;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Projectiles.FlecheEnBois;

public class ArcEnBois extends Arme {
    //id:0-19 = blocs, 20-29 =armes, 30-39=outils

    public ArcEnBois(String nom, String description) {
        super("Arc en bois", "Un arc tout ce qu'il y'a de plus banal",20);
        degat = 3;
    }
}
