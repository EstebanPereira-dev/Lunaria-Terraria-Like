package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Interfaces;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

// Interface pour les armes
public interface Arme {
    int getDegats();
    int getPortee();
    void attaquer(Hero hero, Acteur cible);
}