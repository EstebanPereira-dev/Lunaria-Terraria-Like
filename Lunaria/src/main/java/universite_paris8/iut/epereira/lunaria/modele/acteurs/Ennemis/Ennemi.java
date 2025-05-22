package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public abstract class Ennemi extends Acteur {

    protected Hero hero;
    protected int degat, range;

    public Ennemi(int pv, int v, int degat, int range, Environement env, Hero hero, double x, double y){
        super(pv, v, env, x, y);
        this.degat = degat;
        this.range = range;
        this.hero = hero;
    }

    // Condition d'apparition - à redéfinir dans chaque ennemi
    public abstract boolean conditionApparation();

    // Méthode de spawn simple
    public abstract void spawner();

    public int getDegat() {
        return degat;
    }

    public Hero getHero() {
        return hero;
    }
}