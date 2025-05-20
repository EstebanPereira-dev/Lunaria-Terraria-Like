package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.InventaireJoueur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;

import java.util.ArrayList;

public class Hero extends Acteur {
    private InventaireJoueur inv;
    private ArrayList<Boolean> actions;
    private boolean haut = false, bas = false, droite = false, gauche = false, inventaire = false, pause = false, attaque = false;

    public Hero(Environement env) {
        super(env);
        actions = new ArrayList<>();
        inv = new InventaireJoueur();
        remplirAction();
    }

    public void remplirAction() {
        actions.add(haut);
        actions.add(bas);
        actions.add(droite);
        actions.add(gauche);
        actions.add(inventaire);
        actions.add(pause);
        actions.add(attaque);
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
        attaque();
    }

    public void attaque() {
        attaque = actions.get(6);
        if (attaque) {
            System.out.println("Attaque à mains nues!");

            for (Acteur acteur : getEnv().getActeurs()) {
                if (acteur instanceof Ennemi) {

                    double distanceX = Math.abs(getPosX() - acteur.getPosX());
                    double distanceY = Math.abs(getPosY() - acteur.getPosY());
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                    if (distance <= range) {
                        acteur.setPv(acteur.getPv() - getDegat());
                        System.out.println("Ennemi touché ! Points de vie restants: " + acteur.getPv());

                        if (acteur.getPv() <= 0) {
                            System.out.println("Ennemi vaincu !");
                            acteur.estMort();
                        }
                    }
                }
            }
        }
    }


    public InventaireJoueur getInv() {
        return inv;
    }

    public ArrayList<Boolean> getActions() {
        return actions;
    }

   // public void casserBloc(){
     //   this.getEnv().getTerrain().changerTuile(0,);
    //}
}