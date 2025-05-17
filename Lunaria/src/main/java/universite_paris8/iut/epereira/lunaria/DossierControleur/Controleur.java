package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import java.net.URL;
import java.util.ResourceBundle;

public class Controleur implements Initializable {
    @FXML
    private GridPane inventaire;
    @FXML
    private TilePane tilePaneId;
    @FXML
    private TextArea pauseID;
    @FXML
    private Pane tabJeu;
    @FXML
    private ImageView background;

    private Environement env;
    private GestionnaireJeu gestionnaireJeu;
    private GestionnaireMap gestionMap;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN,ConfigurationJeu.HEIGHT_SCREEN);

        tabJeu.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tabJeu.setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);

        tilePaneId.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tilePaneId.setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        background.setFitWidth(ConfigurationJeu.WIDTH_SCREEN);
        background.setFitHeight(ConfigurationJeu.HEIGHT_SCREEN);

        gestionnaireJeu = new GestionnaireJeu(tabJeu,pauseID,inventaire,env);
        gestionMap = new GestionnaireMap(tilePaneId, env);

        Platform.runLater(() -> {
            gestionMap.chargerTiles(env.getTerrain());
        });

        Platform.runLater(() -> {
            tabJeu.requestFocus();
            gestionnaireJeu.demarrer();
        });
    }
}