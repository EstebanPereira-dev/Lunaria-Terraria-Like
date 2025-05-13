package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Random;

public abstract class Acteur {
    static Random random = new Random();
    
    private int pv;
    private double v;
    public static int compteur=1;
    private String id;
    public DoubleProperty x;
    public DoubleProperty y;
    private Environement env;
    private int degat;


    public Acteur(int pv, int v, int degat, Environement env){
        this.pv = pv;
        this.v = v;
        this.degat = degat;
        id = "A"+compteur;
        compteur++;
        this.x = new SimpleDoubleProperty(random.nextInt(env.getWidth()-1));
        this.y = new SimpleDoubleProperty(random.nextInt(env.getHeight()-1));
    }

    // Main character
    public Acteur(Environement env){
        this.env = env;
        pv = 100;
        this.v = 10;
        this.x = new SimpleDoubleProperty(100); // a definir
        this.y = new SimpleDoubleProperty(100); // a definir
        degat = 5;
        id = "A"+0;
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

    public String getId() {
        return id;
    }

    public int getPv() {
        return pv;
    }

    public double getV() {
        return v;
    }

    @Override
    public String toString() {
        return "Acteur :" + getId() + "Position :" + getXProperty() + "/" + getYProperty();
    }
}
