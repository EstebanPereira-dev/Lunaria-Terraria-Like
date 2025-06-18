package universite_paris8.iut.epereira.lunaria.vue;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Craft.Craft;
import universite_paris8.iut.epereira.lunaria.modele.Item;



public class ObsCraft implements ListChangeListener<Craft> {
    private ScrollPane scrollPane;
    //Image des objet
    private Image imageVide,imageBois,imageMur,imagePiocheEnBois,imageHacheEnBois,imageViandeMouton;


    public ObsCraft(ScrollPane scrollPane){
        this.scrollPane = scrollPane;

        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
        imagePiocheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/PiocheEnBois.png"));
        imageHacheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/HacheEnBois.png"));
        imageViandeMouton = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/ViandeMouton.png"));

    }

    public Image selectImage(Item id){
        Image sprite;
        if(id == null){
            sprite = imageVide;
        }
        else{
            switch (id.getId()){

                case 4:
                    sprite = imageMur;
                    break;

                case 5:
                    sprite = imageBois;
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



    @Override
    public void onChanged(Change<? extends Craft> change) {
        while(change.next()){

        }
    }
}
