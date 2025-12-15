package com.sandro.gestionpeliculas.modelo; // ✅ Correcto: carpeta modelo

import com.sandro.gestionpeliculas.dao.ActorDAO;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter; // Añadido
import java.io.File;
import java.io.FileWriter;     // Añadido
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.Optional;

public class ActoresController implements Initializable {

    // --- ELEMENTOS FXML ---
    @FXML private TableView<Actor> tablaActores;
    @FXML private TableColumn<Actor, Integer> colId;
    @FXML private TableColumn<Actor, String> colNombre;
    @FXML private TableColumn<Actor, String> colNacionalidad;

    @FXML private TextField txtNombre;
    @FXML private TextField txtNacionalidad;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private ImageView imgFoto;

    // --- VARIABLES ---
    private ObservableList<Actor> listaActores;
    private ActorDAO actorDAO;
    private File archivoImagenSeleccionado;
    private ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        actorDAO = new ActorDAO();
        listaActores = FXCollections.observableArrayList();

        configurarColumnas();
        cargarActores();

        // Listener para selección
        tablaActores.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> mostrarDetalles(newSelection)
        );
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colNacionalidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNacionalidad()));
    }

    private void cargarActores() {
        listaActores.clear();
        listaActores.addAll(actorDAO.listarTodos());
        tablaActores.setItems(listaActores);
    }

    private void mostrarDetalles(Actor a) {
        if (a == null) return;
        txtNombre.setText(a.getNombre());
        txtNacionalidad.setText(a.getNacionalidad());
        if (a.getFechaNacimiento() != null) {
            dpFechaNacimiento.setValue(a.getFechaNacimiento());
        } else {
            dpFechaNacimiento.setValue(null);
        }

        archivoImagenSeleccionado = null;
        if (a.getFotoUrl() != null && !a.getFotoUrl().isEmpty()) {
            try {
                File file = new File(a.getFotoUrl());
                if (file.exists()) {
                    imgFoto.setImage(new Image(file.toURI().toString()));
                } else {
                    imgFoto.setImage(null);
                }
            } catch (Exception e) { imgFoto.setImage(null); }
        } else {
            imgFoto.setImage(null);
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtNombre.clear();
        txtNacionalidad.clear();
        dpFechaNacimiento.setValue(null);
        imgFoto.setImage(null);
        archivoImagenSeleccionado = null;
        tablaActores.getSelectionModel().clearSelection();
    }

    @FXML
    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto Actor");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            archivoImagenSeleccionado = file;
            imgFoto.setImage(new Image(file.toURI().toString()));
        }
    }

    private String copiarImagen(File archivo) {
        try {
            Path destino = Paths.get("imagenes_actores");
            if (!Files.exists(destino)) Files.createDirectories(destino);
            String nombre = "actor_" + System.currentTimeMillis() + "_" + archivo.getName();
            Path rutaFinal = destino.resolve(nombre);
            Files.copy(archivo.toPath(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);
            return rutaFinal.toString();
        } catch (Exception e) { return null; }
    }

    @FXML
    public void guardarActor() {
        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta("Error", "El nombre es obligatorio", Alert.AlertType.ERROR);
            return;
        }

        Actor actor = tablaActores.getSelectionModel().getSelectedItem();
        boolean esNuevo = (actor == null);
        if (esNuevo) actor = new Actor();

        actor.setNombre(txtNombre.getText());
        actor.setNacionalidad(txtNacionalidad.getText());
        actor.setFechaNacimiento(dpFechaNacimiento.getValue());

        if (archivoImagenSeleccionado != null) {
            String ruta = copiarImagen(archivoImagenSeleccionado);
            if (ruta != null) actor.setFotoUrl(ruta);
        }

        if (esNuevo) {
            if (actorDAO.insertar(actor)) mostrarAlerta("Info", "Actor guardado", Alert.AlertType.INFORMATION);
        } else {
            if (actorDAO.actualizar(actor)) mostrarAlerta("Info", "Actor actualizado", Alert.AlertType.INFORMATION);
        }
        cargarActores();
        limpiarFormulario();
    }

    @FXML
    public void eliminarActor() {
        Actor actor = tablaActores.getSelectionModel().getSelectedItem();
        if (actor == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Borrar a " + actor.getNombre() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            actorDAO.eliminar(actor.getId());
            cargarActores();
            limpiarFormulario();
        }
    }

    // --- ¡ESTE MÉTODO FALTABA Y ES CRUCIAL PORQUE EL FXML LO LLAMA! ---
    @FXML
    public void exportarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Listado de Actores");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        fileChooser.setInitialFileName("actores_backup.csv");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID;NOMBRE;NACIONALIDAD;FECHA_NACIMIENTO\n");
                for (Actor a : listaActores) {
                    writer.write(String.format("%d;%s;%s;%s\n",
                            a.getId(), a.getNombre(), a.getNacionalidad(), a.getFechaNacimiento()));
                }
                mostrarAlerta("Info", "Exportado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "Fallo al exportar: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sandro/gestionpeliculas/MenuPrincipal.fxml"));
            loader.setResources(this.resources);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}