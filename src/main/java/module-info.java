module com.sandro.gestionpeliculas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;

    // 1. Permitir que la interfaz (FXML) use nuestro código principal
    opens com.sandro.gestionpeliculas to javafx.fxml;

    // 2. ¡ESTA ES LA CLAVE!
    // Permitir que la Tabla (javafx.base) lea los datos de 'modelo' (Pelicula, Actor...)
    opens com.sandro.gestionpeliculas.modelo to javafx.base;

    exports com.sandro.gestionpeliculas;
}