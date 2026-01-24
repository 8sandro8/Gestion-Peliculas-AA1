package com.sandro.gestionpeliculas;

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
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes", locale);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sandro/gestionpeliculas/MenuPrincipal.fxml"));
            loader.setResources(bundle); // Le pasamos el nuevo idioma
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

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
    public void irADirectores(ActionEvent event) {
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/DirectoresView.fxml");
    }

    @FXML
    public void irADashboard(ActionEvent event) {
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/DashboardView.fxml");
    }

    @FXML
    public void salir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

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