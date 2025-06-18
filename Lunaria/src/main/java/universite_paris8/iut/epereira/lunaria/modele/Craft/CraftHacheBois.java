package universite_paris8.iut.epereira.lunaria.modele.Craft;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.HacheBois;

public class CraftHacheBois extends Craft{
    public CraftHacheBois(Environement env){
        super(env,new HacheBois());
        super.addItem(new Planche(),30);
    }
}
