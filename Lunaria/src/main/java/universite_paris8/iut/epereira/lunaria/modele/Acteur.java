package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

public abstract class Acteur {
    static Random random = new Random();
    
    private int pv;
    private int v;
    public static int compteur=1;
    private String id;
    private DoubleProperty x;
    private DoubleProperty y;
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
        v = 3;
        this.x = new SimpleDoubleProperty(100); // a definir
        this.y = new SimpleDoubleProperty(100); // a definir
        degat = 5;
        id = "A"+0;
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

    public int getV() {
        return v;
    }

    @Override
    public String toString() {
        return "Acteur :" + getId() + "Position :" + getXProperty() + "/" + getYProperty();
    }
}
