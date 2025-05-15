package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.InventaireJoueur;

public class Hero extends Acteur {
    private InventaireJoueur inv;

    public Hero(Environement env) {
        super(env);
        inv = new InventaireJoueur();
    }


    public InventaireJoueur getInv() {
        return inv;
    }
}
