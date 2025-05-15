package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Adepte extends Ennemi {

    public Adepte(Environement env, double x, double y) {
        super(75,2,20,env,x,y);
    }

    // Invocation ou autre type de condition
    public boolean conditionApparation() {
        return this.getEnv().getCylceJourNuit() == 'N';
    }
}
