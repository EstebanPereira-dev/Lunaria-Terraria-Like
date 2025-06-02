package universite_paris8.iut.epereira.lunaria.modele;

import java.util.ArrayList;

public class Craft {

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

        for(int j = 0; j< recette.size();j++){
            int quantiteNecessaire = quantite.get(j);

            for(int i = 0; i < env.getHero().getInv().getTaille(); i++){
                //System.out.println((env.getHero().getInv().getListeditem()[i]));
                if(env.getHero().getInv().getListeditem().get(i) != null){
                    if(env.getHero().getInv().getListeditem().get(i).getId() == recette.get(j).getId()){
                        quantiteNecessaire -= env.getHero().getInv().getQuantite()[i];
                    }
                }
            }
            //si apres avoir parcourus tout l'inventaire, il manque des ressource, alors return false
            if(quantiteNecessaire > 0){
                return false;
            }
        }

        return true;
    }


    public Item crafting(){
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
                            if(quantiteAPrendre < env.getHero().getInv().getQuantite()[i]){
                                env.getHero().getInv().getQuantite()[i] -= quantiteAPrendre;
                                quantiteAPrendre = 0;
                            }
                            //si la quandtite est moindre ou égal
                            //alors supprimer de l'inventaire,
                            //réduit la quantite necessaire apres avoir 'pris' ce qu'il y as dans l'inventaire
                            // et continuer la recherche
                            if(env.getHero().getInv().getQuantite()[i] <= quantiteAPrendre){
                                quantiteAPrendre -= env.getHero().getInv().getQuantite()[i];
                                env.getHero().getInv().getQuantite()[i] = 0;
                                env.getHero().getInv().getListeditem().add(i,null);
                            }
                        }
                    }
                }
            }
        }
        return resultat; //return l'item crafté
    }



}
