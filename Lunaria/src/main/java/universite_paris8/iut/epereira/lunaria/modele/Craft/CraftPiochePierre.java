package universite_paris8.iut.epereira.lunaria.modele.Craft;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Pierre;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Pioche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.PiochePierre;

public class CraftPiochePierre extends Craft {

    public CraftPiochePierre(Environement env) {
        super(env, new PiochePierre());
        super.addItem(new Planche(),10);
        super.addItem(new Pierre(),30);
    }
}
