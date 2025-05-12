package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Adepte extends Acteur {

    public Adepte(Environement env) {
        super(75,2,20,env);
    }

    // Invocation ou autre type de condition
    public boolean conditionApparation() {
        return this.getEnv().getCylceJourNuit() == 'N';
    }
}
