package universite_paris8.iut.epereira.lunaria.modele.ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Damnés extends Acteur {
    public Damnés(int pv, int v, int degat, Environement env) {
        super(pv, v, degat, env);
    }

    @Override
    public boolean conditionApparation() {
        return true;
    }
}
