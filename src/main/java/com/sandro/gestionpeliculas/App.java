package com.sandro.gestionpeliculas.modelo; // Ahora está en modelo

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle; // Para quitar los bordes de la ventana

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // CORRECCIÓN: Usar ruta absoluta "/" para encontrar el FXML en resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/sandro/gestionpeliculas/Splash.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cargando...");
        stage.initStyle(StageStyle.UNDECORATED); // Esto hace que el Splash no tenga botones de cerrar/minimizar
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}