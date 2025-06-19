module universite_paris8.iut.epereira.lunaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires jdk.unsupported.desktop;
    requires org.junit.jupiter.api;
    requires junit;

    opens universite_paris8.iut.epereira.lunaria.controleur to javafx.fxml;
    exports universite_paris8.iut.epereira.lunaria;
    opens universite_paris8.iut.epereira.lunaria.vue to javafx.fxml;
}
