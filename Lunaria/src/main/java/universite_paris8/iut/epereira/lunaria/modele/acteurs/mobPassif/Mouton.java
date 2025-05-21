package universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif;

import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public class Mouton extends mobPassif{

    public Mouton(int pv, int v, Environement env, double x, double y) {
        super(pv, v, env, x, y);
    }

    @Override
    public void deplacement() {
        boolean droite = false;
        int aleatoire = rdm.nextInt(2500);
        appliquerGravite();
        if ( getPosX() < ConfigurationJeu.WIDTH_SCREEN -10) {
            deplacerHorizontalement(vitesseX);
            droite = true;
        }
        else if (droite) {
            deplacerHorizontalement(-vitesseX);
        }
        if (auSol && aleatoire <= 1)
            vitesseY = SAUT;
        deplacerVerticalement();
    }
    @Override
    public void agit() {
        System.out.println("ejigjjhsiohjios");
    }
}
