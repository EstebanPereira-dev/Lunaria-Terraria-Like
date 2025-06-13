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
    public int getDegats() { return 0; }
    public int getPortee() { return 0; }

    public boolean isEstStackable() {
        return estStackable;
    }

    public void setEstStackable(boolean estStackable) {
        this.estStackable = estStackable;
    }

    public int getStackMax() {
        return stackMax;
    }

    public void setStackMax(int stackMax) {
        this.stackMax = stackMax;
    }

    public boolean estEquipe() {
        return equipe;
    }

    public void setEquipe(boolean equipe) {
        this.equipe = equipe;
    }

    // Méthodes de vérification basées sur les valeurs de retour
    public boolean estOutil() { return peutCasser(1) || peutCasser(5); }
    public boolean estConsommable() { return getValeurNutritive() > 0; }
    public boolean estPlacable() { return peutEtrePlaceSur(0); }
    public boolean estArme() { return getDegats() > 0; }

    // Getters basiques
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public int getId() { return id; }
    public boolean isDestructible() { return destructible; }
    public boolean isConsommableUneSeuleFois() { return consommableUneSeuleFois; }
}