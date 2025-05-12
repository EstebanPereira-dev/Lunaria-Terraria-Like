package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.property.IntegerProperty;

import java.util.Random;

public class Acteur {
    private int pv;
    private int v;
    public static int compteur=0;
    private String id;
    private IntegerProperty x;
    private IntegerProperty y;
    private Environement env;
    public Acteur(int pv, int v){
        this.pv = pv;
        this.v = v;
        id = "A"+compteur;
        compteur++;
        this.x = random.nextInt(env.getWidth()-1);
        this.y= random.nextInt(env.getHeight()-1);
    }

    public
}
