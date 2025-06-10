package universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Inventaire;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.ViandeDeMouton;

public class Aleksa extends PNJ{
    public Aleksa(int pv, int v, Environement env, double x, double y) {
        super(pv, v, env, x, y, "Le Serbe poète", true);
        reStock();
    }

    public void reStock() {
        inv.ajouterItem(new Planche(), 50);
    }
    public void prix() {

    }



    @Override
    public void agit() {

    }

    @Override
    public void loot(){
        if(getPv() <= 0){
            getEnv().getHero().getInv().ajouterItem(new ViandeDeMouton());
        }
    }
}
