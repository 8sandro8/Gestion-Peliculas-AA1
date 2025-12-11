package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.ActorDAO;
import com.sandro.gestionpeliculas.modelo.Actor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ActoresController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Actor> tablaActores;
    @FXML private TableColumn<Actor, Integer> colId;
    @FXML private TableColumn<Actor, String> colNombre;
    @FXML private TableColumn<Actor, String> colNacionalidad;

    @FXML private TextField txtNombre;
    @FXML private DatePicker dateNacimiento;
    @FXML private TextField txtNacionalidad;

    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnVolver;

    private ActorDAO actorDAO = new ActorDAO();
    private Actor actorSeleccionado = null;
    private ObservableList<Actor> listaMaster = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        cargarActores();

        tablaActores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                actorSeleccionado = newSelection;
                mostrarDetalles(actorSeleccionado);
            }
        });

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
        dateNacimiento.setValue(a.getFechaNacimiento());

        btnGuardar.setText("ACTUALIZAR");
        btnGuardar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    @FXML
    void guardarActor(ActionEvent event) {
        String nombre = txtNombre.getText();
        if (nombre == null || nombre.isEmpty()) return;

        LocalDate fecha = dateNacimiento.getValue();
        if (fecha == null) return;

        String nacionalidad = txtNacionalidad.getText();

        if (actorSeleccionado == null) {
            // CREAR
            Actor nuevo = new Actor(0, nombre, fecha, nacionalidad);
            if (actorDAO.insertar(nuevo)) {
                limpiarFormulario(null);
                cargarActores();
            }
        } else {
            // ACTUALIZAR
            Actor editado = new Actor(actorSeleccionado.getId(), nombre, fecha, nacionalidad);
            if (actorDAO.actualizar(editado)) {
                limpiarFormulario(null);
                cargarActores();
            }
        }
    }

    @FXML
    void eliminarActor(ActionEvent event) {
        if (actorSeleccionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar");
        confirm.setHeaderText("¿Borrar a " + actorSeleccionado.getNombre() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (actorDAO.eliminar(actorSeleccionado.getId())) {
                limpiarFormulario(null);
                cargarActores();
            }
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombre.setText("");
        txtNacionalidad.setText("");
        dateNacimiento.setValue(null);

        tablaActores.getSelectionModel().clearSelection();
        actorSeleccionado = null;

        btnGuardar.setText("GUARDAR ACTOR");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}