package universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif;

import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.WIDTH_SCREEN;
import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public class Mouton extends mobPassif{

    public Mouton(int pv, int v, Environement env, double x, double y) {
        super(pv, v, env, x, y);
    }

    @Override
    public void deplacement() {

        int aleatoire = rdm.nextInt(2500);
        appliquerGravite();

        tempsAvantChangement++;

        if (tempsAvantChangement >= dureeAction) {
            double rand = Math.random();
            if (rand < 0.3) {
                direction = -1; // gauche
            } else if (rand < 0.6) {
                direction = 1;  // droite
            } else {
                direction = 0;  // arrêt
            }

            tempsAvantChangement = 0;
            dureeAction = (int)(Math.random() * 120 + 60);
        }

        deplacerHorizontalement(vitesseX * direction);

        // Rebonds sur les bords
        if (getPosX() > ConfigurationJeu.WIDTH_SCREEN - 10) {
            direction = -1;
        } else if (getPosX() < 10) {
            direction = 1;
        }

        if (auSol && aleatoire <= 1)
            vitesseY = SAUT;
        deplacerVerticalement();
    }
    @Override
    public void agit() {
        System.out.println("action mouton");
    }
}
