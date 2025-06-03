package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.*;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public class Adepte extends Ennemi {

    private static long dernierSpawn = 0;

    // Modes de comportement
    public static final int MODE_INACTIF = 0;    // Ne bouge pas, n'attaque pas
    public static final int MODE_AGGRESSIF = 1;  // Poursuit activement le héros
    public static final int MODE_PATROUILLE = 2; // Patrouille dans une zone et devient agressif si le héros s'approche

    private int mode;
    private long dernierChangementMode;
    private double zonePatrouilleX; // Point central de patrouille
    private double rayonPatrouille = 100; // Rayon de patrouille
    private boolean directionPatrouille = true; // true = droite, false = gauche

    // Pathfinding
    private Queue<Point> chemin;
    private long dernierCalculChemin;
    private static final long INTERVALLE_CALCUL_CHEMIN = 1500; // Recalculer toutes les 1.5s (plus stable)
    private double distanceDetection = 150; // Distance à laquelle l'adepte détecte le héros

    // Classe interne pour représenter un point
    private static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                Point p = (Point) obj;
                return x == p.x && y == p.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public Adepte(int pv, int v, int degat, int range, Environement env, Hero hero, double x, double y) {
        super(pv, v, degat, range, env, hero, x, y);
        this.mode = MODE_PATROUILLE; // Mode par défaut
        this.dernierChangementMode = System.currentTimeMillis();
        this.zonePatrouilleX = x; // La position initiale devient le centre de patrouille
        this.chemin = new LinkedList<>();
        this.dernierCalculChemin = 0;
    }

    @Override
    public void deplacement() {
        appliquerGravite();

        switch (mode) {
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

    private void deplacementAggressif() {
        // Vérifier si le héros est mort
        if (hero.getPv() <= 0) {
            changerMode(MODE_PATROUILLE);
            return;
        }

        long maintenant = System.currentTimeMillis();

        // Recalculer le chemin périodiquement
        if (maintenant - dernierCalculChemin > INTERVALLE_CALCUL_CHEMIN) {
            calculerCheminVersHero();
            dernierCalculChemin = maintenant;
        }

        // Suivre le chemin calculé
        if (!chemin.isEmpty()) {
            Point prochainPoint = chemin.peek();
            double targetX = prochainPoint.x * ConfigurationJeu.TAILLE_TUILE;

            if (Math.abs(getPosX() - targetX) < ConfigurationJeu.TAILLE_TUILE) {
                chemin.poll(); // Point atteint, passer au suivant
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

    private void deplacementPatrouille() {
        double distanceHero = Math.abs(getPosX() - hero.getPosX());

        // Si le héros est proche ET vivant, passer en mode agressif
        if (hero.getPv() > 0 && distanceHero < distanceDetection) {
            changerMode(MODE_AGGRESSIF);
            return;
        }

        // Patrouiller autour du point central
        double distanceZone = Math.abs(getPosX() - zonePatrouilleX);

        if (distanceZone > rayonPatrouille) {
            // Retourner vers le centre
            if (getPosX() < zonePatrouilleX) {
                deplacerHorizontalement(vitesseX);
            } else {
                deplacerHorizontalement(-vitesseX);
            }
        } else {
            // Patrouiller dans la direction actuelle
            if (directionPatrouille) {
                deplacerHorizontalement(vitesseX);
                if (getPosX() >= zonePatrouilleX + rayonPatrouille) {
                    directionPatrouille = false;
                }
            } else {
                deplacerHorizontalement(-vitesseX);
                if (getPosX() <= zonePatrouilleX - rayonPatrouille) {
                    directionPatrouille = true;
                }
            }
        }

        // Sauter parfois pendant la patrouille
        if (auSol && rdm.nextInt(300) <= 1) {
            vitesseY = SAUT;
        }
    }

    private void deplacementSimple() {
        if (getPosX() < hero.getPosX()) {
            deplacerHorizontalement(vitesseX);
        } else {
            deplacerHorizontalement(-vitesseX);
        }

        if (auSol && rdm.nextInt(250) <= 1) {
            vitesseY = SAUT;
        }
    }

    private void calculerCheminVersHero() {
        int startX = (int) (getPosX() / ConfigurationJeu.TAILLE_TUILE);
        int startY = (int) (getPosY() / ConfigurationJeu.TAILLE_TUILE);
        int targetX = (int) (hero.getPosX() / ConfigurationJeu.TAILLE_TUILE);
        int targetY = (int) (hero.getPosY() / ConfigurationJeu.TAILLE_TUILE);

        chemin = bfs(startX, startY, targetX, targetY);
    }

    private Queue<Point> bfs(int startX, int startY, int targetX, int targetY) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Map<Point, Point> parent = new HashMap<>();

        // Ajuster les positions pour qu'elles soient au sol
        startY = trouverSolY(startX);
        targetY = trouverSolY(targetX);

        Point start = new Point(startX, startY);
        Point target = new Point(targetX, targetY);

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // Si on est assez proche du target (même colonne ou adjacente)
            if (Math.abs(current.x - target.x) <= 1) {
                return reconstruireChemin(parent, start, current);
            }

            // Explorer les mouvements horizontaux possibles
            explorerMouvements(current, queue, visited, parent);
        }

        return new LinkedList<>(); // Aucun chemin trouvé
    }

    private void explorerMouvements(Point current, Queue<Point> queue, Set<Point> visited, Map<Point, Point> parent) {
        // Mouvements horizontaux (gauche et droite)
        int[] directions = {-1, 1};

        for (int dir : directions) {
            int newX = current.x + dir;

            // Vérifier les limites
            if (newX < 0 || newX >= getEnv().getTerrain().getTableau()[0].length) {
                continue;
            }

            // Trouver le sol à cette position X
            int solY = trouverSolY(newX);

            // Si on peut atteindre cette position (pas trop haut ou trop bas)
            int diffY = Math.abs(solY - current.y);
            if (diffY <= 3) { // Peut sauter/tomber de maximum 3 blocs
                Point next = new Point(newX, solY);

                if (!visited.contains(next)) {
                    visited.add(next);
                    parent.put(next, current);
                    queue.offer(next);
                }
            }
        }
    }

    private int trouverSolY(int x) {
        int[][] terrain = getEnv().getTerrain().getTableau();

        // Chercher le premier bloc tangible depuis le haut
        for (int y = 0; y < terrain.length; y++) {
            if (getEnv().getTerrain().estTangible(x, y)) {
                return Math.max(0, y - 1); // Position juste au-dessus du bloc
            }
        }

        return terrain.length - 1; // Si pas de sol, retourner le bas
    }

    private Queue<Point> reconstruireChemin(Map<Point, Point> parent, Point start, Point target) {
        LinkedList<Point> chemin = new LinkedList<>();
        Point current = target;

        while (current != null && !current.equals(start)) {
            chemin.addFirst(current);
            current = parent.get(current);
        }

        return chemin;
    }

    private boolean doitSauter(Point prochainPoint) {
        // Calculer la position actuelle en tuiles
        int currentTileY = (int) (getPosY() / ConfigurationJeu.TAILLE_TUILE);

        // Si le prochain point est plus haut, on doit sauter
        if (prochainPoint.y < currentTileY) {
            return true;
        }

        // Vérifier s'il y a un obstacle devant nous qui nécessite un saut
        int direction = (getPosX() < prochainPoint.x * ConfigurationJeu.TAILLE_TUILE) ? 1 : -1;
        int nextX = (int) (getPosX() / ConfigurationJeu.TAILLE_TUILE) + direction;

        // Si il y a un mur devant et qu'on est au sol, sauter
        if (getEnv().getTerrain().estTangible(nextX, currentTileY)) {
            return true;
        }

        return false;
    }

    private void changerModeAutomatiquement() {
        long maintenant = System.currentTimeMillis();
        double distanceHero = Math.abs(getPosX() - hero.getPosX());

        // Si le héros est mort, tous les adeptes passent en mode patrouille
        if (hero.getPv() <= 0 && mode != MODE_PATROUILLE) {
            changerMode(MODE_PATROUILLE);
            return;
        }

        switch (mode) {
            case MODE_AGGRESSIF:
                // Retourner en patrouille si le héros est trop loin
                if (distanceHero > distanceDetection * 1.5) {
                    changerMode(MODE_PATROUILLE);
                }
                break;

            case MODE_PATROUILLE:
                // Devenir inactif parfois (5% de chance toutes les 10 secondes)
                if (maintenant - dernierChangementMode > 10000 && rdm.nextInt(100) < 5) {
                    changerMode(MODE_INACTIF);
                }
                break;

            case MODE_INACTIF:
                // Reprendre la patrouille après un certain temps ou si le héros s'approche
                if (maintenant - dernierChangementMode > 5000 ||
                        (hero.getPv() > 0 && distanceHero < distanceDetection)) {
                    changerMode(MODE_PATROUILLE);
                }
                break;
        }
    }

    private void changerMode(int nouveauMode) {
        this.mode = nouveauMode;
        this.dernierChangementMode = System.currentTimeMillis();

        if (nouveauMode == MODE_AGGRESSIF) {
            this.chemin.clear();
        }
    }

    @Override
    public void agit() {
        if (mode == MODE_INACTIF) {
            return;
        }

        double distanceX = Math.abs(getPosX() - hero.getPosX());
        double distanceY = Math.abs(getPosY() - hero.getPosY());
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance <= range) {
            int degatsInfliges = getDegat();

            if (mode == MODE_AGGRESSIF) {
                degatsInfliges = (int) (degatsInfliges * 1.2);
            }

            hero.setPv(hero.getPv() - degatsInfliges);
            String modeString = getModeString(mode);
            System.out.println("Hero touché par Adepte (" + modeString + ") ! Dégâts: " + degatsInfliges +
                    ", Points de vie restants: " + hero.getPv());

            if (hero.getPv() <= 0) {
                System.out.println("Hero vaincu !");
                hero.estMort();
            }
        }
    }

    @Override
    public boolean conditionApparation() {
        long maintenant = System.currentTimeMillis();
        return (maintenant - dernierSpawn) > 90000 &&
                rdm.nextInt(100) < 30; // 30% de chance
    }

    @Override
    public void spawner() {
        if (!conditionApparation()) return;

        double x = rdm.nextDouble() * getEnv().getWidth();
        double y = trouverHauteurSol(x) - 20;
        int pv = 15 + rdm.nextInt(10);
        int vitesse = 1 + rdm.nextInt(2);
        int degats = 8 + rdm.nextInt(5);

        Adepte nouvelAdepte = new Adepte(pv, vitesse, degats, range, getEnv(), hero, x, y);
        getEnv().ajouter(nouvelAdepte);
        dernierSpawn = System.currentTimeMillis();
    }

    private double trouverHauteurSol(double x) {
        int tileX = (int) (x / ConfigurationJeu.TAILLE_TUILE);
        int[][] terrain = getEnv().getTerrain().getTableau();

        for (int tileY = 0; tileY < terrain.length; tileY++) {
            if (getEnv().getTerrain().estTangible(tileX, tileY)) {
                return tileY * ConfigurationJeu.TAILLE_TUILE;
            }
        }

        return getEnv().getHeight();
    }

    // Getters et setters
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        changerMode(mode);
    }

    private String getModeString(int mode) {
        switch (mode) {
            case MODE_INACTIF: return "INACTIF";
            case MODE_AGGRESSIF: return "AGGRESSIF";
            case MODE_PATROUILLE: return "PATROUILLE";
            default: return "INCONNU";
        }
    }

    public void setDistanceDetection(double distance) {
        this.distanceDetection = distance;
    }

    public void setRayonPatrouille(double rayon) {
        this.rayonPatrouille = rayon;
    }
}