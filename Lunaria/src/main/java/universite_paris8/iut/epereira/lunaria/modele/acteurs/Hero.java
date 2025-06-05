package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
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

    public boolean peutAttaquer() {
        return !attackOnCooldown;
    }

    public boolean executerAttaque() {
        if (!peutAttaquer()) {
            System.out.println("COOLDOWN");
            return false;
        }

        // Marquer le début du cooldown - L'attaque se lance toujours
        attackOnCooldown = true;

        // Logique d'attaque - vérifier s'il y a des ennemis touchés
        boolean ennemisATouches = false;
        ArrayList<Acteur> ennemisASupprimer = new ArrayList<>();

        for (Acteur acteur : getEnv().getActeurs()) {
            if (acteur instanceof Ennemi) {
                double distanceX = Math.abs(getPosX() - acteur.getPosX());
                double distanceY = Math.abs(getPosY() - acteur.getPosY());
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (distance <= range) {
                    acteur.setPv(acteur.getPv() - degat);
                    System.out.println("Ennemi touché ! Points de vie restants: " + acteur.getPv());
                    ennemisATouches = true;

                    if (acteur.getPv() <= 0) {
                        System.out.println("Ennemi vaincu !");
                        ennemisASupprimer.add(acteur);
                    }
                }
            }
        }

        // Supprimer les ennemis morts
        for (Acteur ennemi : ennemisASupprimer) {
            ennemi.estMort();
        }

        // Message si aucun ennemi touché
        if (!ennemisATouches) {
            System.out.println("Attaque dans le vide !");
        }

        // Démarrer le cooldown
        demarrerCooldown();

        // Retourne toujours true car l'attaque s'est lancée
        return true;
    }

    private void demarrerCooldown() {
        Timeline cooldownTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    this.attackOnCooldown = false;
                    System.out.println("Attaque dispo");
                })
        );
        cooldownTimer.setCycleCount(1);
        cooldownTimer.play();
    }
    @Override
    public void agit() {

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