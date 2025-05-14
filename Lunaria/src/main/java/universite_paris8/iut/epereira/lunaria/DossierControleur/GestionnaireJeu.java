package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.HashMap;
import java.util.Map;

public class GestionnaireJeu {

    private TextArea pauseID;
    private final Pane zoneJeu;
    private final Environement environnement;
    private final Map<Acteur, Circle> sprites = new HashMap<>();
    private final Acteur hero;
    private Terrain terrain;

    private double vitesseY = 0;
    private final double GRAVITE = 0.2;
    private final double SAUT = -5;
    private final int TAILLE_TUILE = 32;

    private boolean toucheGauche = false;
    private boolean toucheDroite = false;
    private boolean toucheEspace = false;

    private boolean jeuEnPause = false;

    private Timeline gameLoop;

    public GestionnaireJeu(Pane zoneJeu, TextArea pauseID) {
        this.zoneJeu = zoneJeu;
        this.environnement = new Environement(800, 608);
        this.terrain = new Terrain(25, 19); // Création du terrain
        this.hero = new Hero(environnement);
        this.pauseID = pauseID;

        Circle heroSprite = creerSprite(hero);
        sprites.put(hero, heroSprite);
        zoneJeu.getChildren().add(heroSprite);
        environnement.ajouter(hero);

        configurerEvenements();
        creerBoucleDeJeu();
    }

    // Configuration des événements
    private void configurerEvenements() {
        zoneJeu.setFocusTraversable(true);
        zoneJeu.setOnKeyPressed(this::gererTouchePressee);
        zoneJeu.setOnKeyReleased(this::gererToucheRelachee);
    }

    private void gererTouchePressee(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                toucheEspace = true;
                break;
            case Q:
                toucheGauche = true;
                break;
            case D:
                toucheDroite = true;
                break;
            case ESCAPE:
                jeuEnPause = !jeuEnPause;
                if (jeuEnPause) {
                    pauseID.setVisible( true );
                    arreter();
                } else {
                    demarrer();
                    pauseID.setVisible( false );
                }
                break;
        }
    }

    private void gererToucheRelachee(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                toucheEspace = false;
                break;
            case Q:
                toucheGauche = false;
                break;
            case D:
                toucheDroite = false;
                break;
        }
    }

    private void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(16), e -> miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    private boolean estEnCollisionDirection(Acteur acteur, int tailleTuile, int directionX, int directionY) {
        double centreX = acteur.x.get();
        double centreY = acteur.y.get();
        double rayon = 10;

        // Calculer la position du point dans la direction spécifiée
        double pointX = centreX + directionX * rayon;
        double pointY = centreY + directionY * rayon;

        // Convertir en indices de tuile
        int tuileX = (int) (pointX / tailleTuile);
        int tuileY = (int) (pointY / tailleTuile);

        return terrain.estTangible(tuileY, tuileX);
    }

    private void miseAJourJeu() {
        // Mettre à jour le sprite visuel
        Circle heroSprite = sprites.get(hero);
        if (heroSprite != null) {
            heroSprite.setTranslateX(hero.x.get());
            heroSprite.setTranslateY(hero.y.get());
        }

        // Vérifier si au sol
        boolean auSol = estAuSol();

        // Appliquer la gravité
        if (!auSol) {
            vitesseY += GRAVITE;
            if (vitesseY > 8.0) vitesseY = 8.0; // Limiter la vitesse de chute
        } else if (vitesseY > 0) {
            vitesseY = 0;
        }

        if (toucheEspace && auSol) {
            vitesseY = SAUT;
        }

        double oldX = hero.x.get();
        double oldY = hero.y.get();

        double vitesseX = 0;
        if (toucheGauche) vitesseX -= ((Hero)hero).getV();
        if (toucheDroite) vitesseX += ((Hero)hero).getV();

        if (vitesseX != 0) {
            hero.x.set(oldX + vitesseX);
            if (estEnCollision()) {
                hero.x.set(oldX);
            }
        }

        if (vitesseY != 0) {
            hero.y.set(oldY + vitesseY);
            if (estEnCollision()) {
                hero.y.set(oldY);
                vitesseY = 0;
            }
        }
    }

    private boolean estEnCollision() {

        double centreX = hero.x.get();
        double centreY = hero.y.get();
        double rayon = 10;

        int minTileX = Math.max(0, (int)((centreX - rayon) / TAILLE_TUILE));
        int maxTileX = Math.min(terrain.getWidth() - 1, (int)((centreX + rayon) / TAILLE_TUILE));
        int minTileY = Math.max(0, (int)((centreY - rayon) / TAILLE_TUILE));
        int maxTileY = Math.min(terrain.getHeight() - 1, (int)((centreY + rayon) / TAILLE_TUILE));

        for (int y = minTileY; y <= maxTileY; y++) {
            for (int x = minTileX; x <= maxTileX; x++) {
                if (terrain.getTerrain()[y][x] != 0) { // 0 = CIEL
                    double tuileCentreX = x * TAILLE_TUILE + TAILLE_TUILE/2;
                    double tuileCentreY = y * TAILLE_TUILE + TAILLE_TUILE/2;

                    double distX = Math.abs(centreX - tuileCentreX);
                    double distY = Math.abs(centreY - tuileCentreY);

                    if (distX < (TAILLE_TUILE/2 + rayon - 2) &&
                            distY < (TAILLE_TUILE/2 + rayon - 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean estAuSol() {

        double centreX = hero.x.get();
        double testY = hero.y.get() + 11; // 10 (rayon) + 1

        int tileX = (int)(centreX / TAILLE_TUILE);
        int tileY = (int)(testY / TAILLE_TUILE);

        return tileY >= 0 && tileY < terrain.getHeight() &&
                tileX >= 0 && tileX < terrain.getWidth() &&
                terrain.getTerrain()[tileY][tileX] != 0;
    }


    public Circle creerSprite(Acteur a) {
        Circle r = new Circle(10);
        if (a instanceof Hero) {
            r.setFill(javafx.scene.paint.Color.LIGHTGOLDENRODYELLOW);
            r.setId("Hero");
        } else {
            r.setFill(javafx.scene.paint.Color.RED);
            r.setId("Ennemis");
        }
        r.setTranslateX(a.x.get());
        r.setTranslateY(a.y.get());
        return r;
    }

    // Démarrage
    public void demarrer() {
        gameLoop.play();
    }

    // Pause
    public void arreter() {
        gameLoop.stop();
    }


    public void ajouterActeur(Acteur acteur) {
        Circle sprite = creerSprite(acteur);
        sprites.put(acteur, sprite);
        zoneJeu.getChildren().add(sprite);
        environnement.ajouter(acteur);
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    // GETTER :
    public Environement getEnvironnement() {
        return environnement;
    }

    public Acteur getHero() {
        return hero;
    }

    public Terrain getTerrain() {
        return terrain;
    }
}