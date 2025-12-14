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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    // --- NUEVO: IMAGEN ---
    @FXML private ImageView imgFoto;

    // --- VARIABLES DE DATOS ---
    private ActorDAO actorDAO = new ActorDAO();
    private Actor actorSeleccionado = null;
    private ObservableList<Actor> listaMaster = FXCollections.observableArrayList();
    private ResourceBundle resources;

    // Variable para la foto temporal
    private File archivoImagenSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

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
        dpFechaNacimiento.setValue(a.getFechaNacimiento());

        // --- LOGICA FOTO ---
        archivoImagenSeleccionado = null;
        if (a.getFotoUrl() != null && !a.getFotoUrl().isEmpty()) {
            try {
                File file = new File(a.getFotoUrl());
                if (file.exists()) {
                    imgFoto.setImage(new Image(file.toURI().toString()));
                } else {
                    imgFoto.setImage(null);
                }
            } catch (Exception e) {
                imgFoto.setImage(null);
            }
        } else {
            imgFoto.setImage(null);
        }
    }

    // --- NUEVO: SELECCIONAR IMAGEN ---
    @FXML
    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            archivoImagenSeleccionado = file;
            imgFoto.setImage(new Image(file.toURI().toString()));
        }
    }

    // --- NUEVO: COPIAR IMAGEN ---
    private String copiarImagenAlProyecto(File archivoOriginal) {
        try {
            Path carpetaDestino = Paths.get("imagenes");
            if (!Files.exists(carpetaDestino)) {
                Files.createDirectories(carpetaDestino);
            }

            String extension = "";
            int i = archivoOriginal.getName().lastIndexOf('.');
            if (i > 0) extension = archivoOriginal.getName().substring(i);

            String nombreFinal = "actor_" + System.currentTimeMillis() + extension;
            Path rutaDestino = carpetaDestino.resolve(nombreFinal);

            Files.copy(archivoOriginal.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            return rutaDestino.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

        // Creamos el objeto (temporalmente sin ID si es nuevo)
        Actor actorAGuardar = new Actor();
        actorAGuardar.setNombre(nombre);
        actorAGuardar.setFechaNacimiento(fecha);
        actorAGuardar.setNacionalidad(nacionalidad);

        // --- PROCESAR FOTO ---
        if (archivoImagenSeleccionado != null) {
            String ruta = copiarImagenAlProyecto(archivoImagenSeleccionado);
            if (ruta != null) actorAGuardar.setFotoUrl(ruta);
        } else if (actorSeleccionado != null) {
            // Mantener la foto anterior si no se ha cambiado
            actorAGuardar.setFotoUrl(actorSeleccionado.getFotoUrl());
        }

        if (actorSeleccionado == null) {
            // NUEVO
            if (actorDAO.insertar(actorAGuardar)) {
                mostrarAlerta("alerta.titulo.info", "Actor guardado correctamente");
                limpiarFormulario(null);
                cargarActores();
            } else {
                mostrarAlerta("alerta.titulo.error", "No se pudo guardar el actor");
            }
        } else {
            // EDITAR
            actorAGuardar.setId(actorSeleccionado.getId()); // Recuperamos el ID
            if (actorDAO.actualizar(actorAGuardar)) {
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
        try {
            if (resources != null && resources.containsKey("alerta.titulo.aviso")) {
                confirm.setTitle(resources.getString("alerta.titulo.aviso"));
            } else { confirm.setTitle("Aviso"); }
        } catch(Exception e) { confirm.setTitle("Confirmar"); }

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

        // Limpiar foto
        imgFoto.setImage(null);
        archivoImagenSeleccionado = null;

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