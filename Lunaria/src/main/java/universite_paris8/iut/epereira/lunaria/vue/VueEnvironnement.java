package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.image.Image;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Environement;

import javax.sound.sampled.Control;

public class VueEnvironnement {
    private Controleur controleur;

    //images du background

    private Image imageJour=new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/BackgroundJour.png"));
    private Image imagenuit=new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png"));

    public VueEnvironnement(Controleur controleur){
        this.controleur=controleur;
    }

    public void setBackground(){
        if (controleur.getEnv().getEtatJour())
            controleur.getBackground().setImage(imageJour);
        else
            controleur.getBackground().setImage(imagenuit);
    }
}
