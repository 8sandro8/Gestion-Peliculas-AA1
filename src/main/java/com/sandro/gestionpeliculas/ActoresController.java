package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.ActorDAO;
import com.sandro.gestionpeliculas.modelo.Actor;
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
import java.util.ResourceBundle;

public class ActoresController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Actor> tablaActores;
    @FXML private TableColumn<Actor, Integer> colId;
    @FXML private TableColumn<Actor, String> colNombre;
    @FXML private TableColumn<Actor, String> colNacionalidad;

    @FXML private TextField txtNombre;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtNacionalidad;
    @FXML private ImageView imgFoto;

    private ActorDAO actorDAO = new ActorDAO();
    private Actor actorSeleccionado = null;
    private File archivoImagenSeleccionado;

    private ObservableList<Actor> listaMaster = FXCollections.observableArrayList();
    private FilteredList<Actor> listaFiltrada;

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
            listaFiltrada.setPredicate(actor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (actor.getNombre() != null && actor.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (actor.getNacionalidad() != null && actor.getNacionalidad().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Actor> sortedData = new SortedList<>(listaFiltrada);
        sortedData.comparatorProperty().bind(tablaActores.comparatorProperty());
        tablaActores.setItems(sortedData);

        cargarActores();

        tablaActores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                actorSeleccionado = newSel;
                mostrarDetalles(actorSeleccionado);
            }
        });
    }

    private void cargarActores() {
        listaMaster.clear();
        listaMaster.addAll(actorDAO.obtenerTodos());
    }

    private void mostrarDetalles(Actor a) {
        if (a == null) return;
        txtNombre.setText(a.getNombre());
        txtNacionalidad.setText(a.getNacionalidad());
        if (dpFechaNacimiento != null) dpFechaNacimiento.setValue(a.getFechaNacimiento());

        archivoImagenSeleccionado = null;
        if (imgFoto != null) {
            imgFoto.setImage(null);

            if (a.getFotoUrl() != null && !a.getFotoUrl().isEmpty()) {
                try {
                    File file = new File(a.getFotoUrl());
                    if (file.exists()) {
                        imgFoto.setImage(new Image(file.toURI().toString()));
                    }
                } catch (Exception e) {
                    System.out.println("Error cargando imagen: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            archivoImagenSeleccionado = file;
            if (imgFoto != null) {
                imgFoto.setImage(new Image(file.toURI().toString()));
            }
        }
    }

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
            mostrarAlerta("Error", "El nombre es obligatorio");
            return;
        }

        LocalDate fecha = dpFechaNacimiento.getValue();
        String nacionalidad = txtNacionalidad.getText();

        Actor actorGestor;
        if (actorSeleccionado == null) {
            actorGestor = new Actor();
        } else {
            actorGestor = actorSeleccionado;
        }

        actorGestor.setNombre(nombre);
        actorGestor.setFechaNacimiento(fecha);
        actorGestor.setNacionalidad(nacionalidad);

        if (archivoImagenSeleccionado != null) {
            String ruta = copiarImagenAlProyecto(archivoImagenSeleccionado);
            if (ruta != null) {
                actorGestor.setFotoUrl(ruta);
            }
        }

        boolean exito;
        if (actorSeleccionado == null) {
            exito = actorDAO.insertar(actorGestor);
        } else {
            exito = actorDAO.actualizar(actorGestor);
        }

        if (exito) {
            mostrarAlerta("Éxito", "Actor guardado correctamente");
            limpiarFormulario(null);
            cargarActores();
        } else {
            mostrarAlerta("Error", "No se pudo guardar");
        }
    }

    @FXML
    void eliminarActor(ActionEvent event) {
        if (actorSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un actor primero");
            return;
        }
        if (actorDAO.eliminar(actorSeleccionado.getId())) {
            mostrarAlerta("Eliminado", "Actor eliminado");
            limpiarFormulario(null);
            cargarActores();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar");
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtNombre.clear();
        txtNacionalidad.clear();
        dpFechaNacimiento.setValue(null);
        if (imgFoto != null) imgFoto.setImage(null);
        tablaActores.getSelectionModel().clearSelection();
        actorSeleccionado = null;
        archivoImagenSeleccionado = null;
    }

    @FXML
    void exportarCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fileChooser.setInitialFileName("actores.csv");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID;Nombre;Nacionalidad");
                writer.newLine();
                for (Actor a : listaMaster) {
                    writer.write(a.getId() + ";" + a.getNombre() + ";" + a.getNacionalidad());
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
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo volver al menú: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}