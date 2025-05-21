package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.Random;

public abstract class Ennemi extends Acteur {

    protected Hero hero;
    protected int degat, range;
    public Ennemi(int pv, int v, int degat,int range, Environement env, Hero hero, double x, double y){
        super(pv,v,env,x,y);
        this.degat = degat;
        this.range = range;
        this.hero = hero;
    }

    public int getDegat() {
        return degat;
    }

    public Hero getHero() {
        return hero;
    }
}