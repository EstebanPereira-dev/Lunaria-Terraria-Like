package universite_paris8.iut.epereira.lunaria.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Environement {
    private char cylceJourNuit;
    private int width;
    private int height;
    private final ObservableList<Acteur> acteurs = FXCollections.observableArrayList();

    public Environement(int width, int height){
        this.height = height;
        this.width = width;
    }


    // GETTER :
    public int getHeight() {
        return height;
    }

    public ObservableList<Acteur> getActeurs() {
        return acteurs;
    }
    public Acteur getActeur(String id) {
        for(Acteur a:this.acteurs){
            if(a.getId().equals(id)){
                return a;
            }
        }
        return null;
    }
    public void ajouter(Acteur a){
        acteurs.add(a);
    }
    public int getWidth() {
        return width;
    }

    public char getCylceJourNuit() {
        return cylceJourNuit;
    }



}
