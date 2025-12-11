package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.DirectorDAO;
import com.sandro.gestionpeliculas.modelo.Director;
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

public class DirectoresController implements Initializable {

    // --- ELEMENTOS DE LA VISTA ---
    @FXML private TextField txtBuscar;

    @FXML private TableView<Director> tablaDirectores;
    @FXML private TableColumn<Director, Integer> colId;
    @FXML private TableColumn<Director, String> colNombre;

    @FXML private TextField txtNombre;
    @FXML private DatePicker dateNacimiento;
    @FXML private TextField txtNacionalidad;
    @FXML private TextField txtWeb;

    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnVolver;

    // --- VARIABLES GLOBALES ---
    private DirectorDAO directorDAO = new DirectorDAO();
    private Director directorSeleccionado = null;
    // Lista maestra para el buscador
    private ObservableList<Director> listaMaster = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Configurar las columnas (deben coincidir con los atributos de la clase Director)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // 2. Cargar datos de la BBDD
        cargarDirectores();

        // 3. Listener Tabla: Rellenar formulario al hacer clic
        tablaDirectores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                directorSeleccionado = newSelection;
                mostrarDetalles(directorSeleccionado);
            }
        });

        // 4. Listener Buscador: Filtrar al escribir
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarDirectores(newValue);
        });
    }

    // --- MÉTODOS AUXILIARES ---

    private void cargarDirectores() {
        tablaDirectores.getItems().clear();
        List<Director> lista = directorDAO.obtenerTodos();

        // Guardamos en la lista maestra y en la tabla
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
        dateNacimiento.setValue(d.getFechaNacimiento());

        // Cambiar botón a modo EDICIÓN
        btnGuardar.setText("ACTUALIZAR");
        btnGuardar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --- ACCIONES DE BOTONES ---

    @FXML
    void guardarDirector(ActionEvent event) {
        String nombre = txtNombre.getText();
        if (nombre == null || nombre.isEmpty()) {
            mostrarAlerta("Error", "El nombre es obligatorio.");
            return;
        }

        LocalDate fecha = dateNacimiento.getValue();
        if (fecha == null) {
            mostrarAlerta("Error", "La fecha de nacimiento es obligatoria.");
            return;
        }

        String nacionalidad = txtNacionalidad.getText();
        String web = txtWeb.getText();

        // LÓGICA: ¿CREAR O ACTUALIZAR?
        if (directorSeleccionado == null) {
            // MODO CREAR (ID 0)
            Director nuevo = new Director(0, nombre, fecha, nacionalidad, web);
            if (directorDAO.insertar(nuevo)) {
                System.out.println("✅ Director creado.");
                limpiarFormulario(null);
                cargarDirectores();
            }
        } else {
            // MODO ACTUALIZAR
            Director editado = new Director(directorSeleccionado.getId(), nombre, fecha, nacionalidad, web);
            if (directorDAO.actualizar(editado)) {
                System.out.println("🔄 Director actualizado.");
                limpiarFormulario(null);
                cargarDirectores();
            }
        }
    }

    @FXML
    void eliminarDirector(ActionEvent event) {
        if (directorSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un director para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Director");
        confirm.setHeaderText("¿Eliminar a " + directorSeleccionado.getNombre() + "?");
        confirm.setContentText("CUIDADO: Sus películas se quedarán sin director asignado.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (directorDAO.eliminar(directorSeleccionado.getId())) {
                System.out.println("🗑️ Director eliminado.");
                limpiarFormulario(null);
                cargarDirectores();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar.");
            }
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombre.setText("");
        txtNacionalidad.setText("");
        txtWeb.setText("");
        dateNacimiento.setValue(null);

        tablaDirectores.getSelectionModel().clearSelection();
        directorSeleccionado = null;

        // Resetear botón a modo GUARDAR
        btnGuardar.setText("GUARDAR");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}