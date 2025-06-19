package universite_paris8.iut.epereira.lunaria.modele;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

public abstract class Item {
    protected String nom;
    protected String description;
    protected boolean destructible;
    protected int id;
    protected boolean consommableUneSeuleFois;
    protected boolean estStackable;
    protected int stackMax;
    protected boolean equipe;

    public Item(String nom, String description, boolean destructible, int id, boolean consommableUneSeuleFois) {
        this.nom = nom;
        this.description = description;
        this.destructible = destructible;
        this.id = id;
        this.consommableUneSeuleFois = consommableUneSeuleFois;
        this.estStackable = true;
        this.stackMax = 64;
        this.equipe = false;
    }

    // Méthodes dispatch - chaque item redéfinit seulement ce qu'il sait faire
    public boolean peutCasser(int typeBloc) { return false; }
    public boolean peutEtrePlaceSur(int typeBloc) { return false; }
    public int getValeurNutritive() { return 0; }

    public boolean isEstStackable() {
        return estStackable;
    }

    public int getStackMax() {
        return stackMax;
    }

    public boolean estEquipe() {
        return equipe;
    }

    public void setEquipe(boolean equipe) {
        this.equipe = equipe;
    }

    // Getters basiques
    public String getNom() { return nom; }
    public int getId() { return id; }
}