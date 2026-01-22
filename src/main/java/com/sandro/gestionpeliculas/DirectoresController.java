package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.DirectorDAO;
import com.sandro.gestionpeliculas.modelo.Director;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DirectoresController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Director> tablaDirectores;
    @FXML private TableColumn<Director, Integer> colId;
    @FXML private TableColumn<Director, String> colNombre;
    @FXML private TableColumn<Director, String> colNacionalidad;

    @FXML private TextField txtNombre;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtNacionalidad;
    @FXML private TextField txtWeb;

    private DirectorDAO directorDAO = new DirectorDAO();
    private Director directorSeleccionado = null;

    private ObservableList<Director> listaMaster = FXCollections.observableArrayList();
    private FilteredList<Director> listaFiltrada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));

        colNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));

        colNacionalidad.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNacionalidad()));

        listaFiltrada = new FilteredList<>(listaMaster, b -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(director -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (director.getNombre() != null && director.getNombre().toLowerCase().contains(lowerCaseFilter)) return true;
                if (director.getNacionalidad() != null && director.getNacionalidad().toLowerCase().contains(lowerCaseFilter)) return true;

                return false;
            });
        });

        SortedList<Director> sortedData = new SortedList<>(listaFiltrada);
        sortedData.comparatorProperty().bind(tablaDirectores.comparatorProperty());
        tablaDirectores.setItems(sortedData);

        cargarDirectores();

        tablaDirectores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                directorSeleccionado = newSel;
                mostrarDetalles(directorSeleccionado);
            }
        });
    }

    private void cargarDirectores() {
        listaMaster.clear();
        listaMaster.addAll(directorDAO.obtenerTodos());
    }

    private void mostrarDetalles(Director d) {
        if (d == null) return;
        txtNombre.setText(d.getNombre());
        txtNacionalidad.setText(d.getNacionalidad());
        txtWeb.setText(d.getWebOficial());
        dpFechaNacimiento.setValue(d.getFechaNacimiento());
    }

    @FXML
    void guardarDirector(ActionEvent event) {
        String nombre = txtNombre.getText();
        if (nombre == null || nombre.isEmpty()) {
            mostrarAlerta("Error", "El nombre es obligatorio");
            return;
        }

        LocalDate fecha = dpFechaNacimiento.getValue();
        String nacionalidad = txtNacionalidad.getText();
        String web = txtWeb.getText();

        Director dirGestor;
        if (directorSeleccionado == null) {
            dirGestor = new Director();
        } else {
            dirGestor = directorSeleccionado;
        }

        // Ajusta los setters según tu clase Director
        dirGestor.setNombre(nombre);
        dirGestor.setFechaNacimiento(fecha);
        dirGestor.setNacionalidad(nacionalidad);
        dirGestor.setWebOficial(web);

        boolean exito;
        if (directorSeleccionado == null) {
            exito = directorDAO.insertar(dirGestor);
        } else {
            exito = directorDAO.actualizar(dirGestor);
        }

        if (exito) {
            mostrarAlerta("Éxito", "Director guardado correctamente");
            limpiarFormulario(null);
            cargarDirectores();
        } else {
            mostrarAlerta("Error", "No se pudo guardar");
        }
    }

    @FXML
    void eliminarDirector(ActionEvent event) {
        if (directorSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un director primero");
            return;
        }
        if (directorDAO.eliminar(directorSeleccionado.getId())) {
            mostrarAlerta("Eliminado", "Director eliminado");
            limpiarFormulario(null);
            cargarDirectores();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar");
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombre.clear();
        txtNacionalidad.clear();
        txtWeb.clear();
        dpFechaNacimiento.setValue(null);
        tablaDirectores.getSelectionModel().clearSelection();
        directorSeleccionado = null;
    }

    @FXML
    void exportarCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fileChooser.setInitialFileName("directores.csv");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID;Nombre;Nacionalidad;Web");
                writer.newLine();
                for (Director d : listaMaster) {
                    writer.write(d.getId() + ";" + d.getNombre() + ";" + d.getNacionalidad() + ";" + d.getWebOficial());
                    writer.newLine();
                }
                mostrarAlerta("Éxito", "Exportado correctamente.");
            } catch (IOException e) {
                mostrarAlerta("Error", "Fallo al exportar.");
            }
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            // ResourceBundle bundle = ResourceBundle.getBundle("...");
            // loader.setResources(bundle);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al volver: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}