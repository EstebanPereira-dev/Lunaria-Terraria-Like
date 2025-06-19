package universite_paris8.iut.epereira.lunaria.modele.Craft;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;

import java.util.ArrayList;

public abstract class Craft {

    private Environement env;
    private ArrayList<Item> recette;
    private ArrayList<Integer> quantite;
    private Item resultat;

    public Craft(Environement env, Item resultat) {
        this.env = env;
        this.resultat = resultat;
        recette = new ArrayList<>();
        quantite = new ArrayList<>();
    }


    public void addItem(Item item, int quantite) {
        recette.add(item);
        this.quantite.add(quantite);
    }


    //regarde si le hero as tout les ingrédiant pour craft l'item
    public boolean craftable(){
        boolean quit = true;
        boolean craftable = true;
        for(int j = 0; j< recette.size();j++){
            int quantiteNecessaire = quantite.get(j);
            System.out.println("quantite necessaire: " + quantiteNecessaire);
            for(int i = 0; i < env.getHero().getInv().getTaille(); i++){
                //System.out.println((env.getHero().getInv().getListeditem()[i]));
                if(env.getHero().getInv().getListeditem().get(i) != null){
                    System.out.println("entrer dans case inv non vide");
                    if(env.getHero().getInv().getListeditem().get(i).getId() == recette.get(j).getId()){
                        System.out.println("entrer dans item necessaire = item dans inv");
                        quantiteNecessaire -= env.getHero().getInv().getQuantite()[i].getValue();
                    }
                }
            }
            //si apres avoir parcourus tout l'inventaire, il manque des ressource, alors return false
            if(quantiteNecessaire > 0){
                System.out.println("quantiteNecessaire : " + quantiteNecessaire);
                craftable = false;
                System.out.println( " quantiteNecessaire > 0 : " + craftable);
            }
        }
        System.out.println( "craftable return :" + craftable);
        return craftable;
    }


    public Item crafting(){
        System.out.println(" TU NE DEVRAIS PAS ETRE" + craftable());
        if(craftable()){
            //parcours de la liste d item de la recette
            for(int j = 0; j < recette.size(); j++){
                int quantiteAPrendre = quantite.get(j);
                //parcours de la liste d item dans l inventaire
                for (int i = 0;i < env.getHero().getInv().getTaille() && quantiteAPrendre > 0;i++){
                    if(env.getHero().getInv().getListeditem().get(i) != null){

                        //recherche la meme id de ressource dans l inventaire
                        if(env.getHero().getInv().getListeditem().get(i).getId() == recette.get(j).getId()){
                            //si la quantité dans l inventaire est supérieur a la quantite demander
                            //alors enlever la quantite a l inventaire
                            if(quantiteAPrendre < env.getHero().getInv().getQuantite()[i].getValue()){
                                env.getHero().getInv().getQuantite()[i].set(env.getHero().getInv().getQuantite()[i].getValue() - quantiteAPrendre);
                                quantiteAPrendre = 0;
                            }
                            //si la quantite est moindre ou égal
                            //alors supprimer de l'inventaire,
                            //réduit la quantite necessaire apres avoir 'pris' ce qu'il y as dans l'inventaire
                            // et continuer la recherche
                            if(env.getHero().getInv().getQuantite()[i].getValue() <= quantiteAPrendre){
                                quantiteAPrendre -= env.getHero().getInv().getQuantite()[i].getValue();
                                env.getHero().getInv().getQuantite()[i].setValue(0);
                                env.getHero().getInv().getListeditem().set(i,null);
                            }
                        }
                    }
                }
            }
            System.out.println("retourne le resultat");
            return resultat; //return l'item crafté
        }
        return null;
    }


    public Item getResultat(){
        return resultat;
    }

}
