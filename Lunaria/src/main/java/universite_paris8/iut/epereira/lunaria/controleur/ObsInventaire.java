package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Item;

public class ObsInventaire implements ListChangeListener<Item> {
    //Image dans FXML de l'inventaire
//    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9;
    private TilePane tilePaneInv;
    //Image des objet
    private Image imageVide, imageBois, imageBuisson, imageHerbe, imageMur, imageTerre;

    public ObsInventaire(TilePane tilePaneInv) {
        //initializer pour avoir toute les image lier avec le controleur
//        this.img1 = img1;
//        this.img2 = img2;
//        this.img3 = img3;
//        this.img4 = img4;
//        this.img5 = img5;
//        this.img6 = img6;
//        this.img7 = img7;
//        this.img8 = img8;
//        this.img9 = img9;
        this.tilePaneInv = tilePaneInv;

        //initializer chaque image
        imageVide = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Vide.png"));
        imageTerre = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Terre.png"));
        imageHerbe = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Herbe.png"));
        imageBuisson = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Buisson.png"));
        imageMur = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/MurEnPierre.png"));
        imageBois = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/FondEnBois.png"));

    }

    /*
    quand la liste de l'inventaire change on update en prennant l'id de l item pour avoir la bonne image
    et on prend la position dans l'inventaire pour poser sur la bonne imageView
     */
    @Override
    public void onChanged(Change change) {
        while (change.next()) {
            Image sprite;
            Item item = (Item) change.getList().get(change.getFrom());

//            System.out.println("Item Id:");
//            System.out.println(item.getId());
            //selon l'id de l'item, on prend la bonne image
            switch (item.getId()) {
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
                default:
                    sprite = imageVide;
                    break;
            }
            ImageView img = new ImageView(sprite);

            img.setFitHeight(48);
            img.setFitWidth(48);

            VBox warpper = new VBox(img);
            warpper.setPadding(new Insets(2.5));
            warpper.setStyle("-fx-border-color: grey;");
            tilePaneInv.getChildren().add(change.getFrom(), warpper);



            //System.out.println("\n\n numero inventaire:");
            //System.out.println(change.getFrom());

            //selon la position de l'item dans l'inventaire on prend la bonne case ou placer l'item
        }
    }
}
