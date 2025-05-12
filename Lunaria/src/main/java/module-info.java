module universite_paris8.iut.epereira.lunaria {
    requires javafx.controls;
    requires javafx.fxml;


    opens universite_paris8.iut.epereira.lunaria to javafx.fxml;
    exports universite_paris8.iut.epereira.lunaria;
}