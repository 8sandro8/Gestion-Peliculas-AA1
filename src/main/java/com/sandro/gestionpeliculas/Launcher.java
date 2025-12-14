package com.sandro.gestionpeliculas;

public class Launcher {
    public static void main(String[] args) {
        // Prueba de conexión (asegúrate de que aquí diga .conectar() y no .getConexion())
        ConexionBBDD.conectar();

        App.main(args);
    }
}