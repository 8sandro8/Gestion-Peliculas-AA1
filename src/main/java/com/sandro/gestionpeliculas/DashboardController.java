package com.sandro.gestionpeliculas;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDatos();
    }

    private void cargarDatos() {
        int totalPelis = estadisticasDAO.contarPeliculas();
        int totalActores = estadisticasDAO.contarActores();

        lblTotalPelis.setText(String.valueOf(totalPelis));
        lblTotalActores.setText(String.valueOf(totalActores));

        Map<String, Integer> datosGeneros = estadisticasDAO.contarPeliculasPorGenero();
        ObservableList<PieChart.Data> datosGrafico = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : datosGeneros.entrySet()) {
            String nombreGenero = entry.getKey();
            Integer cantidad = entry.getValue();
            datosGrafico.add(new PieChart.Data(nombreGenero + " (" + cantidad + ")", cantidad));
        }

        graficoGeneros.setData(datosGrafico);
        graficoGeneros.setTitle("Géneros");
        graficoGeneros.setLabelsVisible(true);
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            ResourceBundle bundle = null;
            try {
                bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");
            } catch (Exception e1) {
                try {
                    bundle = ResourceBundle.getBundle("mensajes");
                } catch (Exception e2) {
                    System.out.println("No se encontró el archivo de idiomas.");
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sandro/gestionpeliculas/MenuPrincipal.fxml"));

            if (bundle != null) {
                loader.setResources(bundle);
            }

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}