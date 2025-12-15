package com.sandro.gestionpeliculas.modelo; // 1. OJO: Ahora está en el paquete 'modelo'

import com.sandro.gestionpeliculas.dao.EstadisticasDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label lblTotalPelis;
    @FXML private Label lblTotalActores;
    @FXML private PieChart graficoGeneros;

    private EstadisticasDAO estadisticasDAO = new EstadisticasDAO();
    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle; // Guardamos el idioma
        cargarDatos();
    }

    private void cargarDatos() {
        // 1. Cargar números grandes
        int totalPelis = estadisticasDAO.contarPeliculas();
        int totalActores = estadisticasDAO.contarActores();

        lblTotalPelis.setText(String.valueOf(totalPelis));
        lblTotalActores.setText(String.valueOf(totalActores));

        // 2. Cargar Gráfico de Quesitos (PieChart)
        Map<String, Integer> datosGeneros = estadisticasDAO.contarPeliculasPorGenero();

        ObservableList<PieChart.Data> datosGrafico = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : datosGeneros.entrySet()) {
            String nombreGenero = entry.getKey();
            Integer cantidad = entry.getValue();

            // Añadimos el dato: "Terror (5)"
            datosGrafico.add(new PieChart.Data(nombreGenero + " (" + cantidad + ")", cantidad));
        }

        graficoGeneros.setData(datosGrafico);
        graficoGeneros.setTitle("Géneros");
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            // 2. OJO: Usamos la ruta absoluta "/" para ir a buscar el FXML a la raíz de resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sandro/gestionpeliculas/MenuPrincipal.fxml"));

            // Pasamos los recursos para no perder el idioma seleccionado
            loader.setResources(this.resources);

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al volver al menú: " + e.getMessage());
        }
    }
}