package universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

public abstract class mobPassif extends Acteur {
    protected int direction = 1;
    protected int tempsAvantChangement = 0;
    protected int dureeAction = (int)(Math.random() * 120 + 60); // 1-3 secondes
    public mobPassif(int pv, int v, Environement env, double x, double y) {
        super(pv, v, env, x, y);
    }
}
