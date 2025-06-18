package universite_paris8.iut.epereira.lunaria.modele.Craft;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Pierre;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Hache;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.HachePierre;

public class CraftHachePierre extends Craft{


    public CraftHachePierre(Environement env) {
        super(env, new HachePierre());
        super.addItem(new Planche(),10);
        super.addItem(new Pierre(), 30);
    }

}
