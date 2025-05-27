package universite_paris8.iut.epereira.lunaria.controleur;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;
import universite_paris8.iut.epereira.lunaria.vue.VueActeur;
import universite_paris8.iut.epereira.lunaria.vue.VueHero;

public class GestionSouris {
    private Environement env;
    private Controleur controleur;

    public GestionSouris(Environement env, Controleur controleur) {
        this.env = env;
        this.controleur = controleur;
    }

    public void clicDeSouris(MouseEvent mouseEvent) {
        if(env.getHero().getPv() > 0) {
            controleur.dernierePosX = mouseEvent.getX();
            controleur.dernierePosY = mouseEvent.getY();

            int tuileX = (int) (controleur.dernierePosX / ConfigurationJeu.TAILLE_TUILE);
            int tuileY = (int) (controleur.dernierePosY / ConfigurationJeu.TAILLE_TUILE);

            int[][] terrain = env.getTerrain().getTableau();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                System.out.println("Clic à : X = " + controleur.dernierePosX + " | Y = " + controleur.dernierePosY);

                if (terrain[tuileY][tuileX] != 0) { // Si la case n'est pas vide
                    int heroX = (int) (env.getHero().getPosX() / ConfigurationJeu.TAILLE_TUILE);
                    int heroY = (int) (env.getHero().getPosY() / ConfigurationJeu.TAILLE_TUILE);
                    int range = env.getHero().getRange(); // en nombre de cases

                    int distanceX = Math.abs(tuileX - heroX);
                    int distanceY = Math.abs(tuileY - heroY);

                    if (distanceX <= range && distanceY <= range) {
                        Item item = Item.getItemPourTuile(terrain[tuileY][tuileX]);
                        int posLibre = env.getHero().getInv().trouverPremiereCaseVide();
                        env.getTerrain().changerTuile(0, tuileX, tuileY);
                        controleur.getGestionMap().chargerTiles(env.getTerrain());
                        if (posLibre != -1 && item != null) {
                            env.getHero().getInv().addItem(posLibre, item);
                            System.out.println("+1 de " + item.getNom());
                            System.out.println(env.getHero().getInv().toString());

                            //afficher si un item est équipé ou non
                            int i = env.getHero().getInv().getItemEquipe();
                            if (i != -1) {
                                Item itemEquipe = env.getHero().getInv().getListeditem()[i];
                                System.out.println(itemEquipe.getNom() + " " + itemEquipe.estEquipe());
                            } else {
                                System.out.println("Aucun item équipé.");
                            }
                        }
                    }
                } else {
                    VueActeur vueHero = controleur.getVueActeur(env.getHero());
                    if (vueHero instanceof VueHero) {
                        ((VueHero) vueHero).attaquer();
                    }
                }
            }

            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                System.out.println("Clic droit à : X = " + controleur.dernierePosX + " | Y = " + controleur.dernierePosY);

                if (terrain[tuileY][tuileX] != 0) { // Si la case n'est pas vide
                    int heroX = (int) (env.getHero().getPosX() / ConfigurationJeu.TAILLE_TUILE);
                    int heroY = (int) (env.getHero().getPosY() / ConfigurationJeu.TAILLE_TUILE);
                    int range = env.getHero().getRange(); // en nombre de cases

                    int distanceX = Math.abs(tuileX - heroX);
                    int distanceY = Math.abs(tuileY - heroY);

                    if (distanceX <= range && distanceY <= range) {
                        int i = env.getHero().getInv().getItemEquipe();
                        if (i != -1) {
                            Item itemEquipe = env.getHero().getInv().getListeditem()[i];
                            if (itemEquipe.getPeutEtrePlace()) {
                                env.getTerrain().changerTuile(itemEquipe.getId(), tuileX, tuileY);
                                controleur.getGestionMap().chargerTiles(env.getTerrain());
                                env.getHero().getInv().removeItem(i);
                            }
                        }
                    }
                }
            }
        }
    }
}