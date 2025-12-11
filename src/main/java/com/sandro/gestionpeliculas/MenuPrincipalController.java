package com.sandro.gestionpeliculas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuPrincipalController {

    public Button btnPeliculas;
    public Button btnArtistas;
    public Button btnSalir;

    @FXML
    public void irAPeliculas(ActionEvent actionEvent) {
        try {
            // 1. Cargar la vista de Películas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PeliculasView.fxml"));
            Parent root = loader.load();

            // 2. Conseguir la ventana actual (el escenario)
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            // 3. Cambiar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("🎬 Navegando a Gestión de Películas...");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error al cambiar de pantalla.");
        }
    }

    @FXML
    public void irAArtistas(ActionEvent actionEvent) {
        try {
            // AHORA REDIRIGE A ACTORES
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActoresView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void cerrarApp(ActionEvent event) {
        System.out.println("👋 Cerrando aplicación...");
        System.exit(0);
    }
}