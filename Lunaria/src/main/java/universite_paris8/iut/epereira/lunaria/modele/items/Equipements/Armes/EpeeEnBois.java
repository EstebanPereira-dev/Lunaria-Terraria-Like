package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Armes;

import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Arme;

public class EpeeEnBois extends Arme {
    //id:0-19 = blocs, 20-29 =armes, 30-39=outils
    public EpeeEnBois() {
        super("Epée en bois", "Arme tranchante capable de pourfendre le mal",21);
        degat = 3;
        range = 2;
    }
}
