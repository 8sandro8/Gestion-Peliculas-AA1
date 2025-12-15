module com.sandro.gestionpeliculas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Para la base de datos
    requires org.mariadb.jdbc; // Si usas MariaDB (a veces necesario explícitamente)

    // PERMISOS PARA FXML (Para que funcionen los @FXML y los controladores)
    opens com.sandro.gestionpeliculas to javafx.fxml;
    opens com.sandro.gestionpeliculas.modelo to javafx.fxml;

    // PERMISOS PARA EJECUTAR LA APP (Exports)
    exports com.sandro.gestionpeliculas;

    // ⚠️ ESTA ES LA LÍNEA QUE TE FALTA Y SOLUCIONA EL ERROR:
    exports com.sandro.gestionpeliculas.modelo;
}