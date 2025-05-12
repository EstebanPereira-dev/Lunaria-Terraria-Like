package universite_paris8.iut.epereira.lunaria.modele.ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Adepte extends Acteur {

    public Adepte(Environement env) {
        super(75,2,20,env);
    }

    @Override
    public boolean conditionApparation() {
        if(this.getEnv().getCylceJourNuit() == 'N'){
            return true;
        }
        return false;
    }
}
