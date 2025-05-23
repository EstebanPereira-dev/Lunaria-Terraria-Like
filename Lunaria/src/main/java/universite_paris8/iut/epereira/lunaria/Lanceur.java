/*package universite_paris8.iut.epereira.lunaria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;

import java.io.IOException;

public class Lanceur extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        menu(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Lanceur.class.getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Map.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);
        stage.setTitle("Lunaria");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    public void menu(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Lanceur.class.getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/VueMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 349.0,613);
        stage.setTitle("Lunaria");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}*/
package universite_paris8.iut.epereira.lunaria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Lanceur extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Menu/MenuImage.jpg"));

        // Chargement avec le chemin absolu en utilisant getResource
        URL fxmlUrl = getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Menu/VueMenu.fxml");
        if (fxmlUrl == null) {
            System.err.println("Fichier FXML introuvable ! Vérifiez le chemin.");
            System.exit(-1);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 613, 349);
        stage.setTitle("Lunaria - Menu Principal");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Ignorer le warning d'accessibilité sous Linux
        System.setProperty("javafx.embed.singleThread", "true");
        launch(args);
    }
}