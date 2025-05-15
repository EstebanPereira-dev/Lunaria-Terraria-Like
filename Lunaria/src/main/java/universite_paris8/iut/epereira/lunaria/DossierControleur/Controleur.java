package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import java.net.URL;
import java.util.ResourceBundle;

public class Controleur implements Initializable {
    @FXML
    private TilePane tilePaneId;
    @FXML
    private TextArea pauseID;
    @FXML
    private Pane tabJeu;
    private Terrain terrain;
    private GestionnaireJeu gestionnaireJeu;
    private GestionnaireMap gestionMap;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            terrain = new Terrain(22, 16);
            gestionnaireJeu = new GestionnaireJeu(tabJeu,pauseID);
            gestionMap = new GestionnaireMap(tilePaneId, terrain);

            gestionnaireJeu.setTerrain(terrain);

            Platform.runLater(() -> {

                    gestionMap.chargerTiles(terrain);
            });

            Platform.runLater(() -> {
                tabJeu.requestFocus();
                gestionnaireJeu.demarrer();
            });
    }
}