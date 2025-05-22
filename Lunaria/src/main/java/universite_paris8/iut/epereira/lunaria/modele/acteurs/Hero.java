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
    private int range, degat;

    public Hero(Environement env) {
        super(env);
        actions = new ArrayList<>();
        inv = new InventaireJoueur();
        remplirAction();
        range = 5;
        degat = 10;
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
    }

    @Override
    public void agit() {
        attaque = actions.get(6);
        if (attaque) {
            System.out.println("Attaque à mains nues!");

            // Créer une liste temporaire pour stocker les ennemis qui doivent être tués
            ArrayList<Acteur> ennemisASupprimer = new ArrayList<>();

            // Parcourir tous les acteurs pour trouver les ennemis à portée
            for (Acteur acteur : getEnv().getActeurs()) {
                if (acteur instanceof Ennemi) {
                    double distanceX = Math.abs(getPosX() - acteur.getPosX());
                    double distanceY = Math.abs(getPosY() - acteur.getPosY());
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                    if (distance <= range) {
                        acteur.setPv(acteur.getPv() - degat);
                        System.out.println("Ennemi touché ! Points de vie restants: " + acteur.getPv());

                        if (acteur.getPv() <= 0) {
                            System.out.println("Ennemi vaincu !");
                            // Au lieu d'appeler estMort() ici, ajoutez l'ennemi à la liste temporaire
                            ennemisASupprimer.add(acteur);
                        }
                    }
                }
            }

            // Maintenant, marquer les ennemis pour suppression en dehors de la boucle
            for (Acteur ennemi : ennemisASupprimer) {
                ennemi.estMort();
            }

            // Réinitialiser immédiatement l'action d'attaque pour éviter le spam
            actions.set(6, false);
        }
    }

    public InventaireJoueur getInv() {
        return inv;
    }
    public int getRange() {
        return range;
    }

    public int getDegat() {
        return degat;
    }

    public ArrayList<Boolean> getActions() {
        return actions;
    }

   // public void casserBloc(){
     //   this.getEnv().getTerrain().changerTuile(0,);
    //}
}