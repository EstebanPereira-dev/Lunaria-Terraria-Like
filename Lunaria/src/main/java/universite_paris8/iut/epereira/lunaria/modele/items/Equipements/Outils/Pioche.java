package universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils;

import universite_paris8.iut.epereira.lunaria.modele.Item;
//id:0-19 = blocs, 20-29 =armes, 30-39=outils

public class Pioche extends Item {
    public Pioche() {
        super("Pioche en bois", "permet de casser la pierre et la terre", false, 31, false);
    }

    @Override
    public boolean peutCasser(int typeBloc) {
        // La pioche peut casser : terre (1), pierre (2), charbon (3), fer (4)
        return typeBloc == 1 || typeBloc == 2 || typeBloc == 3 || typeBloc == 4;
    }
}