package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.Random;

public abstract class Ennemi extends Acteur {
    static public Random rdm = new Random();
    protected Hero hero;
    public Ennemi(int pv, int v, int degat,int range, Environement env, Hero hero, double x, double y){
        super(pv,v,degat,range,env,x,y);
        this.hero = hero;
    }
    @Override
    public void deplacement(){
        appliquerGravite();
        deplacerVerticalement();
    }

    public Hero getHero() {
        return hero;
    }
}