package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Damnés extends Ennemi {
    public Damnés(int pv, int v, int degat, Environement env, double x, double y) {
        super(x, y,env);
    }
    // Invocation ou autre type de condition
    public boolean conditionApparation() {
        return this.getEnv().getCylceJourNuit() == 'N';
    }
}
