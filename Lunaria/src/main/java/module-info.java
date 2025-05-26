module universite_paris8.iut.epereira.lunaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;

    opens universite_paris8.iut.epereira.lunaria.controleur to javafx.fxml;
    exports universite_paris8.iut.epereira.lunaria;
}
