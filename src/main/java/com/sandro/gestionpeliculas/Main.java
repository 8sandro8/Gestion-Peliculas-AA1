package com.sandro.gestionpeliculas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // --- NUEVA LÍNEA: APLICAR ESTILOS ---
        scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
        // ------------------------------------

        stage.setTitle("Gestión de Cine - AA1");
        stage.setScene(scene);
        stage.show();

        System.out.println("🚀 Aplicación iniciada...");
        // Connection con = ConexionBBDD.conectar(); // Esto ya lo usas dentro de los DAOs
    }

    public static void main(String[] args) {
        launch(); // Esto es lo que arranca la parte visual
    }
}