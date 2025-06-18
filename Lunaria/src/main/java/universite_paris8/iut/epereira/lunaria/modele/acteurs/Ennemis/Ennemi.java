package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.*;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public abstract class Ennemi extends Acteur {

    protected Hero hero;
    protected int degat, range;
    private int cooldown; //5 pour l'adepte
    // Modes de comportement
    public static final int MODE_INACTIF = 0;    // Ne bouge pas, n'attaque pas
    public static final int MODE_AGGRESSIF = 1;  // Poursuit activement le héros
    public static final int MODE_PATROUILLE = 2; // Patrouille dans une zone et devient agressif si le héros s'approche

    private int mode;
    private long dernierChangementMode;
    private double zonePatrouilleX; // Point central de patrouille
    private double rayonPatrouille = 100; // Rayon de patrouille
    private boolean directionPatrouille = true; // true = droite, false = gauche

    private static long dernierSpawn = 0;

    private Queue<Point> chemin;
    private long dernierCalculChemin;
    private static final long INTERVALLE_CALCUL_CHEMIN = 1500; // Recalculer toutes les 1.5s (plus stable)
    private double distanceDetection = 150;


    public Ennemi(int pv, int v, int degat, int range, Environement env, Hero hero, double x, double y, int cooldown) {
        super(pv, v, env, x, y);
        this.degat = degat;
        this.range = range;
        this.hero = hero;
        this.mode = MODE_PATROUILLE; // Mode par défaut
        this.dernierChangementMode = System.currentTimeMillis();
        this.zonePatrouilleX = x; // La position initiale devient le centre de patrouille
        this.chemin = new LinkedList<>();
        this.dernierCalculChemin = 0;
        this.cooldown=cooldown;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            if (getPosX() == p.x && getPosY() == p.y)
                return true;
        }
        return false;
    }

    public abstract void deplacement();
    
    public abstract void deplacementAggressif();

    public abstract void deplacementPatrouille();

    public abstract  void deplacementSimple();

    public void calculerCheminVersHero() {
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

    public abstract void explorerMouvements(Point current, Queue<Point> queue, Set<Point> visited, Map<Point, Point> parent);


    private Queue<Point> reconstruireChemin(Map<Point, Point> parent, Point start, Point target) {
        LinkedList<Point> chemin = new LinkedList<>();
        Point current = target;

        while (current != null && !current.equals(start)) {
            chemin.addFirst(current);
            current = parent.get(current);
        }

        return chemin;
    }



    public void changerModeAutomatiquement() {
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

    public void changerMode(int nouveauMode) {
        this.mode = nouveauMode;
        this.dernierChangementMode = System.currentTimeMillis();

        if (nouveauMode == MODE_AGGRESSIF) {
            this.chemin.clear();
        }
    }

    @Override
    public void agit() {
        if (mode == MODE_INACTIF || attackOnCooldown) {
            return;
        }
        // Calcul de la position du hero
        double distanceX = Math.abs(getPosX() - hero.getPosX());
        double distanceY = Math.abs(getPosY() - hero.getPosY());
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        // Vérifier si le héros est à portée
        if (distance <= range) {
            // Calculer les dégâts avec bonus éventuel
            int degatsInfliges = getDegat();
            if (mode == MODE_AGGRESSIF) {
                degatsInfliges = (int) (degatsInfliges * 1.2);
            }

            // Infliger les dégâts au héros
            hero.setPv(hero.getPv() - degatsInfliges);

            // Vérifier si le héros est vaincu
            if (hero.getPv() <= 0) {
                hero.estMort();
            }
            // Déclencher le cooldown d'attaque
            demarrerCooldownAttaque();
        }
    }

    private void demarrerCooldownAttaque(){
        attackOnCooldown = true;

        final Acteur finalActeur = this;
        Timeline attackCooldownTimer = new Timeline(
                new KeyFrame(Duration.seconds(cooldown), e -> {
                    if (!getEnv().getActeursASupprimer().contains(finalActeur)) {
                        finalActeur.attackOnCooldown = false;
                        System.out.println("Ennemi peut attaquer à nouveau");
                    }
                })
        );
        attackCooldownTimer.setCycleCount(1);
        attackCooldownTimer.play();
    }


    public int trouverSolY(int x) {
        ObservableList<Integer> terrain = getEnv().getTerrain().getTableau();

        // Chercher le premier bloc tangible depuis le haut
        for (int y = 0; y < terrain.size(); y++) {
            if (getEnv().getTerrain().estTangible(x, y)) {
                return Math.max(0, y - 1); // Position juste au-dessus du bloc
            }
        }

        return terrain.size() - 1; // Si pas de sol, retourner le bas
    }


    public abstract boolean conditionApparation();

    public abstract void spawner();


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
    public int getDegat() {
        return degat;
    }

    public Hero getHero() {
        return hero;
    }

    public int getRange() {
        return range;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getRayonPatrouille() {
        return rayonPatrouille;
    }

    public double getDistanceDetection() {
        return distanceDetection;
    }

    public double getZonePatrouilleX() {
        return zonePatrouilleX;
    }
    public boolean getDirectionPatrouille(){
        return this.directionPatrouille;
    }

    public void setDirectionPatrouille(boolean directionPatrouille) {
        this.directionPatrouille = directionPatrouille;
    }

    public long getDernierCalculChemin() {
        return dernierCalculChemin;
    }

    public static long getIntervalleCalculChemin() {
        return INTERVALLE_CALCUL_CHEMIN;
    }

    public void setDernierCalculChemin(long dernierCalculChemin) {
        this.dernierCalculChemin = dernierCalculChemin;
    }

    public Queue<Point> getChemin() {
        return chemin;
    }
}