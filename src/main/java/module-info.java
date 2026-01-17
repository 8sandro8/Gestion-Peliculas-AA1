module com.sandro.gestionpeliculas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;

    opens com.sandro.gestionpeliculas to javafx.fxml;
    opens com.sandro.gestionpeliculas.modelo to javafx.fxml;

    exports com.sandro.gestionpeliculas;

    exports com.sandro.gestionpeliculas.modelo;
}