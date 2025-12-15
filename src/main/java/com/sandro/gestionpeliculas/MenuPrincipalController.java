package com.sandro.gestionpeliculas.modelo; // ✅ Correcto: carpeta modelo

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuPrincipalController implements Initializable {

    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
    }

    // --- MÉTODOS DE CAMBIO DE IDIOMA ---

    @FXML
    public void cambiarEspanol(ActionEvent event) {
        cargarIdioma(event, new Locale("es"));
    }

    @FXML
    public void cambiarIngles(ActionEvent event) {
        cargarIdioma(event, new Locale("en"));
    }

    private void cargarIdioma(ActionEvent event, Locale locale) {
        try {
            // 1. Cargamos el fichero de propiedades con el nuevo idioma
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes", locale);

            // 2. Cargamos de nuevo el FXML del menú
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sandro/gestionpeliculas/MenuPrincipal.fxml"));
            loader.setResources(bundle); // Le pasamos el nuevo idioma
            Parent root = loader.load();

            // 3. Mostramos la nueva escena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // Actualizamos la referencia local
            this.resources = bundle;

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cambiar el idioma: " + e.getMessage());
        }
    }

    // --- NAVEGACIÓN ---

    @FXML
    public void irAPeliculas(ActionEvent event) {
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/PeliculasView.fxml");
    }

    @FXML
    public void irAActores(ActionEvent event) {
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/ActoresView.fxml");
    }

    @FXML
    public void irADashboard(ActionEvent event) {
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/DashboardView.fxml");
    }

    @FXML
    public void salir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Usamos las claves del idioma actual para el título y el mensaje
        String titulo = resources.containsKey("alerta.titulo.aviso") ? resources.getString("alerta.titulo.aviso") : "Salir";
        String mensaje = resources.containsKey("menu.confirmar.salir") ? resources.getString("menu.confirmar.salir") : "¿Seguro que quieres salir?";

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    private void cambiarPantalla(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            // Pasamos el idioma actual a la siguiente pantalla
            loader.setResources(this.resources);

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error al cargar la pantalla: " + fxmlFile);
            mostrarError("No se pudo cargar la pantalla:\n" + fxmlFile + "\n\nError: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}