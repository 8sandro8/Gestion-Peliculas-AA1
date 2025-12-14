package com.sandro.gestionpeliculas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle; // <--- ESTA ES LA LÍNEA QUE FALTABA

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Cargamos los textos
        ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

        // CAMBIO: Volvemos a poner 'Splash.fxml' para que salga el logo al inicio
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Splash.fxml"));
        fxmlLoader.setResources(bundle);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Gestión de Películas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}