package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;

import java.util.Random;

public abstract class Acteur {
    static Random random = new Random();
    public double vitesseY;
    public double vitesseX;
    private int pv;
    public static int compteur=1;
    private String id;

    private double posX, posY;

    public DoubleProperty x;
    public DoubleProperty y;

    private Environement env;
    private int degat;
    public double GRAVITE = 0.2;

    public boolean collision;
    public boolean auSol;
    public double SAUT = -10;

    public Acteur(int pv, int v, int degat, Environement env, double x, double y) {
        this.pv = pv;
        this.vitesseX = v;
        this.degat = degat;
        this.env = env;
        id = "A"+compteur;
        compteur++;

        this.posX = x;
        this.posY = y;

        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);

        collision = env.getTerrain().estEnCollision(this);
        auSol = env.getTerrain().estAuSol(this);
    }

    // Main character
    public Acteur(Environement env) {
        this.env = env;
        pv = 100;
        this.vitesseX = 7;

        this.posX = 100;
        this.posY = 100;

        this.x = new SimpleDoubleProperty(100);
        this.y = new SimpleDoubleProperty(100);

        degat = 5;
        id = "A"+0;
        collision = env.getTerrain().estEnCollision(this);
        auSol = env.getTerrain().estAuSol(this);
    }

    public abstract void deplacement();

    public void updateVisualPosition() {
        x.set(posX);
        y.set(posY);
    }

    public void appliquerGravite() {
        auSol = getEnv().getTerrain().estAuSol(this);

        if (!auSol) {
            vitesseY += GRAVITE;

            if (vitesseY > 8.0) vitesseY = 8.0;
        } else {
            vitesseY = 0;
        }
    }

    public void deplacerVerticalement() {
        if (vitesseY == 0) return;

        double oldY = posY;

        posY += vitesseY;

        collision = getEnv().getTerrain().estEnCollision(this);

        if (collision) {
            posY = oldY;

            if (vitesseY > 0) {
                auSol = true;
            }

            vitesseY = 0;
        } else {
            auSol = getEnv().getTerrain().estAuSol(this);
            if (auSol && vitesseY > 0) {
                vitesseY = 0;
            }
        }

        updateVisualPosition();
    }

    public void deplacerHorizontalement(double deltaX) {
        if (deltaX == 0) return;

        double oldX = posX;

        posX += deltaX;

        if (getEnv().getTerrain().estEnCollision(this)) {
            posX = oldX;
        }

        updateVisualPosition();
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    // SETTER
    public void setXProperty(double x) {
        this.posX = x;
        this.x.set(x);
    }

    public void setYProperty(double y) {
        this.posY = y;
        this.y.set(y);
    }

    // GETTER :
    public Environement getEnv() {
        return env;
    }

    public DoubleProperty getXProperty() {
        return x;
    }

    public DoubleProperty getYProperty() {
        return y;
    }

    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }

    public String getId() {
        return id;
    }

    public int getPv() {
        return pv;
    }

    public double getVitesseX() {
        return vitesseX;
    }

}