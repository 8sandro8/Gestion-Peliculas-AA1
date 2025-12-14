package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.ActorDAO;
import com.sandro.gestionpeliculas.modelo.Actor;
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

public class ActoresController implements Initializable {

    // --- ELEMENTOS DE LA VISTA ---
    @FXML private TextField txtBuscar;
    @FXML private TableView<Actor> tablaActores;
    @FXML private TableColumn<Actor, Integer> colId;
    @FXML private TableColumn<Actor, String> colNombre;
    @FXML private TableColumn<Actor, String> colNacionalidad;

    @FXML private TextField txtNombre;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtNacionalidad;

    // --- VARIABLES DE DATOS ---
    private ActorDAO actorDAO = new ActorDAO();
    private Actor actorSeleccionado = null;
    private ObservableList<Actor> listaMaster = FXCollections.observableArrayList();

    // --- VARIABLE IDIOMA ---
    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // CAPTURAR EL IDIOMA ACTUAL
        this.resources = resourceBundle;

        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        cargarActores();

        // Listener para seleccionar de la tabla
        tablaActores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                actorSeleccionado = newSelection;
                mostrarDetalles(actorSeleccionado);
            }
        });

        // Listener para el buscador
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarActores(newVal));
    }

    private void cargarActores() {
        tablaActores.getItems().clear();
        List<Actor> lista = actorDAO.obtenerTodos();
        listaMaster = FXCollections.observableArrayList(lista);
        tablaActores.setItems(listaMaster);
    }

    private void filtrarActores(String texto) {
        if (texto == null || texto.isEmpty()) {
            tablaActores.setItems(listaMaster);
            return;
        }
        ObservableList<Actor> filtro = FXCollections.observableArrayList();
        for (Actor a : listaMaster) {
            if (a.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtro.add(a);
            }
        }
        tablaActores.setItems(filtro);
    }

    private void mostrarDetalles(Actor a) {
        txtNombre.setText(a.getNombre());
        txtNacionalidad.setText(a.getNacionalidad());
        dpFechaNacimiento.setValue(a.getFechaNacimiento());
    }

    @FXML
    void guardarActor(ActionEvent event) {
        String nombre = txtNombre.getText();
        if (nombre == null || nombre.isEmpty()) {
            mostrarAlerta("alerta.titulo.error", "El nombre es obligatorio");
            return;
        }

        LocalDate fecha = dpFechaNacimiento.getValue();
        String nacionalidad = txtNacionalidad.getText();

        if (actorSeleccionado == null) {
            // CREAR NUEVO
            Actor nuevo = new Actor(0, nombre, fecha, nacionalidad);
            if (actorDAO.insertar(nuevo)) {
                mostrarAlerta("alerta.titulo.info", "Actor guardado correctamente");
                limpiarFormulario(null);
                cargarActores();
            } else {
                mostrarAlerta("alerta.titulo.error", "No se pudo guardar el actor");
            }
        } else {
            // ACTUALIZAR EXISTENTE
            Actor editado = new Actor(actorSeleccionado.getId(), nombre, fecha, nacionalidad);
            if (actorDAO.actualizar(editado)) {
                mostrarAlerta("alerta.titulo.info", "Actor actualizado correctamente");
                limpiarFormulario(null);
                cargarActores();
            } else {
                mostrarAlerta("alerta.titulo.error", "No se pudo actualizar");
            }
        }
    }

    @FXML
    void eliminarActor(ActionEvent event) {
        if (actorSeleccionado == null) {
            mostrarAlerta("alerta.titulo.aviso", "Selecciona un actor para eliminar");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        // Usamos el recurso para el título
        try {
            confirm.setTitle(resources.getString("alerta.titulo.aviso"));
        } catch (Exception e) { confirm.setTitle("Confirmar"); }

        confirm.setHeaderText(null);
        confirm.setContentText("¿Borrar a " + actorSeleccionado.getNombre() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (actorDAO.eliminar(actorSeleccionado.getId())) {
                mostrarAlerta("alerta.titulo.info", "Actor eliminado correctamente");
                limpiarFormulario(null);
                cargarActores();
            } else {
                mostrarAlerta("alerta.titulo.error", "No se pudo eliminar (puede que tenga películas asociadas)");
            }
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombre.clear();
        txtNacionalidad.clear();
        dpFechaNacimiento.setValue(null);

        tablaActores.getSelectionModel().clearSelection();
        actorSeleccionado = null;
    }

    @FXML
    void exportarCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        fileChooser.setInitialFileName("actores.csv");

        Stage stage = (Stage) txtBuscar.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID;Nombre;Fecha Nacimiento;Nacionalidad");
                writer.newLine();

                for (Actor a : listaMaster) {
                    String fechaStr = (a.getFechaNacimiento() != null) ? a.getFechaNacimiento().toString() : "";
                    writer.write(a.getId() + ";" + a.getNombre() + ";" + fechaStr + ";" + a.getNacionalidad());
                    writer.newLine();
                }
                mostrarAlerta("alerta.titulo.info", "Datos de actores exportados correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("alerta.titulo.error", "No se pudo guardar el archivo: " + e.getMessage());
            }
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            // PASAMOS EL IDIOMA ACTUAL AL VOLVER
            loader.setResources(this.resources);

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("alerta.titulo.error", "No se pudo volver al menú: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String claveTitulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        // Intentamos traducir el título
        try {
            if (resources != null && resources.containsKey(claveTitulo)) {
                alerta.setTitle(resources.getString(claveTitulo));
            } else {
                alerta.setTitle(claveTitulo);
            }
        } catch (Exception e) {
            alerta.setTitle(claveTitulo);
        }

        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}