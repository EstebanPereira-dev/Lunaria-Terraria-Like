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
    @Override
    public void deplacement() {
        haut = actions.get(0);
        droite = actions.get(2);
        gauche = actions.get(3);

        appliquerGravite();

        if (haut && auSol) {
            vitesseY = SAUT;
            auSol = false;
        }

        deplacerVerticalement();

        double deltaX = 0;
        if (gauche) deltaX -= getVitesseX();
        if (droite) deltaX += getVitesseX();
        deplacerHorizontalement(deltaX);
    }

    public InventaireJoueur getInv() {
        return inv;
    }
    public ArrayList<Boolean> getActions() {
        return actions;
    }
}
