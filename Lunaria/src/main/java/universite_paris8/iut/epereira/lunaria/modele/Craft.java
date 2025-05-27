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
                if(env.getHero().getInv().getListeditem()[i].getId() == recette.get(j).getId()){
                    quantiteNecessaire -= env.getHero().getInv().getQuantite()[i];
                }
            }
            //si apres avoir parcourus tout l'inventaire, il manque des ressource, alors return false
            if(quantiteNecessaire > 0){
                return false;
            }
        }

        return true;
    }


    public Item Crafting(){
        if(craftable()){
            //parcours de la liste d item de la recette
            for(int j = 0; j < recette.size(); j++){
                int quantiteAPrendre = quantite.get(j);
                //parcours de la liste d item dans l inventaire
                for (int i = 0;i < env.getHero().getInv().getTaille() && quantiteAPrendre > 0;i++){

                    //recherche la meme id de ressource dans l inventaire
                    if(env.getHero().getInv().getListeditem()[i].getId() == recette.get(j).getId()){
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
                            env.getHero().getInv().getListeditem()[i] = null;
                        }
                    }
                }
            }
        }
        return resultat;
    }


//    public Item crafting() {
//        if (craftable()) { //si il a toute les ressource
//            for (Item itemrec : recette) { //pour chaque item dans la recette
//                int quantiteaenelever = itemrec.getQuantite(); //on récupère la quantité voulue
//                for (int i = 0; i < env.getHero().getInv().getTaille(); i++) { //on parcours tout l'inventaire
//                    if (itemrec.getNom().equals(env.getHero().getInv().getIteminList(i).getNom())) {//si c est le bon item
//                        if(quantiteaenelever>0){//savoir si la quantité voulue est déja atteinte
//                            if (env.getHero().getInv().getIteminList(i).getQuantite() < quantiteaenelever) { //si la quantité d'item voulue est suppérieur
//                                quantiteaenelever -= env.getHero().getInv().getIteminList(i).getQuantite(); //on enlève cette quantité a la quantité final
//                                env.getHero().getInv().removeItem(i);// et on retire l'item de l'inventaire
//                            } else {
//                                //si la quantité voulue est inférieur a la quantité voulue, on le retire juste de la quantité de l'item
//                                env.getHero().getInv().getIteminList(i).setQuantite(env.getHero().getInv().getIteminList(i).getQuantite()-quantiteaenelever );
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return resultat;
//    }


}
