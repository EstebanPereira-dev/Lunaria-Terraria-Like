package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.vue.VueActeur;

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

    //gere ce qui se passe toute les tick
    public void miseAJourJeu() {
        // Supprimer les acteurs morts de la vue
        List<Acteur> acteursASupprimer = env.getActeursASupprimer();
        for (Acteur acteur : acteursASupprimer) {
            VueActeur vueActeur = controleur.getVueActeur(acteur);
            if (vueActeur != null) {
                vueActeur.supprimerActeurVue(acteur);
            }
        }
        env.supprimerActeursMarques();

        env.update();

        // Ajouter les nouveaux acteurs à la vue
        for (Acteur acteur : env.getActeurs()) {
            VueActeur vueActeurExistante = controleur.getVueActeur(acteur);
            if (vueActeurExistante == null) {
                VueActeur nouvelleVue = new VueActeur(acteur, controleur);
                controleur.getVuesActeurs().add(nouvelleVue);
            }
        }

        List<Acteur> acteursCopie = new ArrayList<>(env.getActeurs());
        for (Acteur a : acteursCopie) {
            double oldX = a.getPosX();
            a.deplacement();
            double deltaX = a.getPosX() - oldX;

            VueActeur vueActeur = controleur.getVueActeur(a);
            if (vueActeur != null) {
                vueActeur.mettreAJourAnimation(a, deltaX);
            }

            if (a instanceof Ennemi && !a.attackOnCooldown) {
                a.agit();
                a.attackOnCooldown = true;

                final Acteur finalActeur = a;
                Timeline attackCooldownTimerEnnemi = new Timeline(
                        new KeyFrame(Duration.seconds(5), e -> {
                            if (!env.getActeursASupprimer().contains(finalActeur)) {
                                finalActeur.attackOnCooldown = false;
                            }
                        })
                );
                attackCooldownTimerEnnemi.setCycleCount(1);
                attackCooldownTimerEnnemi.play();
            }
        }

        controleur.getBarreDeVieHero().mettreAJour(env.getHero().getPv());
    }
}