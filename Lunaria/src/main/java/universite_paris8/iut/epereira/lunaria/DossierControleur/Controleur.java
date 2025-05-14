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
    private GestionnaireJeu gestionnaireJeu;
    GestionnaireMap gestionMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gestionnaireJeu = new GestionnaireJeu(tabJeu);
        Platform.runLater(() -> {
            tabJeu.requestFocus();
        });
        gestionnaireJeu.demarrer();
        gestionMap= new GestionnaireMap(tilePaneId);
        gestionMap.chargerTiles(gestionMap.getCarte());
    }

}