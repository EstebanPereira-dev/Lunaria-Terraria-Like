package universite_paris8.iut.epereira.lunaria.modele.ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Lycanthrope extends Acteur {
    public Lycanthrope(int pv, int v, int degat, Environement env) {
        super(pv, v, degat, env);
    }

    @Override
    public boolean conditionApparation() {
        return false;
    }
}

