package universite_paris8.iut.epereira.lunaria.controleur;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
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

        int[][] terrain = env.getTerrain().getTableau();

        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            gererClicGauche(terrain, tuileX, tuileY);
        }

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            gererClicDroit(terrain, tuileX, tuileY);
        }
        }
    }

    private void gererClicGauche(int[][] terrain, int tuileX, int tuileY) {
        System.out.println("Clic à : X = " + dernierePosX + " | Y = " + dernierePosY);

        if (terrain[tuileY][tuileX] != 0) { // Si la case n'est pas vide
            if (estDansRange(tuileX, tuileY)) {
                casserBloc(terrain, tuileX, tuileY);
            }
        } else {
            VueActeur vueHero= controleur.getVueActeur(env.getHero());
            if(vueHero instanceof VueHero)
                ((VueHero) vueHero).attaquer();
        }
        // Afficher l'inventaire après chaque action
        env.getHero().getInv().afficherInventaire();
    }

    private void gererClicDroit(int[][] terrain, int tuileX, int tuileY) {
        System.out.println("Clic droit à : X = " + dernierePosX + " | Y = " + dernierePosY);

        if (terrain[tuileY][tuileX] == 0) { // Si la case est vide
            if (estDansRange(tuileX, tuileY)) {
                placerBloc(tuileX, tuileY);
            }
        }
    }

    private boolean estDansRange(int tuileX, int tuileY) {
        int heroX = (int) (env.getHero().getPosX() / ConfigurationJeu.TAILLE_TUILE);
        int heroY = (int) (env.getHero().getPosY() / ConfigurationJeu.TAILLE_TUILE);
        int range = env.getHero().getRange(); // en nombre de cases

        int distanceX = Math.abs(tuileX - heroX);
        int distanceY = Math.abs(tuileY - heroY);

        return distanceX <= range && distanceY <= range;
    }

    private void casserBloc(int[][] terrain, int tuileX, int tuileY) {
        Item item = Item.getItemPourTuile(terrain[tuileY][tuileX]);

        env.getHero().getInv().ajouterItem(item, 1);
        // Supprimer la tuile du terrain
        env.getTerrain().changerTuile(0, tuileX, tuileY);
        controleur.getGestionMap().chargerTiles(env.getTerrain());

        //optionnel, juste pour voir l'avancé
        System.out.println("+1 " + item.getNom());
        // Afficher le total de cet item dans l'inventaire
        int totalItem = env.getHero().getInv().compterItem(item.getNom());
        System.out.println("Total " + item.getNom() + " : " + totalItem);

    }

    private void placerBloc(int tuileX, int tuileY) {
        if (estPositionOccupeeParActeur(tuileX, tuileY)) {
            System.out.println("Impossible de placer un bloc sur un acteur !");
        } else {
            int positionEquipe = env.getHero().getInv().getItemEquipe();

            if (positionEquipe != -1) {
                Item item = env.getHero().getInv().getListeditem()[positionEquipe];

                // Vérification que l'item existe et peut être placé
                if (item != null && item.getPeutEtrePlace()) {
                    // Placer le bloc sur le terrain
                    env.getTerrain().changerTuile(item.getId(), tuileX, tuileY);
                    controleur.getGestionMap().chargerTiles(env.getTerrain());

                    // Retirer un item de l'inventaire
                    env.getHero().getInv().retirerItem(positionEquipe, 1);

                    // Mettre à jour l'affichage de l'inventaire
                    controleur.getGestionInventaire().mettreAJourAffichage();

                    System.out.println("Bloc placé : " + item.getNom());
                } else if (item == null) {
                    System.out.println("Erreur : item équipé est null");
                } else {
                    System.out.println("Cet item ne peut pas être placé");
                }
            } else {
                System.out.println("Aucun item équipé pour placer");
            }
        }
    }

    private boolean estPositionOccupeeParActeur(int tuileX, int tuileY) {
        for (Acteur acteur : env.getActeurs()) {
            if (acteur instanceof Hero) {
                int heroX = (int) (acteur.getPosX() / ConfigurationJeu.TAILLE_TUILE);
                int heroY = (int) (acteur.getPosY() / ConfigurationJeu.TAILLE_TUILE);

                if ((tuileX == heroX - 1 || tuileX == heroX || tuileX == heroX + 1) && (tuileY == heroY - 1 || tuileY == heroY || tuileY == heroY + 1)) {
                    return true;
                } else {
                    int acteurX = (int) (acteur.getPosX() / ConfigurationJeu.TAILLE_TUILE);
                    int acteurY = (int) (acteur.getPosY() / ConfigurationJeu.TAILLE_TUILE);

                    if (tuileX == acteurX && tuileY == acteurY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}