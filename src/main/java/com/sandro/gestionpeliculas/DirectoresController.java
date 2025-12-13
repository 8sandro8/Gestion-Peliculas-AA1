package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.DirectorDAO;
import com.sandro.gestionpeliculas.modelo.Director;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DirectoresController implements Initializable {

    // --- ELEMENTOS FXML ---
    @FXML private TextField txtBuscar;
    @FXML private TableView<Director> tablaDirectores;
    @FXML private TableColumn<Director, Integer> colId;
    @FXML private TableColumn<Director, String> colNombre;
    @FXML private TableColumn<Director, String> colNacionalidad;

    @FXML private TextField txtNombre;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtNacionalidad;
    @FXML private TextField txtWeb;

    // --- VARIABLES ---
    private DirectorDAO directorDAO = new DirectorDAO();
    private Director directorSeleccionado = null;
    private ObservableList<Director> listaMaster = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        cargarDirectores();

        // Listener de selección
        tablaDirectores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                directorSeleccionado = newSel;
                mostrarDetalles(directorSeleccionado);
            }
        });

        // Listener del buscador
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarDirectores(newVal));
    }

    private void cargarDirectores() {
        tablaDirectores.getItems().clear();
        // Usamos obtenerTodos() que ya arreglamos ayer en el DAO
        List<Director> lista = directorDAO.obtenerTodos();
        listaMaster = FXCollections.observableArrayList(lista);
        tablaDirectores.setItems(listaMaster);
    }

    private void filtrarDirectores(String texto) {
        if (texto == null || texto.isEmpty()) {
            tablaDirectores.setItems(listaMaster);
            return;
        }
        ObservableList<Director> filtro = FXCollections.observableArrayList();
        for (Director d : listaMaster) {
            if (d.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtro.add(d);
            }
        }
        tablaDirectores.setItems(filtro);
    }

    private void mostrarDetalles(Director d) {
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

        if (directorSeleccionado == null) {
            // CREAR
            // Usamos el constructor completo que arreglamos ayer
            Director nuevo = new Director(0, nombre, fecha, web, nacionalidad);
            if (directorDAO.insertar(nuevo)) {
                mostrarAlerta("Éxito", "Director guardado correctamente");
                limpiarFormulario(null);
                cargarDirectores();
            } else {
                mostrarAlerta("Error", "No se pudo guardar");
            }
        } else {
            // ACTUALIZAR
            Director editado = new Director(directorSeleccionado.getId(), nombre, fecha, web, nacionalidad);
            if (directorDAO.actualizar(editado)) {
                mostrarAlerta("Éxito", "Director actualizado");
                limpiarFormulario(null);
                cargarDirectores();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar");
            }
        }
    }

    @FXML
    void eliminarDirector(ActionEvent event) {
        if (directorSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un director primero");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar");
        confirm.setHeaderText("¿Borrar a " + directorSeleccionado.getNombre() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (directorDAO.eliminar(directorSeleccionado.getId())) {
                mostrarAlerta("Eliminado", "Director eliminado");
                limpiarFormulario(null);
                cargarDirectores();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar (quizás tiene películas asignadas)");
            }
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
        fileChooser.setTitle("Guardar Archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        fileChooser.setInitialFileName("directores.csv");

        Stage stage = (Stage) txtBuscar.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID;Nombre;Fecha Nacimiento;Nacionalidad;Web");
                writer.newLine();

                for (Director d : listaMaster) {
                    String fechaStr = (d.getFechaNacimiento() != null) ? d.getFechaNacimiento().toString() : "";
                    String webStr = (d.getWebOficial() != null) ? d.getWebOficial() : "";

                    writer.write(d.getId() + ";" + d.getNombre() + ";" + fechaStr + ";" + d.getNacionalidad() + ";" + webStr);
                    writer.newLine();
                }
                mostrarAlerta("Éxito", "Directores exportados correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Error al exportar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo volver al menú: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}