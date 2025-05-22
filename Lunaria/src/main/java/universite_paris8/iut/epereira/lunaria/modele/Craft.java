//package universite_paris8.iut.epereira.lunaria.modele;
//
//import java.util.ArrayList;
//
//public class Craft {
//
//    private Environement env;
//    private ArrayList<Item> recette;
//    private Item resultat;
//
//    public Craft(Environement env, Item resultat) {
//        this.env = env;
//        this.resultat = resultat;
//        recette = new ArrayList<>();
//    }
//
//
//    public void addItem(Item item) {
//        recette.add(item);
//    }
//
//
//    public boolean craftable() {
//        for (Item itemRecette : recette) {
//            int compteur = 0;
//            for (Item item : env.getHero().getInv().getListeditem()) {
//                if (item.getNom().equals(itemRecette.getNom())) {
//                    compteur += item.getQuantite();
//                }
//            }
//            if (compteur < itemRecette.getQuantite()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
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
//
//
//}
