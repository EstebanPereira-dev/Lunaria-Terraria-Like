package universite_paris8.iut.epereira.lunaria.modele.Craft;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.PiocheBois;

public class CraftPiocheBois extends Craft{
    public CraftPiocheBois(Environement env) {
        super(env, new PiocheBois());
        super.addItem(new Planche(),30);
    }
}
