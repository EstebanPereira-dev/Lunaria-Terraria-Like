package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

public class CameraJeu {
    private final Pane conteneurMonde;
    private final GridPane terrainGrid;
    private final Pane conteneurJeu;
    private final Hero hero;

    // Taille de la carte totale
    private final double LARGEUR_CARTE_TOTALE;
    private final double HAUTEUR_CARTE_TOTALE;

    // Taille de la "fenêtre" visible
    private final double LARGEUR_ECRAN = ConfigurationJeu.WIDTH_SCREEN;
    private final double HAUTEUR_ECRAN = ConfigurationJeu.HEIGHT_SCREEN;

    // Position actuelle de la caméra
    private double cameraX = 0;
    private double cameraY = 0;

    public CameraJeu(GridPane terrainGrid, Pane conteneurJeu, Hero hero, double largeurCarte, double hauteurCarte) {
        this.terrainGrid = terrainGrid;
        this.conteneurJeu = conteneurJeu;
        this.hero = hero;
        this.LARGEUR_CARTE_TOTALE = largeurCarte;
        this.HAUTEUR_CARTE_TOTALE = hauteurCarte;

        // Créer le conteneur monde qui va contenir TOUT
        this.conteneurMonde = new Pane();


        // Retirer le terrain de son parent actuel et l'ajouter au conteneur monde
        if (terrainGrid.getParent() != null) {
            ((Pane) terrainGrid.getParent()).getChildren().remove(terrainGrid);
        }
        conteneurMonde.getChildren().add(terrainGrid);

        // Ajouter le conteneur monde au conteneur de jeu
        conteneurJeu.getChildren().add(conteneurMonde);

        // Configurer la taille de la carte
        terrainGrid.setPrefWidth(LARGEUR_CARTE_TOTALE);
        terrainGrid.setPrefHeight(HAUTEUR_CARTE_TOTALE);

        // Le conteneur monde a la même taille que la carte
        conteneurMonde.setPrefWidth(LARGEUR_CARTE_TOTALE);
        conteneurMonde.setPrefHeight(HAUTEUR_CARTE_TOTALE);

        // S'assurer que le conteneur a la bonne taille
        conteneurJeu.setPrefWidth(LARGEUR_ECRAN);
        conteneurJeu.setPrefHeight(HAUTEUR_ECRAN);

        // Activer le clipping pour ne montrer que la zone visible
        conteneurJeu.setClip(new javafx.scene.shape.Rectangle(LARGEUR_ECRAN, HAUTEUR_ECRAN));
    }

    public void mettreAJourCamera() {
        // Position du héros
        double heroX = hero.getPosX();
        double heroY = hero.getPosY();

        // Calculer où devrait être la caméra pour centrer le héros
        double nouvelleCameraX = heroX - (LARGEUR_ECRAN / 2);
        double nouvelleCameraY = heroY - (HAUTEUR_ECRAN / 2);

        // Appliquer les limites pour ne pas sortir de la carte
        nouvelleCameraX = Math.max(0, Math.min(nouvelleCameraX, LARGEUR_CARTE_TOTALE - LARGEUR_ECRAN));
        nouvelleCameraY = Math.max(0, Math.min(nouvelleCameraY, HAUTEUR_CARTE_TOTALE - HAUTEUR_ECRAN));

        // Mettre à jour la position de la caméra
        this.cameraX = nouvelleCameraX;
        this.cameraY = nouvelleCameraY;

        // Déplacer TOUT le monde (terrain + acteurs) ensemble
        conteneurMonde.setTranslateX(-cameraX);
        conteneurMonde.setTranslateY(-cameraY);
    }

    public void centrerSurHero() {
        double heroX = hero.getPosX();
        double heroY = hero.getPosY();

        this.cameraX = Math.max(0, Math.min(heroX - (LARGEUR_ECRAN / 2), LARGEUR_CARTE_TOTALE - LARGEUR_ECRAN));
        this.cameraY = Math.max(0, Math.min(heroY - (HAUTEUR_ECRAN / 2), HAUTEUR_CARTE_TOTALE - HAUTEUR_ECRAN));

        conteneurMonde.setTranslateX(-cameraX);
        conteneurMonde.setTranslateY(-cameraY);
    }

    public void ajouterActeurAuMonde(javafx.scene.Node acteurNode) {
        conteneurMonde.getChildren().add(acteurNode);
    }

    public void retirerActeurDuMonde(javafx.scene.Node acteurNode) {
        conteneurMonde.getChildren().remove(acteurNode);
    }


    public double[] ecranVersMonde(double ecranX, double ecranY) {
        return new double[]{ecranX + cameraX, ecranY + cameraY};
    }

    public double[] mondeVersEcran(double mondeX, double mondeY) {
        return new double[]{mondeX - cameraX, mondeY - cameraY};
    }
}