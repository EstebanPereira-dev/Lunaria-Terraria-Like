package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.vue.VueActeur;
import universite_paris8.iut.epereira.lunaria.vue.VueActeurFactory;
import universite_paris8.iut.epereira.lunaria.vue.VueEnnemi;

import java.util.ArrayList;
import java.util.List;

public class GestionBoucle {
    private Environement env;
    private Controleur controleur;

    public GestionBoucle(Environement env, Controleur controleur){
        this.env = env;
        this.controleur = controleur;
    }

    public void miseAJourJeu() {
        // ✅ Supprimer les acteurs morts de la vue
        List<Acteur> acteursASupprimer = env.getActeursASupprimer();
        for (Acteur acteur : acteursASupprimer) {
            VueActeur vueActeur = controleur.getVueActeur(acteur);
            if (vueActeur != null) {
                vueActeur.supprimerActeurVue(acteur);
            }
        }
        env.supprimerActeursMarques();

        env.update();

        // ✅ Ajouter les nouveaux acteurs à la vue avec la factory (initialisation automatique)
        for (Acteur acteur : env.getActeurs()) {
            VueActeur vueActeurExistante = controleur.getVueActeur(acteur);
            if (vueActeurExistante == null) {
                VueActeur nouvelleVue = VueActeurFactory.creerVue(acteur, controleur); // ✅ Initialisation automatique
                controleur.getVuesActeurs().add(nouvelleVue);
            }
        }

        // ✅ Déplacements et animations
        List<Acteur> acteursCopie = new ArrayList<>(env.getActeurs());
        for (Acteur a : acteursCopie) {
            double oldX = a.getPosX();
            a.deplacement();
            double deltaX = a.getPosX() - oldX;

            VueActeur vueActeur = controleur.getVueActeur(a);
            if (vueActeur != null) {
                vueActeur.mettreAJourAnimation(a, deltaX);
            }

            // ✅ Attaque des ennemis avec méthode spécialisée
            if (a instanceof Ennemi) {
                VueActeur vueEnnemi = controleur.getVueActeur(a);
                if (vueEnnemi instanceof VueEnnemi) {
                    ((VueEnnemi) vueEnnemi).attaquer();
                }
            }
        }

        controleur.getBarreDeVieHero().mettreAJour(env.getHero().getPv());
    }
}