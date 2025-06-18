package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils;

import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Interfaces.Outil;
//id:0-19 = blocs, 20-29 =armes, 30-39=outils

public abstract class Hache extends Item implements Outil {
    public Hache(String nom, int id) {
        super(nom, "Une hache permet de couper du bois",
                false, id, false);
    }

    @Override
    public boolean peutCasser(int typeBloc) {
        // La hache peut casser : bois (5), feuilles (6)
        return typeBloc == 5 || typeBloc == 6;
    }

}