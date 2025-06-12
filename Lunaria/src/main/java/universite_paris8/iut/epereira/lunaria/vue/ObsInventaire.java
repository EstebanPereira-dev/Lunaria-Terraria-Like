package universite_paris8.iut.epereira.lunaria.vue;

import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import universite_paris8.iut.epereira.lunaria.modele.Item;

public class ObsInventaire implements ListChangeListener<Item> {
    //Image dans FXML de l'inventaire
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9;

    //Image des objet
    private Image imageVide,imageBois,imageBuisson,imageHerbe,imageMur,imageTerre,imagePiocheEnBois,imageHacheEnBois,imageViandeMouton;

    public ObsInventaire(ImageView img1,ImageView img2,ImageView img3,ImageView img4,ImageView img5,ImageView img6,ImageView img7,ImageView img8,ImageView img9){
        //initializer pour avoir toute les image lier avec le controleur
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
        this.img6 = img6;
        this.img7 = img7;
        this.img8 = img8;
        this.img9 = img9;

        //initializer chaque image
        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));
        imagePiocheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/PiocheEnBois.png"));
        imageHacheEnBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/HacheEnBois.png"));
        imageViandeMouton = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/ViandeMouton.png"));

    }

    /*
    quand la liste de l'inventaire change on update en prennant l'id de l item pour avoir la bonne image
    et on prend la position dans l'inventaire pour poser sur la bonne imageView
     */
    @Override
    public void onChanged(Change change) {
        while (change.next()){
            Image sprite;
            Item item = (Item) change.getList().get(change.getFrom());

//            System.out.println("Item Id:");
//            System.out.println(item.getId());
            if(item == null){
                sprite = imageVide;
            }
            else{
                //selon l'id de l'item, on prend la bonne image
                switch (item.getId()){
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


            //System.out.println("\n\n numero inventaire:");
            //System.out.println(change.getFrom());

            //selon la position de l'item dans l'inventaire on prend la bonne case ou placer l'item
            switch (change.getFrom()){
                case 0:
                    img1.setImage(sprite);
                    break;

                case 1:
                    img2.setImage(sprite);
                    break;

                case 2:
                    img3.setImage(sprite);
                    break;

                case 3:
                    img4.setImage(sprite);
                    break;

                case 4:
                    img5.setImage(sprite);
                    break;

                case 5:
                    img6.setImage(sprite);
                    break;

                case 6:
                    img7.setImage(sprite);
                    break;

                case 7:
                    img8.setImage(sprite);
                    break;

                case 8:
                    img9.setImage(sprite);
                    break;
            }
        }
    }
}
