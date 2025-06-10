package universite_paris8.iut.epereira.lunaria.controleur;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.epereira.lunaria.modele.*;


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
        controleur.getTilePaneId().getChildren().add(tuileSurbrillance);
    }



    public void gererPositionDeSouris(MouseEvent mouseEvent) {
        dernierePosX = mouseEvent.getX();
        dernierePosY = mouseEvent.getY();

        int tuileX = (int) (dernierePosX / ConfigurationJeu.TAILLE_TUILE);
        int tuileY = (int) (dernierePosY / ConfigurationJeu.TAILLE_TUILE);

        double posX = tuileX * ConfigurationJeu.TAILLE_TUILE;
        double posY = tuileY * ConfigurationJeu.TAILLE_TUILE;

        // Déplacer la surbrillance sur la tuile correspondante
        tuileSurbrillance.setTranslateX(posX);
        tuileSurbrillance.setTranslateY(posY);

        boolean doitAfficherSurbrillance = false;
        Color couleurSurbrillance = Color.WHITE;

        // Vérifier si le héros est dans la portée
        if (env.getHero().estDansRange(tuileX, tuileY)) {

            //verifie si on a un bloc d equipé
            if (env.getHero().getInv().getItemEquipeSousFormeItem().getId() > 0 && env.getHero().getInv().getItemEquipeSousFormeItem().getId() <= 19) {
                // Vérifie que la tuile est vide
                if (env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX, tuileY)) == 0) {
                    doitAfficherSurbrillance = true;
                    couleurSurbrillance = Color.GREEN;
                }
            }
            //verifie si o peut casser le bloc
            else if (env.verifCasser(tuileX, tuileY)) {
                doitAfficherSurbrillance = true;
                couleurSurbrillance = Color.RED;
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
            dernierePosX = mouseEvent.getX();
            dernierePosY = mouseEvent.getY();

            int tuileX = (int) (dernierePosX / ConfigurationJeu.TAILLE_TUILE);
            int tuileY = (int) (dernierePosY / ConfigurationJeu.TAILLE_TUILE);

//        ObservableList<Integer> terrain = env.getTerrain().getTableau();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                gererClicGauche(env.getTerrain().getTableau(), tuileX, tuileY);
            }
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

        if (env.getTerrain().getTableau().get(env.getTerrain().getPos(tuileX,tuileY)) == 0) { // Si la case est vide
            if (env.getHero().estDansRange(tuileX, tuileY)) {
                env.getHero().placerBloc(tuileX, tuileY);
                    // Mettre à jour l'affichage de l'inventaire
                    controleur.getGestionInventaire().mettreAJourAffichage();
            }
        }
    }


}