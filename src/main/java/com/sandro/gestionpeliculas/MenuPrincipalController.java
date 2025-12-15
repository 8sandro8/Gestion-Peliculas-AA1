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
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuPrincipalController implements Initializable {

    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
    }

    @FXML
    public void irAPeliculas(ActionEvent event) {
        // RUTA ABSOLUTA: /com/sandro/gestionpeliculas/PeliculasView.fxml
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/PeliculasView.fxml");
    }

    @FXML
    public void irAActores(ActionEvent event) {
        // RUTA ABSOLUTA: /com/sandro/gestionpeliculas/ActoresView.fxml
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/ActoresView.fxml");
    }

    @FXML
    public void irADashboard(ActionEvent event) {
        // RUTA ABSOLUTA
        cambiarPantalla(event, "/com/sandro/gestionpeliculas/DashboardView.fxml");
    }

    @FXML
    public void salir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres cerrar la aplicación?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    private void cambiarPantalla(ActionEvent event, String fxmlFile) {
        try {
            // Usamos getClass().getResource(fxmlFile) directamente porque fxmlFile ya empieza con "/"
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            // Pasamos el idioma para no perderlo
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
        alert.setTitle("Error de Navegación");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}