package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    protected SimpleIntegerProperty ecu;

    public DoubleProperty x;
    public DoubleProperty y;

    private Environement env;

    public double GRAVITE = 0.2;

    public boolean collision;
    public boolean auSol;
    public double SAUT = -5;

    public boolean attackOnCooldown = false;
    public int cooldownAttack;

    public Acteur(int pv, int v, Environement env, double x, double y) {
        ecu = new SimpleIntegerProperty(0);
        this.pv = pv;
        this.vitesseX = v;
        this.env = env;
        id = "A"+compteur;
        compteur++;

        this.posX = x;
        this.posY = y;

        cooldownAttack = 5;

        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);

        collision = env.getTerrain().estEnCollision(this);
        auSol = env.getTerrain().estAuSol(this);
    }

    // Main character
    public Acteur(Environement env) {
        ecu = new SimpleIntegerProperty(0);
        this.env = env;
        pv = 100;
        this.vitesseX = 3;

        this.posX = 900;
        this.posY = 100;

        this.x = new SimpleDoubleProperty(900);
        this.y = new SimpleDoubleProperty(100);

        id = "A"+0;
        collision = env.getTerrain().estEnCollision(this);
        auSol = env.getTerrain().estAuSol(this);
    }
    public abstract void deplacement();
    public abstract void agit();
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
    public abstract void loot();

    public void estMort(){
        env.retirer(this);
        env.marquerPourSuppression(this);
        loot();
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

    public int getEcu() {
        return ecu.get();
    }

    public Environement getEnv() {
        return env;
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

    public int getCooldownAttack() {
        return cooldownAttack;
    }
    //SETTER

    public void setPv(int pv) {
        this.pv = pv;
    }

    @Override
    public String toString() {
        return "Acteur{" +
                ", pv=" + pv +
                ", id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}