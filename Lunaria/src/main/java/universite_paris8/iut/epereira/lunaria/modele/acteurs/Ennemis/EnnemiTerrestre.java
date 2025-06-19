package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import javafx.collections.ObservableList;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public abstract class EnnemiTerrestre extends Ennemi {
    public EnnemiTerrestre(int pv, int v, int degats, int range, Environement env, Hero hero, double x, double y, int cooldown) {
        super(pv, v, degats, range, env, hero, x, y, cooldown);
    }
    public void deplacement() {
        appliquerGravite();

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

        deplacerVerticalement();
        changerModeAutomatiquement();
    }

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
                deplacerHorizontalement(vitesseX);
            } else {
                deplacerHorizontalement(-vitesseX);
            }
        } else {
            // Patrouiller dans la direction actuelle
            if (getDirectionPatrouille()) {
                deplacerHorizontalement(vitesseX);
                if (getPosX() >= getZonePatrouilleX() + getRayonPatrouille()) {
                    setDirectionPatrouille(false);  // Si l'ennemi est trop loin du centre, il revient vers ce centre
                }
            } else {
                deplacerHorizontalement(-vitesseX);
                if (getPosX() <= getZonePatrouilleX() - getRayonPatrouille()) {
                    setDirectionPatrouille(true);// Sinon, il patrouille dans la direction actuelle, et inversera la direction s’il atteint le bord de la zone
                }
            }
        }

        // Sauter parfois pendant la patrouille
        if (auSol && rdm.nextInt(300) <= 1) {
            vitesseY = SAUT;
        }
    }

    public void deplacementAggressif() {
        // Vérifier si le héros est mort
        if (hero.getPv() <= 0) {
            changerMode(MODE_PATROUILLE);
            return;
        }

        long maintenant = System.currentTimeMillis();

        if (maintenant - getDernierCalculChemin() > getIntervalleCalculChemin()) {//On vérifie si assez de temps c'est écoulé depuis le dernier calcul du chemin pour éviter de faire trop de calculs
            calculerCheminVersHero();
            setDernierCalculChemin(maintenant);
        }

        // Suivre le chemin calculé
        if (!getChemin().isEmpty()) {
            Point prochainPoint = getChemin().peek(); // peek donne le prochain point sans l'enlever
            double targetX = prochainPoint.x * ConfigurationJeu.TAILLE_TUILE; //On convertit les coordonnéees en pixels

            if (Math.abs(getPosX() - targetX) < ConfigurationJeu.TAILLE_TUILE) {
                getChemin().poll(); // Point atteint, passer au suivant
            } else {
                // Se déplacer vers le point
                if (getPosX() < targetX) {
                    deplacerHorizontalement(vitesseX);
                } else {
                    deplacerHorizontalement(-vitesseX);
                }
            }

            // Sauter si nécessaire pour atteindre une plateforme plus haute
            if (auSol && doitSauter(prochainPoint)) {
                vitesseY = SAUT;
            }
        } else {
            // Pas de chemin, déplacement simple vers le héros
            deplacementSimple();
        }
    }

    public void deplacementSimple() {
        if (getPosX() < hero.getPosX()) {
            deplacerHorizontalement(vitesseX);
        } else {
            deplacerHorizontalement(-vitesseX);
        }

        if (auSol && rdm.nextInt(250) <= 1) {
            vitesseY = SAUT;
        }
    }

    private boolean doitSauter(Point prochainPoint) {
        // Calculer la position actuelle en tuiles
        int currentTileY = (int) (getPosY() / ConfigurationJeu.TAILLE_TUILE);

        // Si le prochain point est plus haut, on doit sauter
        if (prochainPoint.y < currentTileY) {
            return true;
        }
        int direction;

        // Vérifier s'il y a un obstacle devant nous qui nécessite un saut
        if (getPosX() < prochainPoint.x * ConfigurationJeu.TAILLE_TUILE)
             direction = 1;
        else
             direction = -1;
        int nextX = (int) (getPosX() / ConfigurationJeu.TAILLE_TUILE) + direction;

        // Si il y a un mur devant et qu'on est au sol, sauter
        if (getEnv().getTerrain().estTangible(nextX, currentTileY)) {
            return true;
        }

        return false;
    }



    public double trouverHauteurSol(double x) { //trouve pour une colonne sa première tuile solide
        int tileX = (int) (x / ConfigurationJeu.TAILLE_TUILE); //conversion de pos(x) en id de tuile
        ObservableList<Integer> terrain = getEnv().getTerrain().getTableau();

        for (int tileY = 0; tileY < terrain.size(); tileY++) {  // parcours la map (vertical) pour trouver la premiere tuile solide
            if (getEnv().getTerrain().estTangible(tileX, tileY)) {
                return tileY * ConfigurationJeu.TAILLE_TUILE;
            }
        }
        return getEnv().getHeight();
    }

    @Override
    public void explorerMouvements(Point current, Queue<Point> queue, Set<Point> visited, Map<Point, Point> parent){
            // Mouvements horizontaux (gauche et droite)
            int[] directions = {-1, 1};

            for (int dir : directions) {
                int newX = current.x + dir;

                // Vérifier les limites
                if (newX < 0 || newX >= getEnv().getTerrain().getTableau().size()){
                    continue;
                }

                // Trouver le sol à cette position X
                int solY = trouverSolY(newX);

                // Si on peut atteindre cette position (vérifier que le saut ou la chute est possible (max 3 blocs))
                int diffY = Math.abs(solY - current.y);
                if (diffY <= 3) { // Peut sauter/tomber de maximum 3 blocs
                    Point next = new Point(newX, solY);

                    if (!visited.contains(next)) {
                        visited.add(next);
                        parent.put(next, current); // Pour reconstituer le chemin plus tard
                        queue.offer(next); // Ajouter à explorer
                    }
                }
            }
    }



}
