package universite_paris8.iut.epereira.lunaria.modele.items.Consommables;

import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Interfaces.Placable;
//id:0-19 = blocs, 20-29 =armes, 30-39=outils

public class Terre extends Item implements Placable {
    public Terre() {
        super("Terre","Bloc de terre meuble",true,1,true);
    }
    @Override
    public boolean peutEtrePlaceSur(int typeBloc) {
        return typeBloc == 0;
    }
}
