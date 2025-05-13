module universite_paris8.iut.epereira.lunaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens universite_paris8.iut.epereira.lunaria.DossierControleur to javafx.fxml;
    exports universite_paris8.iut.epereira.lunaria;
}
