package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public class Adepte extends Ennemi {

    public Adepte(int pv, int v, int degat, int range, Environement env, Hero hero, double x, double y) {
        super(pv, v, degat, range, env, hero, x, y);
    }

    @Override
    public void deplacement() {
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
    public void agit() {
        double distanceX = Math.abs(getPosX() - hero.getPosX());
        double distanceY = Math.abs(getPosY() - hero.getPosY());
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance <= range) {
            hero.setPv(hero.getPv() - getDegat());
            System.out.println("Hero touché ! Points de vie restants: " + hero.getPv());

            if (hero.getPv() <= 0) {
                System.out.println("Hero vaincu !");
                hero.estMort();
            }
        }
    }


    // Invocation ou autre type de condition
    public boolean conditionApparation() {
        return this.getEnv().getCylceJourNuit() == 'N';
    }
}

