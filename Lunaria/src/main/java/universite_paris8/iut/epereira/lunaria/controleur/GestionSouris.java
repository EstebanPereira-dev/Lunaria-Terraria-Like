package universite_paris8.iut.epereira.lunaria.controleur;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.vue.VueActeur;
import universite_paris8.iut.epereira.lunaria.vue.VueHero;


public class GestionSouris {
    private Environement env;
    private Controleur controleur;
    public double dernierePosX = -1;
    public double dernierePosY = -1;

    public GestionSouris(Environement env, Controleur controleur) {
        this.env = env;
        this.controleur = controleur;
    }

    public void clicDeSouris(MouseEvent mouseEvent) {
        if(env.getHero().getPv() > 0) {
        dernierePosX = mouseEvent.getX();
        dernierePosY = mouseEvent.getY();

        int tuileX = (int) (dernierePosX / ConfigurationJeu.TAILLE_TUILE);
        int tuileY = (int) (dernierePosY / ConfigurationJeu.TAILLE_TUILE);

//        ObservableList<Integer> terrain = env.getTerrain().getTableau();

        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            gererClicGauche(env.getTerrain().getTableau(), tuileX, tuileY);
        }

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            gererClicDroit(env.getTerrain().getTableau(), tuileX, tuileY);
        }
        }
    }

    private void gererClicGauche(ObservableList<Integer> terrain, int tuileX, int tuileY) { // faire plutot un appel a agir
        System.out.println("Clic à : X = " + dernierePosX + " | Y = " + dernierePosY);

        if (env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX,tuileY)) != 0) { // Si la case n'est pas vide
            if (env.estDansRange(tuileX, tuileY)) {
                env.casserBloc(terrain, tuileX, tuileY);
            }
        } else {
            VueActeur vueHero= controleur.getVueActeur(env.getHero());
            if(vueHero instanceof VueHero)
                ((VueHero) vueHero).attaquer();
        }
        // Afficher l'inventaire après chaque action
        env.getHero().getInv().afficherInventaire();
    }

    private void gererClicDroit(ObservableList<Integer> terrain, int tuileX, int tuileY) {
        System.out.println("Clic droit à : X = " + dernierePosX + " | Y = " + dernierePosY);

        if (env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX,tuileY)) == 0) { // Si la case est vide
            if (env.estDansRange(tuileX, tuileY)) {
                env.placerBloc(tuileX, tuileY);
                // Mettre à jour l'affichage de l'inventaire
                controleur.getGestionInventaire().mettreAJourAffichage();
            }
        }
    }


   }