package com.sandro.gestionpeliculas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // <--- Importante
import javafx.scene.Node;         // <--- Importante para (Node) event.getSource()
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;              // <--- Importante
import java.util.Locale;
import java.util.ResourceBundle;

// Implementamos Initializable para poder guardar el 'resources' al iniciar
public class MenuPrincipalController implements Initializable {

    // Variable para guardar el idioma actual
    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // Capturamos el idioma que nos pasa el Splash o la pantalla anterior
        this.resources = resources;
    }

    // --- MÉTODOS DE NAVEGACIÓN ---

    @FXML
    private void irAPeliculas(ActionEvent event) {
        try {
            // Ahora 'this.resources' ya no es null porque lo guardamos en el initialize
            ResourceBundle idiomaActual = this.resources;

            // Asegúrate de que este FXML existe. Si se llama 'PeliculasView.fxml', perfecto.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PeliculasView.fxml"));

            // Pasamos el idioma a la siguiente pantalla
            if (idiomaActual != null) {
                loader.setResources(idiomaActual);
            }

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void irAArtistas(ActionEvent actionEvent) {
        // Redirigimos a la pantalla de Actores
        cambiarPantalla(actionEvent, "ActoresView.fxml");
    }

    @FXML
    public void cerrarApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    // --- MÉTODOS PARA CAMBIAR IDIOMA ---

    @FXML
    public void cambiarEspanol(ActionEvent event) {
        System.out.println("🇪🇸 Cambiando a Español...");
        cargarIdioma(new Locale("es"), event);
    }

    @FXML
    public void cambiarIngles(ActionEvent event) {
        System.out.println("🇺🇸 Changing to English...");
        cargarIdioma(Locale.ENGLISH, event);
    }

    // Método auxiliar para recargar la escena con el nuevo idioma
    private void cargarIdioma(Locale nuevoIdioma, ActionEvent event) {
        try {
            // 1. Cambiamos el idioma GLOBAL de la app
            Locale.setDefault(nuevoIdioma);

            // 2. Cargamos el diccionario actualizado
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

            // 3. Recargamos la pantalla del menú
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error al cambiar de idioma.");
        }
    }

    // Método auxiliar para no repetir código al cambiar de pantalla
    private void cambiarPantalla(ActionEvent event, String fxml) {
        try {
            // Siempre cargamos el idioma actual
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setResources(bundle);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}