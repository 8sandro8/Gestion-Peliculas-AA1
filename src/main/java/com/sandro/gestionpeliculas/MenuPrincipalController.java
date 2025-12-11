package com.sandro.gestionpeliculas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuPrincipalController {

    // --- MÉTODOS DE NAVEGACIÓN ---

    @FXML
    public void irAPeliculas(ActionEvent actionEvent) {
        cambiarPantalla(actionEvent, "PeliculasView.fxml");
    }

    @FXML
    public void irAArtistas(ActionEvent actionEvent) {
        // Redirigimos a la pantalla de Actores que ya tienes hecha
        cambiarPantalla(actionEvent, "ActoresView.fxml");
    }

    @FXML
    public void cerrarApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    // --- NUEVO: MÉTODOS PARA CAMBIAR IDIOMA ---

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
            loader.setResources(bundle); // ¡Importante!
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
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
            // Siempre cargamos el idioma actual (sea cual sea el que esté puesto)
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setResources(bundle);
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}