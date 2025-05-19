package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

public class Adepte extends Ennemi {

    public Adepte(int pv, int v, int degat, Environement env, Hero hero, double x, double y) {
        super(pv,v,degat,env,hero,x,y);
    }

    @Override
    public void deplacement(){
        int aleatoire = rdm.nextInt(250);
        appliquerGravite();
        if (getPosX() < hero.getPosX())
            deplacerHorizontalement(vitesseX);
        else
            deplacerHorizontalement(-vitesseX);
        if (auSol && aleatoire <= 1)
            vitesseY = SAUT;
        deplacerVerticalement();
    }

    @Override
    public void attaque() {
        if (getPosX() == hero.getPosX() && auSol)
            hero.setPv(hero.getPv()-getDegat());
    }

    // Invocation ou autre type de condition
    public boolean conditionApparation() {
        return this.getEnv().getCylceJourNuit() == 'N';
    }
}
