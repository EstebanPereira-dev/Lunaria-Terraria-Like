package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.image.Image;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;


public class VueTerrain {
    private Terrain tableau;
    private Environement env;
    private Controleur controleur;

    private Image imageVide;
    private Image imageTerre;
    private Image imageHerbe;
    private Image imageBuisson;
    private Image imageMur;
    private Image imageBois;
    private Image imagePlanche;

    public VueTerrain(Environement env, Controleur controleur) {
        this.controleur = controleur;
        this.env = env;
        controleur.getTabJeu().setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        controleur.getTabJeu().setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);
        controleur.getTilePaneId().setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        controleur.getTilePaneId().setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        tableau = env.getTerrain();

        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buche.png"));
        imagePlanche = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Planche.png"));
    }


    private Image getImageTuile(int typeTuile){
        if (typeTuile==0)
            return imageVide;
        else if (typeTuile==1)
            return imageTerre;
        else if (typeTuile ==2)
            return imageHerbe;
        else if (typeTuile==3)
            return imageBuisson;
        else if (typeTuile ==4)
            return imageMur;
        else if (typeTuile==5)
            return imageBois;
        else if (typeTuile==6)
            return imagePlanche;
        else
            return null;
        }
}