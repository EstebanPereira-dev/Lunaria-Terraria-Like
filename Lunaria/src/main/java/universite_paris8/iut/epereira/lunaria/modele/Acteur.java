package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.property.IntegerProperty;

import java.util.Random;

public abstract class Acteur {
    static Random random;
    
    private int pv;
    private int v;
    public static int compteur=1;
    private String id;
    private int x;
    private int y;
    private Environement env;
    private int degat;


    public Acteur(int pv, int v, int degat, Environement env){
        this.pv = pv;
        this.v = v;
        this.degat = degat;
        id = "A"+compteur;
        compteur++;
        x = random.nextInt(env.getWidth()-1);
        y= random.nextInt(env.getHeight()-1);
    }

    // Main character
    public Acteur(Environement env){
        pv = 100;
        v = 3;
        x = 0; // a definir
        y = 0; // a definir
        degat = 5;
        this.env = env;
        id = "A"+0;
    }

    // GETTER :
    public Environement getEnv() {
        return env;
    }

    public int getPv() {
        return pv;
    }

    public int getV() {
        return v;
    }
}
