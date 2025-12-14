package com.sandro.gestionpeliculas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {

    // DATOS DE TU MARIADB
    private static final String URL = "jdbc:mariadb://localhost:3306/aa1";
    private static final String USER = "root";
    private static final String PASSWORD = "Sandro.89";

    // HE CAMBIADO EL NOMBRE A 'conectar' PARA QUE TUS DAOs FUNCIONEN
    public static Connection conectar() {
        Connection con = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ ¡Conexión exitosa a MariaDB!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error al conectar con la base de datos");
            e.printStackTrace();
        }
        return con;
    }
}