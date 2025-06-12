module universite_paris8.iut.epereira.lunaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires junit;
    requires jdk.unsupported.desktop;

    opens universite_paris8.iut.epereira.lunaria.controleur to javafx.fxml;
    exports universite_paris8.iut.epereira.lunaria;
}
