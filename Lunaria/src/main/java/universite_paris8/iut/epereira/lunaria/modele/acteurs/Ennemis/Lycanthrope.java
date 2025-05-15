package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Lycanthrope extends Ennemi {
    public Lycanthrope(int pv, int v, int degat, Environement env, double x, double y) {
        super(pv, v, degat, env,x,y);
    }

    // Invocation ou autre type de condition
    public boolean conditionApparation() {
        return false;
    }
}

