package com.sandro.gestionpeliculas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carga la pantalla de Splash
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Quita los bordes de la ventana
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setTitle("Cargando...");
        stage.setScene(scene);
        stage.show();
    }

    // El método main DEBE ser exactamente así:
    public static void main(String[] args) {
        launch();
    }
}