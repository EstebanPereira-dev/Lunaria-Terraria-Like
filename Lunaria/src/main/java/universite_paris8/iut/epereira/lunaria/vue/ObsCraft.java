//package universite_paris8.iut.epereira.lunaria.vue;
//
//import javafx.collections.ListChangeListener;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.TilePane;
//import javafx.scene.layout.VBox;
//import universite_paris8.iut.epereira.lunaria.modele.Craft.Craft;
//import universite_paris8.iut.epereira.lunaria.modele.Environement;
//
//
//public class ObsCraft implements ListChangeListener<Craft> {
//    private TilePane tilePane;
//    private LoadImage librairiImage;
//    private Environement env;
//
//    public ObsCraft(TilePane tilePane, Environement env){
//        this.tilePane = tilePane;
//        librairiImage = new LoadImage();
//        this.env = env;
//    }
//
//
//
//
//    @Override
//    public void onChanged(Change<? extends Craft> change) {
//        while(change.next()){
//            System.out.println("enter onchange crafting list");
//
//            tilePane.getChildren().clear();
//            System.out.println(env.getCraftingList());
//
//            for(int i = 0; i < env.getCraftingList().size();i++){
//                System.out.println("entrer dans boucle for craft");
//                ImageView imageView = new ImageView(librairiImage.selectImage(env.getCraftingList().get(i).getResultat()));
//                imageView.setFitHeight(48);
//                imageView.setFitWidth(48);
//                VBox vbox = new VBox();
//                vbox.setStyle("-fx-border-color: red");
//                vbox.getChildren().add(imageView);
//                tilePane.getChildren().add(vbox);
//            }
//
////            for(int j = tilePane.getChildren().size()-1; j >= 0;j--){
////                for(int i = 0; i< change.getRemovedSize();i++){
////                    if(tilePane.getChildren().get(j).getUserData() != null){
////                        if(tilePane.getChildren().get(j).getUserData().equals(change.getRemoved().get(i).getResultat().getId())){
////                            tilePane.getChildren().remove(j);
////                        }
////                    }
////                }
////            }
////
////            System.out.println(change.getAddedSubList());
////            for(int j = 0; j < change.getAddedSize();j++){
////                ImageView imageView = new ImageView(librairiImage.selectImage(change.getAddedSubList().get(j).getResultat()));
////                imageView.setUserData(change.getAddedSubList().get(j).getResultat().getId());
////                tilePane.getChildren().add(imageView);
////            }
//        }
//    }
//}
