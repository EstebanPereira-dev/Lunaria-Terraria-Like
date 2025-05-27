package universite_paris8.iut.epereira.lunaria.modele.items.Equipements;

import universite_paris8.iut.epereira.lunaria.modele.Item;

public class Arme extends Item {
    protected double degat;
    protected int range;
    public Arme(String nom, String description, int id) {
        super(nom, description,false,id,false);
    }

    public double getDegat() {
        return degat;
    }

    public int getRange() {
        return range;
    }
}
