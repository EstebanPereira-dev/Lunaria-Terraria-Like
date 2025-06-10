package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Item;


public class ObsInventaire implements ListChangeListener<Item> {
    private TilePane tilePaneInv;
    private Environement env;
    //Image des objet
    private Image imageVide, imageBois, imageBuisson, imageHerbe, imageMur, imageTerre;

    public ObsInventaire(TilePane tilePaneInv, Environement env) {
        this.tilePaneInv = tilePaneInv;
        this.env = env;

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
            tilePaneInv.getChildren().set(change.getFrom(), warpper);

            tilePaneInv.getChildren().get(change.getFrom()).setOnMouseClicked(event -> {
            });



        }
    }

}
