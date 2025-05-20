package universite_paris8.iut.epereira.lunaria.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Adepte;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.ArrayList;
import java.util.List;

public class Environement {
    private Terrain terrain;
    private Hero hero;
    private char cylceJourNuit;
    private int width;
    private int height;
    //private final ObservableList<Acteur> acteurs = FXCollections.observableArrayList();
    private ArrayList<Acteur> acteurs;
    private List<Acteur> acteursASupprimer = new ArrayList<>();

    public Environement(int width, int height){
        this.terrain = new Terrain(width/ConfigurationJeu.TAILLE_TUILE,height/ConfigurationJeu.TAILLE_TUILE);
        this.hero = new Hero(this);
        Acteur a1 = new Adepte(20,1,10,1,this,hero,300,500);
        acteurs = new ArrayList<>();
        acteurs.add(a1);
        acteurs.add(hero);
        this.height = height;
        this.width = width;
    }

    public void marquerPourSuppression(Acteur acteur) {
        acteursASupprimer.add(acteur);
    }

    public void supprimerActeursMarques() {
        for (Acteur acteur : acteursASupprimer) {
            acteurs.remove(acteur);
        }
        acteursASupprimer.clear();
    }

    // GETTER :
    public List<Acteur> getActeursASupprimer() {
        return new ArrayList<>(acteursASupprimer);
    }
    public int getHeight() {
        return height;
    }

    public ArrayList<Acteur> getActeurs() {
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
    public void retirer(Acteur a){ acteurs.remove(a);}
    public int getWidth() {
        return width;
    }

    public char getCylceJourNuit() {
        return cylceJourNuit;
    }

    public Hero getHero() {
        return hero;
    }
    public Terrain getTerrain() {
        return terrain;
    }
}
