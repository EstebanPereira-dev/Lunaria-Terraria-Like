package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Interfaces;

import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

// Interface pour les items consommables
public interface Consommable {
    void consommer(Hero hero);
    int getValeurNutritive();
    default int getValeurGuerison() { return 0; }
}