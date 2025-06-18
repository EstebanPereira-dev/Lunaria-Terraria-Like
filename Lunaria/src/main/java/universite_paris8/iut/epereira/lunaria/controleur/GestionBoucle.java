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
import universite_paris8.iut.epereira.lunaria.vue.VueAdepte;

import java.util.ArrayList;
import java.util.List;

public class GestionBoucle {
    private Environement env;
    private Controleur controleur;
    //game loop
    private Timeline gameLoop;

    private int compteurFrames = 0;
    private static final int FRAMES_PAR_SECONDE = 1000 / 15; // 15ms par frame = ~66.67 FPS
    private static final int FRAMES_POUR_60_SECONDES = FRAMES_PAR_SECONDE * 60;


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

        int i = 0;
        while (i < env.getActeurs().size()) {
            Acteur a = env.getActeurs().get(i);
            List<Acteur> acteursCopie = new ArrayList<>(env.getActeurs());
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
            i++;
        }

        controleur.getEcu().setText(env.getHero().getEcu()+"");
        controleur.getBarreDeVieHero().mettreAJour(env.getHero().getPv(),env.getHero().getFaim());


        controleur.getBarreDeVieHero().mettreAJour(env.getHero().getPv(),env.getHero().getFaim());
        compteurFrames++;
        if (compteurFrames >= FRAMES_POUR_60_SECONDES) {
            this.env.changerEtatJour();
            compteurFrames = 0; // Reset du compteur
        }

       this.controleur.getVueEnvironnement().setBackground();
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