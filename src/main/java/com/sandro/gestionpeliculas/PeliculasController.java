package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.ActorDAO;
import com.sandro.gestionpeliculas.dao.DirectorDAO;
import com.sandro.gestionpeliculas.dao.PeliculaDAO;
import com.sandro.gestionpeliculas.dao.RepartoDAO;
import com.sandro.gestionpeliculas.modelo.Actor;
import com.sandro.gestionpeliculas.modelo.Director;
import com.sandro.gestionpeliculas.modelo.Pelicula;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
    @FXML private ComboBox<String> comboDirector;

    @FXML private CheckBox checkAdultos;
    @FXML private ImageView imgCartel;

    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnVolver;
    @FXML private Button btnFoto;
    @FXML private Button btnReparto;

    // --- VARIABLES GLOBALES ---
    private String rutaFotoSeleccionada = "";

    // DAOs
    private PeliculaDAO peliculaDAO = new PeliculaDAO();
    private DirectorDAO directorDAO = new DirectorDAO();
    private RepartoDAO repartoDAO = new RepartoDAO();
    private ActorDAO actorDAO = new ActorDAO();

    private Pelicula peliculaSeleccionada = null;
    private ObservableList<Pelicula> listaMaster = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("idDirector"));

        // 2. Rellenar Combos
        comboGenero.setItems(FXCollections.observableArrayList(
                "Acción", "Comedia", "Drama", "Terror", "Ciencia Ficción", "Animación", "Thriller"
        ));
        cargarComboDirectores();

        // 3. Cargar Pelis
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
        List<Director> directores = directorDAO.obtenerTodos();
        ObservableList<String> nombres = FXCollections.observableArrayList();
        for (Director d : directores) {
            nombres.add(d.getId() + " - " + d.getNombre());
        }
        comboDirector.setItems(nombres);
    }

    private int obtenerIdDirectorSeleccionado() {
        String texto = comboDirector.getValue();
        if (texto != null && texto.contains("-")) {
            String[] partes = texto.split(" - ");
            return Integer.parseInt(partes[0]);
        }
        return 1;
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
        comboGenero.setValue(null);

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
    void abrirGestionReparto(ActionEvent event) {
        if (peliculaSeleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una película primero.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reparto: " + peliculaSeleccionada.getTitulo());
        dialog.setHeaderText("Añadir actores a la película");

        try {
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
        } catch (Exception e) { System.out.println("No se pudo cargar CSS al diálogo"); }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("form-card");

        Label lblActuales = new Label("Actores en el reparto:");
        ListView<Actor> listActoresEnPeli = new ListView<>();
        listActoresEnPeli.setPrefHeight(150);

        List<Actor> actoresEnPeli = repartoDAO.obtenerActoresPorPelicula(peliculaSeleccionada.getId());
        listActoresEnPeli.getItems().addAll(actoresEnPeli);

        Button btnQuitar = new Button("Quitar Actor Seleccionado");
        btnQuitar.getStyleClass().add("button-peligro");
        btnQuitar.setOnAction(e -> {
            Actor a = listActoresEnPeli.getSelectionModel().getSelectedItem();
            if (a != null) {
                repartoDAO.eliminarActorDePelicula(a.getId(), peliculaSeleccionada.getId());
                listActoresEnPeli.getItems().remove(a);
            }
        });

        Separator sep = new Separator();

        Label lblAnadir = new Label("Añadir actor:");
        ComboBox<Actor> comboTodosActores = new ComboBox<>();
        comboTodosActores.getItems().addAll(actorDAO.obtenerTodos());
        comboTodosActores.setPromptText("Selecciona un actor...");
        comboTodosActores.setMaxWidth(Double.MAX_VALUE);

        Button btnAnadir = new Button("Añadir al Reparto");
        btnAnadir.setOnAction(e -> {
            Actor actorElegido = comboTodosActores.getValue();
            if (actorElegido != null) {
                boolean exito = repartoDAO.anadirActorAPelicula(actorElegido.getId(), peliculaSeleccionada.getId());
                if (exito) {
                    listActoresEnPeli.getItems().add(actorElegido);
                } else {
                    mostrarAlerta("Info", "Este actor ya está en el reparto.");
                }
            }
        });

        layout.getChildren().addAll(lblActuales, listActoresEnPeli, btnQuitar, sep, lblAnadir, comboTodosActores, btnAnadir);
        dialog.getDialogPane().setContent(layout);
        dialog.showAndWait();
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

    // --- AQUÍ ESTÁ EL ARREGLO ---
    @FXML
    void volverMenu(ActionEvent event) {
        try {
            // 1. CARGAMOS EL IDIOMA (ResourceBundle)
            ResourceBundle bundle = ResourceBundle.getBundle("com.sandro.gestionpeliculas.mensajes");

            // 2. Cargamos el FXML pasándole el idioma
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"));
            loader.setResources(bundle); // <--- ¡ESTA LÍNEA ES LA CLAVE!

            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}