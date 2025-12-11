package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.PeliculaDAO;
import com.sandro.gestionpeliculas.modelo.Pelicula;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PeliculasController implements Initializable {

    // --- ELEMENTOS DE LA PANTALLA ---
    @FXML private TextField txtBuscar;

    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, Integer> colId;
    @FXML private TableColumn<Pelicula, String> colTitulo;
    @FXML private TableColumn<Pelicula, Integer> colDirector;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtPresupuesto;
    @FXML private DatePicker dateLanzamiento;
    @FXML private ComboBox<String> comboGenero;
    @FXML private CheckBox checkAdultos;
    @FXML private ImageView imgCartel;

    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnVolver;
    @FXML private Button btnFoto;

    // --- VARIABLES GLOBALES ---
    private String rutaFotoSeleccionada = "";
    private PeliculaDAO peliculaDAO = new PeliculaDAO();
    private Pelicula peliculaSeleccionada = null; // Para saber cuál estamos editando

    // Lista maestra para el buscador (guarda todas las pelis para no perderlas al filtrar)
    private ObservableList<Pelicula> listaMaster = FXCollections.observableArrayList();

    // --- MÉTODO INITIALIZE (Arranca al abrir la ventana) ---
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("idDirector"));

        // 2. Rellenar ComboBox con géneros
        comboGenero.setItems(FXCollections.observableArrayList(
                "Acción", "Comedia", "Drama", "Terror", "Ciencia Ficción", "Animación", "Thriller"
        ));

        // 3. Cargar datos de la BBDD
        cargarPeliculas();

        // 4. LISTENER TABLA: Detectar clic en una fila
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                peliculaSeleccionada = newSelection;
                mostrarDetallesPelicula(peliculaSeleccionada);
            }
        });

        // 5. LISTENER BUSCADOR: Filtrar mientras escribes
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarPeliculas(newValue);
        });
    }

    // --- MÉTODOS AUXILIARES ---

    private void cargarPeliculas() {
        tablaPeliculas.getItems().clear();
        List<Pelicula> lista = peliculaDAO.obtenerTodas();

        // Guardamos la lista completa en la variable "Master" y en la tabla
        listaMaster = FXCollections.observableArrayList(lista);
        tablaPeliculas.setItems(listaMaster);
    }

    private void filtrarPeliculas(String texto) {
        // Si borras el texto, mostramos todas de nuevo
        if (texto == null || texto.isEmpty()) {
            tablaPeliculas.setItems(listaMaster);
            return;
        }

        // Creamos una lista temporal solo con las coincidencias
        ObservableList<Pelicula> filtro = FXCollections.observableArrayList();
        for (Pelicula p : listaMaster) {
            if (p.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                filtro.add(p);
            }
        }
        tablaPeliculas.setItems(filtro);
    }

    private void mostrarDetallesPelicula(Pelicula p) {
        // Rellenar campos
        txtTitulo.setText(p.getTitulo());
        txtDuracion.setText(String.valueOf(p.getDuracion()));
        txtPresupuesto.setText(String.valueOf(p.getPresupuesto()));
        dateLanzamiento.setValue(p.getFechaLanzamiento());
        checkAdultos.setSelected(p.isEsMas18());

        // Mostrar imagen (controlando errores)
        if (p.getCartelUrl() != null && !p.getCartelUrl().isEmpty()) {
            rutaFotoSeleccionada = p.getCartelUrl();
            try {
                imgCartel.setImage(new Image(rutaFotoSeleccionada));
            } catch (Exception e) {
                imgCartel.setImage(null);
            }
        } else {
            imgCartel.setImage(null);
            rutaFotoSeleccionada = "";
        }

        // CAMBIO VISUAL: Botón en modo "ACTUALIZAR"
        btnGuardar.setText("ACTUALIZAR");
        btnGuardar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    // --- MÉTODOS DE BOTONES ---

    @FXML
    void seleccionarFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Cartel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            rutaFotoSeleccionada = file.toURI().toString();
            imgCartel.setImage(new Image(rutaFotoSeleccionada));
        }
    }

    @FXML
    void guardarPelicula(ActionEvent event) {
        String titulo = txtTitulo.getText();
        if (titulo == null || titulo.isEmpty()) {
            mostrarAlerta("Error", "El título es obligatorio");
            return;
        }

        try {
            int duracion = Integer.parseInt(txtDuracion.getText());
            double presupuesto = Double.parseDouble(txtPresupuesto.getText());
            boolean esMas18 = checkAdultos.isSelected();
            LocalDate fecha = dateLanzamiento.getValue();
            if (fecha == null) {
                mostrarAlerta("Error", "La fecha es obligatoria");
                return;
            }

            // --- DECISIÓN: ¿CREAR O ACTUALIZAR? ---
            if (peliculaSeleccionada == null) {
                // MODO CREAR (INSERT)
                Pelicula nuevaPeli = new Pelicula(0, titulo, fecha, duracion, presupuesto, esMas18, rutaFotoSeleccionada, 1, 1);
                if (peliculaDAO.insertar(nuevaPeli)) {
                    System.out.println("✅ Película creada.");
                    limpiarFormulario(null);
                    cargarPeliculas();
                }
            } else {
                // MODO EDITAR (UPDATE)
                Pelicula peliEditada = new Pelicula(
                        peliculaSeleccionada.getId(), // Mantenemos ID original
                        titulo, fecha, duracion, presupuesto, esMas18, rutaFotoSeleccionada, 1, 1
                );
                if (peliculaDAO.actualizar(peliEditada)) {
                    System.out.println("🔄 Película actualizada.");
                    limpiarFormulario(null);
                    cargarPeliculas();
                }
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Duración y Presupuesto deben ser números.");
        }
    }

    @FXML
    void eliminarPelicula(ActionEvent event) {
        if (peliculaSeleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una película para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Película");
        confirm.setHeaderText("¿Estás seguro de eliminar '" + peliculaSeleccionada.getTitulo() + "'?");
        confirm.setContentText("Se borrarán también sus actores y valoraciones.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (peliculaDAO.eliminar(peliculaSeleccionada.getId())) {
                System.out.println("🗑️ Eliminada correctamente.");
                limpiarFormulario(null);
                cargarPeliculas();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la película.");
            }
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtTitulo.setText("");
        txtDuracion.setText("");
        txtPresupuesto.setText("");
        dateLanzamiento.setValue(null);
        comboGenero.setValue(null);
        checkAdultos.setSelected(false);
        imgCartel.setImage(null);
        rutaFotoSeleccionada = "";

        // Resetear selección de tabla y variable
        tablaPeliculas.getSelectionModel().clearSelection();
        peliculaSeleccionada = null;

        // CAMBIO VISUAL: Botón vuelve a modo "GUARDAR"
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

    // Pequeño método para ahorrar código en las alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}