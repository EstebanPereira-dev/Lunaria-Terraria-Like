package universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public abstract class mobPassif extends Acteur {

    public mobPassif(int pv, int v, Environement env, double x, double y) {
        super(pv, v, env, x, y);
    }
}
