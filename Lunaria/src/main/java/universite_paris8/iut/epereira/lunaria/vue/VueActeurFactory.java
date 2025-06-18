package universite_paris8.iut.epereira.lunaria.vue;

import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Aigle;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif.Mouton;

public class VueActeurFactory {

    public static VueActeur creerVue(Acteur acteur, Controleur controleur) {
        if (acteur instanceof Hero) {
            return new VueHero((Hero) acteur, controleur);
        } else if (acteur instanceof Ennemi) {
            if(acteur instanceof Aigle)
                return new VueAigle((Ennemi) acteur, controleur);
            else
                return new VueAdepte((Ennemi) acteur, controleur);
        } else if (acteur instanceof Mouton){
            return new VueMouton(acteur, controleur);
        }
        else
            return new VuePNJ(acteur, controleur);
    }

    public static boolean aVueSpecialisee(Acteur acteur) {
        return acteur instanceof Hero || acteur instanceof Ennemi;
    }
}