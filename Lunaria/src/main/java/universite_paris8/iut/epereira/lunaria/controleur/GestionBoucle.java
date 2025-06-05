package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.vue.VueActeur;
import universite_paris8.iut.epereira.lunaria.vue.VueActeurFactory;

import java.util.ArrayList;
import java.util.List;

public class GestionBoucle {
    private Environement env;
    private Controleur controleur;
    //game loop
    private Timeline gameLoop;

    public GestionBoucle(Environement env, Controleur controleur){
        this.env = env;
        this.controleur = controleur;
    }
    //création de la boucle de jeux
    public void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(15), e -> miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }
    public void miseAJourJeu() {
        List<Acteur> acteursASupprimer = env.getActeursASupprimer();
        for (Acteur acteur : acteursASupprimer) {
            VueActeur vueActeur = controleur.getVueActeur(acteur);
            if (vueActeur != null) {
                vueActeur.supprimerActeurVue(acteur);
            }
        }
        env.supprimerActeursMarques();

        env.update();

        for (Acteur acteur : env.getActeurs()) {
            VueActeur vueActeurExistante = controleur.getVueActeur(acteur);
            if (vueActeurExistante == null) {
                VueActeur nouvelleVue = VueActeurFactory.creerVue(acteur, controleur);
                controleur.getVuesActeurs().add(nouvelleVue);
            }
        }

        List<Acteur> acteursCopie = new ArrayList<>(env.getActeurs()); // A modifi utiliser une boucle while a la place et observable list
        for (Acteur a : acteursCopie) {
            double oldX = a.getPosX();
            a.deplacement();
            double deltaX = a.getPosX() - oldX;

            VueActeur vueActeur = controleur.getVueActeur(a);
            if (vueActeur != null) {
                vueActeur.mettreAJourAnimation(a, deltaX);
            }
            if (a instanceof Ennemi) {
                a.agit();
            }
        }

        controleur.getBarreDeVieHero().mettreAJour(env.getHero().getPv());
    }
    // Démarrage
    public void demarrer() {
        gameLoop.play();
    }

    // Pause
    public void arreter() {
        gameLoop.stop();
    }
}