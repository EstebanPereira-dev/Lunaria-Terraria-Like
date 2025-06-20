package universite_paris8.iut.epereira.lunaria.vue;

import javafx.scene.image.Image;
import universite_paris8.iut.epereira.lunaria.modele.Item;

public class LoadImage {

    private Image imageVide, imageBois, imageBuisson, imageHerbe, imageMur, imageTerre, imagePiocheEnBois, imageHacheEnBois, imageEpeeEnBois, imageViandeMouton, imagePlanche;

    public LoadImage() {
        //initializer chaque image
        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
        imagePlanche = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Planche.png"));
        imagePiocheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/PiocheEnBois.png"));
        imageHacheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/HacheEnBois.png"));
        imageEpeeEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/EpeeEnBois.png"));
        imageViandeMouton = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/ViandeMouton.png"));
    }


    public Image selectImage(Item id) {
        Image sprite;

        if(id == null){
            sprite = imageVide;
        }else{
            switch (id.getId()) {
                case 1:
                    sprite = imageTerre;
                    break;

                case 2:
                    sprite = imageHerbe;
                    break;

                case 3:
                    sprite = imageBuisson;
                    break;

                case 4:
                    sprite = imageMur;
                    break;

                case 5:
                    sprite = imageBois;
                    break;
                case 15:
                    sprite = imagePlanche;
                    break;
                case 21:
                    sprite = imageEpeeEnBois;
                    break;

                case 30:
                    sprite = imageHacheEnBois;
                    break;

                case 31:
                    sprite = imagePiocheEnBois;
                    break;

                case 40:
                    sprite = imageViandeMouton;
                    break;

                default:
                    sprite = imageVide;
                    break;
            }
        }

        return sprite;
    }
}
