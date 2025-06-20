package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public abstract class EnnemiVolant extends Ennemi {
    private double altitudeCible = 120; // position centrale pour oscillation
    private double amplitudeVol = 100; // variation maximale autour de la position centrale


    public EnnemiVolant(int pv, int v, int degats, int range, Environement env, Hero hero, double x, double y, int cooldown) {
        super(pv, v, degats, range, env, hero, x, y, cooldown);
    }

    public  void deplacement() {
        switch (getMode()) {
            case MODE_INACTIF:
                // Ne bouge pas
                break;

            case MODE_AGGRESSIF:
                deplacementAggressif();
                break;

            case MODE_PATROUILLE:
                deplacementPatrouille();
                break;
        }

        changerModeAutomatiquement();
    }


@Override
public void deplacementPatrouille() {
    double distanceHero = Math.abs(getPosX() - hero.getPosX());

    // Si le héros est proche ET vivant, passer en mode agressif
    if (hero.getPv() > 0 && distanceHero < getDistanceDetection()) {
        changerMode(MODE_AGGRESSIF);
        return;
    }

    // Patrouiller autour du point central
    double distanceZone = Math.abs(getPosX() - getZonePatrouilleX());

    if (distanceZone > getRayonPatrouille()) {
        // Retourner vers le centre
        if (getPosX() < getZonePatrouilleX()) {
            deplacerHorizontalement(vitesseX * 0.8); // Vitesse normale en patrouille
        } else {
            deplacerHorizontalement(-vitesseX * 0.8);
        }
    } else {
        // Patrouiller dans la direction actuelle
        if (getDirectionPatrouille()) {
            deplacerHorizontalement(vitesseX * 0.8);
            if (getPosX() >= getZonePatrouilleX() + getRayonPatrouille()) {
                setDirectionPatrouille(false);
            }
        } else {
            deplacerHorizontalement(-vitesseX * 0.8);
            if (getPosX() <= getZonePatrouilleX() - getRayonPatrouille()) {
                setDirectionPatrouille(true);
            }
        }
    }

    // Oscillation verticale douce en patrouille
    deplacementVerticalPatrouille();
}
    private void deplacementVerticalPatrouille() {
        // Simple oscillation haut/bas
        if (getPosY() < altitudeCible - amplitudeVol) {
            setYProperty(getPosY() + 1); // Monte
        } else if (getPosY() > altitudeCible + amplitudeVol) {
            setYProperty(getPosY() - 1); // Descend
        } else {
            // Mouvement aléatoire léger dans la zone
            if (rdm.nextBoolean()) {
                setYProperty(getPosY() + 1);
            } else {
                setYProperty(getPosY() - 1);
            }
        }
    }

    public void deplacementAggressif()  {
        // Vérifier si le héros est mort
        if (hero.getPv() <= 0) {
            changerMode(MODE_PATROUILLE);
            return;
        }

        // Déplacement horizontal vers le héros
        double distanceX = hero.getPosX() - getPosX();
        if (Math.abs(distanceX) > 5) { // Seuil de proximité
            if (distanceX > 0) {
                deplacerHorizontalement(vitesseX * 1.1); // Plus rapide en mode agressif
            } else {
                deplacerHorizontalement(-vitesseX * 1.1);
            }
        }

        // Déplacement vertical vers le héros
        double distanceY = hero.getPosY() - getPosY();
        double vitesseVerticale = 2.5; //pour descendre plus vite

        if (Math.abs(distanceY) > 10) { // Seuil de proximité vertical
            if (distanceY > 0) {
                setYProperty(getPosY() + vitesseVerticale); // Descendre vers le héros
            } else {
                setYProperty(getPosY() - vitesseVerticale); // Monter vers le héros
            }
        }
    }

    public void deplacementSimple() {
        if (getPosX() < hero.getPosX()) {
            deplacerHorizontalement(vitesseX);
        } else {
            deplacerHorizontalement(-vitesseX);
        }
    }

    @Override
    public void explorerMouvements(Point current, Queue<Point> queue, Set<Point> visited, Map<Point, Point> parent) {
        double dx = hero.getPosX() - getPosX();
        double dy = hero.getPosY() - getPosY();

        double directionX = Math.signum(dx);
        double directionY = Math.signum(dy);

        // Déplacement horizontal
        deplacerHorizontalement(directionX * getVitesseX());

        // Déplacement vertical "volant"
        double nouvelleY = getPosY() + directionY * 1.2;
        setYProperty(nouvelleY);

        updateVisualPosition();
    }



}
