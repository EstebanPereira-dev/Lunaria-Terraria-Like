package universite_paris8.iut.epereira.lunaria.modele;

import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Herbe;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Terre;

public class Item {
    private String nom;

    private String description;
    public static int item_ID = 1;
    private String id;
    private boolean condition;
    private boolean equipe=false;
    public int type; //1=bloc 2=outil 3=arme 4=ressource

    public Item(String nom, String description, int type){
        this.description = description;
        this.nom = nom;
        id = "I"+item_ID;
        item_ID++;
        condition = false;
        this.type=type;
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
            // Ajoute d'autres types si nécessaire
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

    @Override
    public String toString() {
        return nom + "\n" + description + "\n";
    }


}
