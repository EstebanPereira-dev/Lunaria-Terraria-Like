package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public class Ennemi extends Acteur {
    public Ennemi(int pv, int v, int degat, Environement env, double x, double y){
        super(75,2,20,env,x,y);
    }

}
