package universite_paris8.iut.epereira.lunaria.controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;

public class VueMenuControleur {

    @FXML
    private void lancerJeu(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Map.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);

            stage.setScene(scene);
            stage.setTitle("Lunaria - Jeu en cours");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}