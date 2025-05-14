package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.HashMap;
import java.util.Map;

public class GestionnaireJeu {

    private final Pane zoneJeu;
    private final Environement environnement;
    private final Map<Acteur, Circle> sprites = new HashMap<>();
    private final Acteur hero;

    private double vitesseY = 0;
    private final double GRAVITE = 0.2;
    private final double SAUT = -10;

    private boolean toucheGauche = false;
    private boolean toucheDroite = false;
    private boolean toucheEspace = false;

    private Timeline gameLoop;

    public GestionnaireJeu(Pane zoneJeu) {
        this.zoneJeu = zoneJeu;
        this.environnement = new Environement(800, 600);
        this.hero = new Hero(environnement);

        Circle heroSprite = creerSprite(hero);
        sprites.put(hero, heroSprite);
        zoneJeu.getChildren().add(heroSprite);
        environnement.ajouter(hero);

        configurerEvenements();
        creerBoucleDeJeu();
    }
    // SERT A CONFIG
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
    // RESET TOUT LES 16 MS
    private void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(16), e -> miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    private void miseAJourJeu() {
        double limiteSol = zoneJeu.getPrefHeight() * 0.9;
        double posY = hero.y.get();
        boolean auSol = (posY + 10 >= limiteSol - 1);

        // Gestion du saut
        if (toucheEspace && auSol) {
            vitesseY = SAUT;
        }

        if (!auSol) {
            vitesseY += GRAVITE;
        } else if (vitesseY > 0) {
            vitesseY = 0;
            hero.y.set(limiteSol - 10);
        }

        hero.y.set(hero.y.get() + vitesseY);

        if (hero.y.get() > limiteSol - 10) {
            hero.y.set(limiteSol - 10);
            vitesseY = 0;
        }

        if (toucheGauche) {
            hero.x.set(hero.x.get() - ((Hero)hero).getV());
        }
        if (toucheDroite) {
            hero.x.set(hero.x.get() + ((Hero)hero).getV());
        }
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
        r.layoutYProperty().bind(a.getYProperty());
        r.layoutXProperty().bind(a.getXProperty());
        return r;
    }

    public void demarrer() {
        gameLoop.play();
    }
    // AU CAS OU  : Pour PAUSE
    public void arreter() {
        gameLoop.stop();
    }

    public void ajouterActeur(Acteur acteur) {
        Circle sprite = creerSprite(acteur);
        sprites.put(acteur, sprite);
        zoneJeu.getChildren().add(sprite);
        environnement.ajouter(acteur);
    }

    // GETTER
    public Environement getEnvironnement() {
        return environnement;
    }

    public Acteur getHero() {
        return hero;
    }
}