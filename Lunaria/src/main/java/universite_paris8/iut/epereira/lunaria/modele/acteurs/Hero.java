package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.InventaireJoueur;

import java.util.ArrayList;

public class Hero extends Acteur {
    private InventaireJoueur inv;
    private ArrayList<Boolean> actions;
    private boolean haut = false , bas = false, droite = false, gauche = false, inventaire = false, pause = false;

    public Hero(Environement env) {
        super(env);
        actions = new ArrayList<>();
        inv = new InventaireJoueur();
        remplirAction();
    }

    public void remplirAction(){
        actions.add(haut);
        actions.add(bas);
        actions.add(droite);
        actions.add(gauche);
        actions.add(inventaire);
        actions.add(pause);
    }
    public void action(){
        double vitesseX = 0;
        if (gauche) vitesseX -= (this).getVitesseX();
        if (droite) vitesseX += (this).getVitesseX();

        if (!auSol) {
            vitesseY += GRAVITE;
            if (vitesseY > 8.0) vitesseY = 8.0;
        }

        if (haut && auSol) {
            vitesseY = SAUT;
        }

        double oldX = x.get();
        double oldY = y.get();

        if (vitesseX != 0) {
            x.set(oldX + vitesseX);
            if (collision) {
                x.set(oldX);
            }
        }

        if (vitesseY != 0) {
            y.set(oldY + vitesseY);
            if (collision) {
                y.set(oldY);
                vitesseY = 0;
            }
        }
    }

    public InventaireJoueur getInv() {
        return inv;
    }
    public ArrayList<Boolean> getActions() {
        return actions;
    }
}
