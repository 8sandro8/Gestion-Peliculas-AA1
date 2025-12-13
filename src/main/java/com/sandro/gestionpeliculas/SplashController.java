package com.sandro.gestionpeliculas;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML private ProgressBar barraProgreso;
    @FXML private Label lblEstado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Creamos un hilo secundario para simular la carga sin congelar la ventana
        new Thread(() -> {
            try {
                // Simulación de pasos de carga
                actualizarBarra(0.1, "Cargando configuración...");
                Thread.sleep(600);

                actualizarBarra(0.3, "Conectando a Base de Datos...");
                Thread.sleep(800);

                actualizarBarra(0.6, "Cargando imágenes...");
                Thread.sleep(600);

                actualizarBarra(0.9, "Iniciando interfaz...");
                Thread.sleep(400);

                actualizarBarra(1.0, "¡Listo!");
                Thread.sleep(300);

                // Cuando termine, abrimos el menú principal (esto debe hacerse en el hilo de JavaFX)
                Platform.runLater(this::abrirMenuPrincipal);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void actualizarBarra(double progreso, String texto) {
        // Actualizamos la interfaz desde el hilo principal
        Platform.runLater(() -> {
            barraProgreso.setProgress(progreso);
            lblEstado.setText(texto);
        });
    }

    private void abrirMenuPrincipal() {
        try {
            // Cargar idioma por defecto
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();

            // Crear la nueva ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Movie Manager AA1");
            stage.show();

            // Cerrar la ventana del Splash
            Stage myStage = (Stage) lblEstado.getScene().getWindow();
            myStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}