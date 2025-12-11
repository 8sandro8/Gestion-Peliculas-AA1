package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.DirectorDAO;
import com.sandro.gestionpeliculas.dao.PeliculaDAO;
import com.sandro.gestionpeliculas.modelo.Director;
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

    // --- NUEVO: El desplegable de Directores ---
    @FXML private ComboBox<String> comboDirector;

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
    private DirectorDAO directorDAO = new DirectorDAO();
    private Pelicula peliculaSeleccionada = null;
    private ObservableList<Pelicula> listaMaster = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("idDirector"));

        // 2. Rellenar Géneros
        comboGenero.setItems(FXCollections.observableArrayList(
                "Acción", "Comedia", "Drama", "Terror", "Ciencia Ficción", "Animación", "Thriller"
        ));

        // 3. Rellenar Directores (¡IMPORTANTE!)
        cargarComboDirectores();

        // 4. Cargar Pelis
        cargarPeliculas();

        // Listeners
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                peliculaSeleccionada = newSelection;
                mostrarDetallesPelicula(peliculaSeleccionada);
            }
        });

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarPeliculas(newValue);
        });
    }

    // --- MÉTODOS AUXILIARES ---

    private void cargarComboDirectores() {
        System.out.println("🔍 Intentando cargar directores en el combo...");

        List<Director> directores = directorDAO.obtenerTodos();
        System.out.println("📊 Directores encontrados: " + directores.size());

        ObservableList<String> nombres = FXCollections.observableArrayList();

        for (Director d : directores) {
            String etiqueta = d.getId() + " - " + d.getNombre();
            System.out.println("   ➕ Añadido: " + etiqueta);
            nombres.add(etiqueta);
        }
        comboDirector.setItems(nombres);
    }

    private int obtenerIdDirectorSeleccionado() {
        String texto = comboDirector.getValue();
        if (texto != null && texto.contains("-")) {
            String[] partes = texto.split(" - ");
            return Integer.parseInt(partes[0]);
        }
        return 1; // ID por defecto si falla algo
    }

    private void seleccionarDirectorEnCombo(int idDirector) {
        for (String item : comboDirector.getItems()) {
            if (item.startsWith(idDirector + " - ")) {
                comboDirector.setValue(item);
                return;
            }
        }
    }

    private void cargarPeliculas() {
        tablaPeliculas.getItems().clear();
        List<Pelicula> lista = peliculaDAO.obtenerTodas();
        listaMaster = FXCollections.observableArrayList(lista);
        tablaPeliculas.setItems(listaMaster);
    }

    private void filtrarPeliculas(String texto) {
        if (texto == null || texto.isEmpty()) {
            tablaPeliculas.setItems(listaMaster);
            return;
        }
        ObservableList<Pelicula> filtro = FXCollections.observableArrayList();
        for (Pelicula p : listaMaster) {
            if (p.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                filtro.add(p);
            }
        }
        tablaPeliculas.setItems(filtro);
    }

    private void mostrarDetallesPelicula(Pelicula p) {
        txtTitulo.setText(p.getTitulo());
        txtDuracion.setText(String.valueOf(p.getDuracion()));
        txtPresupuesto.setText(String.valueOf(p.getPresupuesto()));
        dateLanzamiento.setValue(p.getFechaLanzamiento());
        checkAdultos.setSelected(p.isEsMas18());

        seleccionarDirectorEnCombo(p.getIdDirector());

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

        btnGuardar.setText("ACTUALIZAR");
        btnGuardar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    // --- ACCIONES DE BOTONES ---

    @FXML
    void seleccionarFoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
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
            mostrarAlerta("Error", "Título obligatorio");
            return;
        }

        try {
            int duracion = Integer.parseInt(txtDuracion.getText());
            double presupuesto = Double.parseDouble(txtPresupuesto.getText());
            boolean esMas18 = checkAdultos.isSelected();
            LocalDate fecha = dateLanzamiento.getValue();
            if (fecha == null) {
                mostrarAlerta("Error", "Fecha obligatoria");
                return;
            }

            int idDirector = obtenerIdDirectorSeleccionado();

            if (peliculaSeleccionada == null) {
                // CREAR
                Pelicula nueva = new Pelicula(0, titulo, fecha, duracion, presupuesto, esMas18, rutaFotoSeleccionada, idDirector, 1);
                if (peliculaDAO.insertar(nueva)) {
                    System.out.println("✅ Película creada.");
                    limpiarFormulario(null);
                    cargarPeliculas();
                }
            } else {
                // ACTUALIZAR
                Pelicula editada = new Pelicula(
                        peliculaSeleccionada.getId(),
                        titulo, fecha, duracion, presupuesto, esMas18, rutaFotoSeleccionada,
                        idDirector,
                        1
                );
                if (peliculaDAO.actualizar(editada)) {
                    System.out.println("🔄 Película actualizada.");
                    limpiarFormulario(null);
                    cargarPeliculas();
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Revisa los números.");
        }
    }

    @FXML
    void eliminarPelicula(ActionEvent event) {
        if (peliculaSeleccionada == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar");
        confirm.setHeaderText("¿Borrar " + peliculaSeleccionada.getTitulo() + "?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (peliculaDAO.eliminar(peliculaSeleccionada.getId())) {
                limpiarFormulario(null);
                cargarPeliculas();
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
        comboDirector.setValue(null);
        checkAdultos.setSelected(false);
        imgCartel.setImage(null);
        rutaFotoSeleccionada = "";

        tablaPeliculas.getSelectionModel().clearSelection();
        peliculaSeleccionada = null;

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
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}