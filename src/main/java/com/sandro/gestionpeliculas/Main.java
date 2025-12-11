package com.sandro.gestionpeliculas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle; // <--- ¡ESTA ERA LA QUE FALTABA!

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Cargar el idioma.
        // Si quieres probar en inglés, cambia la siguiente línea por:
        // ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes", Locale.ENGLISH);
        ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

        // 2. Cargar la vista pasándole el diccionario de idiomas
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuPrincipal.fxml"));
        fxmlLoader.setResources(bundle); // <--- ¡AQUÍ CONECTAMOS EL IDIOMA!

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // 3. Cargar Estilos (CSS)
        scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());

        stage.setTitle("Gestión de Cine - AA1");
        stage.setScene(scene);
        stage.show();

        System.out.println("🚀 Aplicación iniciada. Idioma detectado: " + bundle.getLocale());
    }

    public static void main(String[] args) {
        launch();
    }
}