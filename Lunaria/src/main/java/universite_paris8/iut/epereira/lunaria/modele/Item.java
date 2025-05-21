package universite_paris8.iut.epereira.lunaria.modele;

public class Item {
    private String nom;
    private int quantite;
    private String description;
    public static int item_ID = 1;
    private String id;
    private boolean condition;

    public Item(String nom, int quantite, String description){
        this.description = description;
        this.nom = nom;
        this.quantite = quantite;
        id = "I"+item_ID;
        item_ID++;
        condition = false;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {this.quantite = quantite;}

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean GetCondition() {
        return condition;
    }





    @Override
    public String toString() {
        return nom + "\n" + description + "\n";
    }


}
