package universite_paris8.iut.epereira.lunaria.vue;

import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

public class VueActeurFactory {

    public static VueActeur creerVue(Acteur acteur, Controleur controleur) {
        if (acteur instanceof Hero) {
            return new VueHero((Hero) acteur, controleur);
        } else if (acteur instanceof Ennemi) {
            return new VueEnnemi((Ennemi) acteur, controleur);
        } else {
            // Tous les autres acteurs (moutons, etc.) utilisent le template simple
            return new VueMouton(acteur, controleur);
        }
    }

    public static boolean aVueSpecialisee(Acteur acteur) {
        return acteur instanceof Hero || acteur instanceof Ennemi;
    }
}