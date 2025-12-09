package com.sandro.gestionpeliculas; // Asegúrate de que esto coincide con tu paquete

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {

    // DATOS DE CONEXIÓN (Cámbialos si tu contraseña no es vacía)
    private static final String URL = "jdbc:mariadb://localhost:3306/aa1";
    private static final String USER = "root";
    private static final String PASSWORD = "Sandro.89"; // <--- PONE AQUÍ TU CONTRASEÑA DE DBeaver (si tienes)

    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Intentamos conectar
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ ¡Conexión exitosa con la Base de Datos 'aa1'!");
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar con la Base de Datos:");
            e.printStackTrace();
        }
        return conexion;
    }
}