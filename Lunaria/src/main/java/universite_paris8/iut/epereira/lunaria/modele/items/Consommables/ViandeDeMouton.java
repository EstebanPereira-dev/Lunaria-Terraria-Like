package universite_paris8.iut.epereira.lunaria.modele.items.Consommables;

import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Interfaces.Consommable;

public class ViandeDeMouton extends Item implements Consommable {
    public ViandeDeMouton() {
        super("Viande Halal", "Traditionelle", false, 40, true);
    }

    @Override
    public void consommer(Hero hero) {
        hero.restaurerFaim(getValeurNutritive());
        System.out.println("Vous avez mangé de la viande de mouton !");
    }

    @Override
    public int getValeurNutritive() {
        return 30;
    }
}
