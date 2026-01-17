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

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML private ProgressBar barraProgreso;
    @FXML private Label lblEstado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
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

                Platform.runLater(this::abrirMenuPrincipal);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void actualizarBarra(double progreso, String texto) {
        Platform.runLater(() -> {
            barraProgreso.setProgress(progreso);
            lblEstado.setText(texto);
        });
    }

    private void abrirMenuPrincipal() {
        try {
            ResourceBundle bundle = null;
            try {
                bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");
            } catch (MissingResourceException e) {
                try {
                    bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.textos");
                } catch (MissingResourceException e2) {
                    System.out.println("⚠️ No se encontró archivo de idioma, cargando sin textos.");
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sandro/gestionpeliculas/MenuPrincipal.fxml"));

            if (bundle != null) {
                loader.setResources(bundle);
            }

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Cine AA1");
            stage.show();

            if (lblEstado.getScene() != null) {
                Stage myStage = (Stage) lblEstado.getScene().getWindow();
                myStage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ ERROR FATAL: No se pudo abrir MenuPrincipal.fxml");
            System.out.println("Causa: " + e.getMessage());
        }
    }
}