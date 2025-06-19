package universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif;

import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Inventaire;
import universite_paris8.iut.epereira.lunaria.modele.Item;

import java.util.ArrayList;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public abstract class PNJ extends mobPassif{
    protected String nom, description;
    protected Boolean marchand;
    protected Inventaire inv;

    public PNJ(int pv, int v, Environement env, double x, double y, String description, boolean marchand) {
        super(pv, v, env, x, y);
        this.description = description;
        this.marchand = marchand;
        inv = new Inventaire(9);
    }
    @Override
    public void deplacement() {
        int aleatoire = rdm.nextInt(2500);
        appliquerGravite();

        tempsAvantChangement++;

        if (tempsAvantChangement >= dureeAction) {
            double rand = Math.random();
            if (getEnv().getMarchand() != inv) {

                if (rand < 0.3) {
                    direction = -1; // gauche
                } else if (rand < 0.6) {
                    direction = 1;  // droite
                } else {
                    direction = 0;  // arrêt
                }
            }
            else
                direction = 0;
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

    public Inventaire getInv() {
        return inv;
    }
}
