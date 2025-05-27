package universite_paris8.iut.epereira.lunaria.modele;

import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.*;

public class Item {
    private String nom;

    private String description;
    public int id;
    private boolean condition;
    private boolean equipe=false;
    private boolean peutEtrePlace;
    private boolean estStackable;
    private int stackMax;

    public Item(String nom, String description, boolean peutEtrePlace,int id, boolean estStackable){
        this.description = description;
        this.nom = nom;
        this.id = id;
        condition = false;
        this.peutEtrePlace=peutEtrePlace;
        this.estStackable=estStackable;
        if (this.estStackable)
            stackMax=999;
        }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean GetCondition() {
        return condition;
    }

    public static Item getItemPourTuile(int idTuile) {
        switch (idTuile) {
            case 1:
                return new Terre();
            case 2:
                return new Herbe();
            case 3:
                return new Buisson();
            case 4:
                return new Pierre();
            case 5:
                return new Bois();
            default:
                return null;
        }
    }

    public boolean estEquipe() {
        return equipe;
    }

    public void setEquipe(boolean equipe) {
        this.equipe = equipe;
    }
    public boolean getPeutEtrePlace(){
        return peutEtrePlace;
    }

    public void setPeutEtrePlace(boolean peutEtrePlace) {
        this.peutEtrePlace = peutEtrePlace;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return nom + "\n" + description + "\n";
    }

    public boolean isEquipe() {
        return equipe;
    }

    public boolean isEstStackable() {
        return estStackable;
    }

    public boolean isPeutEtrePlace() {
        return peutEtrePlace;
    }

    public int getStackMax() {
        return stackMax;
    }
}
