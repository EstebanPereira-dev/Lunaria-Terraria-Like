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
    public DoubleProperty x;
    public DoubleProperty y;
    private Environement env;
    private int degat;
    public double GRAVITE = 0.2;

    public boolean collision ;
    public boolean auSol ;
    public double SAUT = -10;


    public Acteur(int pv, int v, int degat,Environement env, double x, double y){
        this.pv = pv;
        this.vitesseX = v;
        this.degat = degat;
        id = "A"+compteur;
        compteur++;
        this.x = new SimpleDoubleProperty(x);//random.nextInt(env.getWidth()-1));
        this.y = new SimpleDoubleProperty(y);//random.nextInt(env.getHeight()-1));
        collision = env.getTerrain().estEnCollision(this);
        auSol = env.getTerrain().estAuSol(this);
    }

    // Main character
    public Acteur(Environement env){
        this.env = env;
        pv = 100;
        this.vitesseX = 7;
        this.x = new SimpleDoubleProperty(100); // a definir
        this.y = new SimpleDoubleProperty(100); // a definir
        degat = 5;
        id = "A"+0;
        collision = env.getTerrain().estEnCollision(this);
        auSol = env.getTerrain().estAuSol(this);
    }

    public void physique(){

        double oldY = getY();

        if (!auSol) {
            vitesseY += GRAVITE;
            if (vitesseY > 8.0) vitesseY = 8.0;
        }

        if (vitesseY != 0) {
            this.y.set(oldY + vitesseY);
            if (collision) {
                y.set(oldY);
                vitesseY = 0;
            }
        }
    }





    //SETTER

    public void setXProperty(double x) {
        this.x.set(x);
    }

    public void setYProperty(double y) {
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
        return x.get();
    }

    public double getY() {
        return y.get();
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

    @Override
    public String toString() {
        return "Acteur :" + getId() + "Position :" + getXProperty() + "/" + getYProperty();
    }
}
