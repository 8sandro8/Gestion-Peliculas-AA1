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
        // 1. Cargar el diseño FXML que hiciste
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuPrincipal.fxml"));

        // 2. Crear la escena (el contenido de la ventana)
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // 3. Poner título y mostrar la ventana
        stage.setTitle("Gestión de Cine - AA1");
        stage.setScene(scene);
        stage.show();

        // 4. (Opcional) Dejamos el aviso de conexión en la consola por si acaso
        System.out.println("🚀 Aplicación iniciada...");
        Connection con = ConexionBBDD.conectar();
    }

    public static void main(String[] args) {
        launch(); // Esto es lo que arranca la parte visual
    }
}