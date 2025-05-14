package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.epereira.lunaria.modele.Terrain;
import java.net.URL;
import java.util.ResourceBundle;

public class Controleur implements Initializable {
    @FXML
    private TilePane tilePaneId;

    @FXML
    private Pane tabJeu;
    private Terrain terrain;
    private GestionnaireJeu gestionnaireJeu;
    private GestionnaireMap gestionMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            terrain = new Terrain(608, 384);
            gestionnaireJeu = new GestionnaireJeu(tabJeu);
            gestionMap = new GestionnaireMap(tilePaneId, terrain);

            Platform.runLater(() -> {
                try {
                    gestionMap.chargerTiles(terrain);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Platform.runLater(() -> {
                tabJeu.requestFocus();
                gestionnaireJeu.demarrer();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}