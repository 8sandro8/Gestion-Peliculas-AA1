package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.PeliculaDAO;
import com.sandro.gestionpeliculas.dao.DirectorDAO;
import com.sandro.gestionpeliculas.modelo.Pelicula;
import com.sandro.gestionpeliculas.modelo.Director;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.util.StringConverter;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Optional;

public class PeliculasController implements Initializable {

    // --- ELEMENTOS DE LA VISTA (FXML) ---
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, Integer> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, String> colDirector;
    @FXML private TableColumn<Pelicula, Integer> colAnio;
    @FXML private TableColumn<Pelicula, String> colGenero;

    @FXML private TextField txtBuscar;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtGenero;
    @FXML private ComboBox<Director> comboDirector;
    @FXML private ImageView imgPoster;

    // --- VARIABLES DE DATOS ---
    private ObservableList<Pelicula> listaPeliculas;
    private PeliculaDAO peliculaDAO;
    private DirectorDAO directorDAO;
    private File imagenSeleccionada;

    // --- VARIABLE PARA IDIOMA ---
    private ResourceBundle resources;

    // --- INICIALIZACIÓN ---
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Capturamos el idioma actual para usarlo en el código Java (alertas, etc)
        this.resources = resourceBundle;

        peliculaDAO = new PeliculaDAO();
        directorDAO = new DirectorDAO();
        listaPeliculas = FXCollections.observableArrayList();

        configurarTabla();
        configurarComboDirector();
        cargarDatos();
        configurarBuscador();

        // Listener: Al hacer clic en la tabla, rellenar el formulario
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalles(newSelection);
            }
        });
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        // Columna personalizada para Director
        colDirector.setCellValueFactory(cellData -> {
            Director d = cellData.getValue().getDirector();
            if (d != null) {
                return new javafx.beans.property.SimpleStringProperty(d.getNombre());
            } else {
                return new javafx.beans.property.SimpleStringProperty("Sin Director");
            }
        });
    }

    private void configurarComboDirector() {
        comboDirector.setConverter(new StringConverter<Director>() {
            @Override
            public String toString(Director director) {
                return (director != null) ? director.getNombre() : "";
            }

            @Override
            public Director fromString(String string) {
                return null;
            }
        });
    }

    private void cargarDatos() {
        listaPeliculas.clear();
        listaPeliculas.addAll(peliculaDAO.listarTodas());
        tablaPeliculas.setItems(listaPeliculas);

        try {
            comboDirector.setItems(FXCollections.observableArrayList(directorDAO.listarTodos()));
        } catch (Exception e) {
            System.out.println("Error al cargar directores: " + e.getMessage());
        }
    }

    private void configurarBuscador() {
        FilteredList<Pelicula> filtro = new FilteredList<>(listaPeliculas, b -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtro.setPredicate(pelicula -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if (pelicula.getTitulo() != null && pelicula.getTitulo().toLowerCase().contains(lowerCaseFilter)) return true;
                if (pelicula.getGenero() != null && pelicula.getGenero().toLowerCase().contains(lowerCaseFilter)) return true;

                return false;
            });
        });

        tablaPeliculas.setItems(filtro);
    }

    // --- MÉTODOS DEL FORMULARIO ---

    @FXML
    public void limpiarFormulario() {
        txtTitulo.clear();
        txtAnio.clear();
        txtDuracion.clear();
        txtGenero.clear();
        comboDirector.getSelectionModel().clearSelection();
        imgPoster.setImage(null);
        imagenSeleccionada = null;
        tablaPeliculas.getSelectionModel().clearSelection();
    }

    private void mostrarDetalles(Pelicula p) {
        if (p == null) return;

        txtTitulo.setText(p.getTitulo());
        txtAnio.setText(String.valueOf(p.getAnio()));
        txtDuracion.setText(String.valueOf(p.getDuracion()));
        txtGenero.setText(p.getGenero());
        comboDirector.setValue(p.getDirector());
    }

    @FXML
    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Póster");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagenSeleccionada = file;
            imgPoster.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    public void guardarPelicula() {
        if (txtTitulo.getText().isEmpty() || txtAnio.getText().isEmpty()) {
            mostrarAlerta("alerta.titulo.error", "El título y el año son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        try {
            String titulo = txtTitulo.getText();
            int anio = Integer.parseInt(txtAnio.getText());
            int duracion = 0;
            if (!txtDuracion.getText().isEmpty()) duracion = Integer.parseInt(txtDuracion.getText());

            String genero = txtGenero.getText();
            Director director = comboDirector.getValue();

            Pelicula peliculaGestor;
            Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                peliculaGestor = new Pelicula();
                peliculaGestor.setId(0);
            } else {
                peliculaGestor = seleccionada;
            }

            peliculaGestor.setTitulo(titulo);
            peliculaGestor.setDuracion(duracion);
            peliculaGestor.setGenero(genero);
            peliculaGestor.setDirector(director);
            peliculaGestor.setFechaLanzamiento(LocalDate.of(anio, 1, 1));
            peliculaGestor.setRating(5.0);
            peliculaGestor.setTieneOscar(false);

            if (seleccionada == null) {
                if (peliculaDAO.insertar(peliculaGestor)) {
                    mostrarAlerta("alerta.titulo.info", "Película guardada correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("alerta.titulo.error", "No se pudo guardar la película", Alert.AlertType.ERROR);
                }
            } else {
                if (peliculaDAO.actualizar(peliculaGestor)) {
                    mostrarAlerta("alerta.titulo.info", "Película actualizada correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("alerta.titulo.error", "No se pudo actualizar", Alert.AlertType.ERROR);
                }
            }

            cargarDatos();
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarAlerta("alerta.titulo.error", "El año y la duración deben ser números enteros", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("alerta.titulo.error", "Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void eliminarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("alerta.titulo.aviso", "Selecciona una película para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        // Usamos el texto del recurso para el título de la alerta
        confirm.setTitle(resources.getString("alerta.titulo.aviso"));
        confirm.setHeaderText(null);
        // Usamos el texto del recurso para la pregunta
        confirm.setContentText(resources.getString("alerta.confirmar.eliminar") + " \n(" + seleccionada.getTitulo() + ")");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (peliculaDAO.eliminar(seleccionada.getId())) {
                cargarDatos();
                limpiarFormulario();
                mostrarAlerta("alerta.titulo.info", "Película eliminada correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("alerta.titulo.error", "No se pudo eliminar la película", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void exportarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Listado de Películas");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        fileChooser.setInitialFileName("peliculas_backup.csv");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ID;TITULO;DIRECTOR;AÑO;DURACION;GENERO\n");
                for (Pelicula p : listaPeliculas) {
                    String nombreDirector = (p.getDirector() != null) ? p.getDirector().getNombre() : "Sin Director";
                    String linea = String.format("%d;%s;%s;%d;%d;%s\n",
                            p.getId(), p.getTitulo(), nombreDirector, p.getAnio(), p.getDuracion(), p.getGenero());
                    writer.write(linea);
                }
                mostrarAlerta("alerta.titulo.info", "Exportado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("alerta.titulo.error", "Fallo al exportar: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        try {
            // USAMOS EL RECURSO ACTUAL PARA NO PERDER EL IDIOMA
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            loader.setResources(this.resources); // <--- IMPORTANTE: Pasamos el idioma actual

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("alerta.titulo.error", "No se pudo volver al menú: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método modificado para aceptar claves de recursos en el título
    private void mostrarAlerta(String claveTitulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        // Intentamos traducir el título si existe la clave, si no, lo dejamos tal cual
        try {
            if (resources != null && resources.containsKey(claveTitulo)) {
                alerta.setTitle(resources.getString(claveTitulo));
            } else {
                alerta.setTitle(claveTitulo); // Si no es clave o falla, usa el texto directo
            }
        } catch (Exception e) {
            alerta.setTitle(claveTitulo);
        }

        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}