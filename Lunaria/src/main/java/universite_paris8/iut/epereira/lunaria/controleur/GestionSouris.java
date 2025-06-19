package universite_paris8.iut.epereira.lunaria.controleur;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Interfaces.Consommable;

public class GestionSouris {
    private Environement env;
    private Controleur controleur;
    public double dernierePosX = -1;
    public double dernierePosY = -1;
    public Rectangle tuileSurbrillance;

    public GestionSouris(Environement env, Controleur controleur) {
        this.env = env;
        this.controleur = controleur;
        //construction du rectangle de surbrillance
        this.tuileSurbrillance = new Rectangle(ConfigurationJeu.TAILLE_TUILE, ConfigurationJeu.TAILLE_TUILE);
        this.tuileSurbrillance.setFill(Color.TRANSPARENT);
        this.tuileSurbrillance.setStrokeWidth(1);
        this.tuileSurbrillance.setVisible(false);

        controleur.getCamera().ajouterActeurAuMonde(tuileSurbrillance);
    }

    public void gererPositionDeSouris(MouseEvent mouseEvent) {

        double ecranX = mouseEvent.getX();
        double ecranY = mouseEvent.getY();

        double[] coordonneesMonde = controleur.getCamera().ecranVersMonde(ecranX, ecranY);
        double mondeX = coordonneesMonde[0];
        double mondeY = coordonneesMonde[1];


        int tuileX = (int) (mondeX / ConfigurationJeu.TAILLE_TUILE);
        int tuileY = (int) (mondeY / ConfigurationJeu.TAILLE_TUILE);

        // Garder les positions monde pour les autres méthodes
        dernierePosX = mondeX;
        dernierePosY = mondeY;

        double posX = tuileX * ConfigurationJeu.TAILLE_TUILE;
        double posY = tuileY * ConfigurationJeu.TAILLE_TUILE;

        // Déplacer la surbrillance sur la tuile correspondante
        tuileSurbrillance.setTranslateX(posX);
        tuileSurbrillance.setTranslateY(posY);

        boolean doitAfficherSurbrillance = false;
        Color couleurSurbrillance = Color.WHITE;

        if (tuileX >= 0 && tuileX < env.getTerrain().getWidth() &&
                tuileY >= 0 && tuileY < env.getTerrain().getHeight()) {

            if (env.getHero().estDansRange(tuileX, tuileY)) {
                Item itemEquipe = env.getHero().getInv().getItemEquipeSousFormeItem();
                if (itemEquipe != null) {
                    int typeBloc = env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX, tuileY));

                    // Vérifier si on peut placer un bloc
                    if (itemEquipe.peutEtrePlaceSur(typeBloc)) {
                        if (env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX, tuileY)) == 0) {
                            doitAfficherSurbrillance = true;
                            couleurSurbrillance = Color.GREEN;
                        }
                    }
                    // Vérifier si on peut casser le bloc
                    else if (itemEquipe.peutCasser(typeBloc)) {
                        doitAfficherSurbrillance = true;
                        couleurSurbrillance = Color.RED;
                    }
                }
            }
        }

        // Appliquer l'état de la surbrillance
        if (doitAfficherSurbrillance) {
            tuileSurbrillance.setVisible(true);
            tuileSurbrillance.setStroke(couleurSurbrillance);
        } else {
            tuileSurbrillance.setVisible(false);
        }
    }

    public void clicDeSouris(MouseEvent mouseEvent) {
        if(env.getHero().getPv() > 0) {
            double ecranX = mouseEvent.getX();
            double ecranY = mouseEvent.getY();

            double[] coordonneesMonde = controleur.getCamera().ecranVersMonde(ecranX, ecranY);
            double mondeX = coordonneesMonde[0];
            double mondeY = coordonneesMonde[1];

            int tuileX = (int) (mondeX / ConfigurationJeu.TAILLE_TUILE);
            int tuileY = (int) (mondeY / ConfigurationJeu.TAILLE_TUILE);

            // Garder les positions monde
            dernierePosX = mondeX;
            dernierePosY = mondeY;


            if (tuileX >= 0 && tuileX < env.getTerrain().getWidth() &&
                    tuileY >= 0 && tuileY < env.getTerrain().getHeight()) {

                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    gererClicGauche(env.getTerrain().getTableau(), tuileX, tuileY);
                }

                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    gererClicDroit(env.getTerrain().getTableau(), tuileX, tuileY);
                }
            } else {
                System.out.println("❌ Clic hors limites du terrain: tuile (" + tuileX + ", " + tuileY + ")");
            }
        }
    }

    private void gererClicGauche(ObservableList<Integer> terrain, int tuileX, int tuileY) { // faire plutot un appel a agir
        System.out.println("Clic à : X = " + dernierePosX + " | Y = " + dernierePosY);
        Item itemEquipe = env.getHero().getInv().getItemEquipeSousFormeItem();
        if (itemEquipe == null) {
            System.out.println("Aucun item équipé !");
            env.getHero().getInv().afficherInventaire();
            return;
        }
        if (env.verifCasser(tuileX,tuileY)) {
            if(env.getHero().estDansRange(tuileX, tuileY)) {
                env.getHero().casserBloc(terrain, tuileX, tuileY);
            }
        } else {
            if (env.getHero().verifAttaque()){
                boolean attaqueLancee = env.getHero().executerAttaque();

                if (attaqueLancee) {
                    controleur.getV().jouerAnimationAttaque();
                }
            }
        }
        env.getHero().getInv().afficherInventaire(); //dans la console
    }

    private void gererClicDroit(ObservableList<Integer> terrain, int tuileX, int tuileY) {
        System.out.println("Clic droit à : X = " + dernierePosX + " | Y = " + dernierePosY);

        Item itemEquipe = env.getHero().getInv().getItemEquipeSousFormeItem();

        if (itemEquipe == null) {
            System.out.println("Aucun item équipé !");
            return;
        }

        // Logique pour placer des blocs
        if (env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX, tuileY)) == 0) {
            if (env.getHero().estDansRange(tuileX, tuileY)) {
                int typeBloc = env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX, tuileY));

                if (itemEquipe.peutEtrePlaceSur(typeBloc)) { // ✅ Maintenant sûr
                    env.getHero().placerBloc(tuileX, tuileY);
                    controleur.getGestionInventaire().mettreAJourAffichage();
                    return;
                }
            }
        }

        // Logique pour consommer les items
        if (itemEquipe instanceof Consommable) {
            Consommable consommable = (Consommable) itemEquipe;
            consommable.consommer(env.getHero());

            // Retirer l'item de l'inventaire
            int positionEquipe = env.getHero().getInv().getItemEquipe();
            if (positionEquipe != -1) {
                env.getHero().getInv().retirerItem(positionEquipe, 1);
                System.out.println("Item consommé et retiré de l'inventaire !");
            }

            controleur.getGestionInventaire().mettreAJourAffichage();
        } else {
            System.out.println("Cet item ne peut pas être consommé !");
        }
    }

    private void gererConsommation() {
        Item itemEquipe = env.getHero().getInv().getItemEquipeSousFormeItem();

        if (itemEquipe != null && itemEquipe instanceof Consommable) {
            Consommable consommable = (Consommable) itemEquipe;
            consommable.consommer(env.getHero());

            int positionEquipe = env.getHero().getInv().getItemEquipe();
            if (positionEquipe != -1) {
                env.getHero().getInv().retirerItem(positionEquipe, 1);
                System.out.println("Item consommé et retiré de l'inventaire !");
            }

            controleur.getGestionInventaire().mettreAJourAffichage();
        } else if (itemEquipe != null) {
            System.out.println("Cet item ne peut pas être consommé !");
        }
    }
}