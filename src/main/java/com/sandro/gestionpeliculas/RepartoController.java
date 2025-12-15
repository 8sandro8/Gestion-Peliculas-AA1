package com.sandro.gestionpeliculas.modelo;

import com.sandro.gestionpeliculas.dao.RepartoDAO;
import com.sandro.gestionpeliculas.dao.ActorDAO;
import com.sandro.gestionpeliculas.modelo.Pelicula;
import com.sandro.gestionpeliculas.modelo.Actor;
import com.sandro.gestionpeliculas.modelo.Actuacion; // <--- IMPORTANTE: Importamos la clase Actuacion

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RepartoController implements Initializable {

    // --- ELEMENTOS FXML ---
    @FXML private Label lblTituloPelicula;

    @FXML private TableView<FilaReparto> tablaReparto;
    @FXML private TableColumn<FilaReparto, String> colActor;
    @FXML private TableColumn<FilaReparto, String> colPersonaje;
    @FXML private TableColumn<FilaReparto, String> colTipo;

    @FXML private ComboBox<Actor> comboActores;
    @FXML private TextField txtPersonaje;
    @FXML private TextField txtTipoPapel;

    // --- VARIABLES ---
    private Pelicula peliculaActual;
    private RepartoDAO repartoDAO;
    private ActorDAO actorDAO;

    private ObservableList<FilaReparto> datosReparto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        repartoDAO = new RepartoDAO();
        actorDAO = new ActorDAO();
        datosReparto = FXCollections.observableArrayList();

        configurarTabla();
        cargarActoresCombo();
    }

    private void configurarTabla() {
        colActor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActor()));
        colPersonaje.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPersonaje()));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));

        tablaReparto.setItems(datosReparto);
    }

    public void initData(Pelicula pelicula) {
        this.peliculaActual = pelicula;
        lblTituloPelicula.setText("Reparto de: " + pelicula.getTitulo());
        cargarReparto();
    }

    private void cargarActoresCombo() {
        try {
            comboActores.setItems(FXCollections.observableArrayList(actorDAO.listarTodos()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- AQUÍ ESTÁ LA CORRECCIÓN DEL ERROR ---
    private void cargarReparto() {
        if (peliculaActual != null) {
            datosReparto.clear();

            // 1. Recibimos la lista de OBJETOS Actuacion (no Strings)
            List<Actuacion> listaActuaciones = repartoDAO.obtenerReparto(peliculaActual.getId());

            // 2. Recorremos los objetos y sacamos los datos
            for (Actuacion actuacion : listaActuaciones) {

                // NOTA: Asumo que tu clase Actuacion tiene .getActor() que devuelve un Actor
                // y .getPersonaje() que devuelve un String.

                String nombreActor = "Desconocido";
                if (actuacion.getActor() != null) {
                    nombreActor = actuacion.getActor().getNombre();
                }

                String personaje = actuacion.getPersonaje();

                // Si tu clase Actuacion tiene getPapel() o getRol(), úsalo aquí.
                // Si no, lo dejamos como "N/A"
                String rol = "N/A";

                datosReparto.add(new FilaReparto(nombreActor, personaje, rol));
            }
        }
    }

    @FXML
    public void agregarActorReparto() {
        Actor actorSeleccionado = comboActores.getValue();
        String personaje = txtPersonaje.getText();

        if (actorSeleccionado == null || personaje.isEmpty()) {
            mostrarAlerta("Faltan datos", "Selecciona un actor y escribe el personaje.");
            return;
        }

        boolean exito = repartoDAO.agregarReparto(peliculaActual.getId(), actorSeleccionado.getId(), personaje);

        if (exito) {
            cargarReparto();
            txtPersonaje.clear();
            txtTipoPapel.clear();
            comboActores.getSelectionModel().clearSelection();
        } else {
            mostrarAlerta("Error", "No se pudo añadir al reparto (quizás ya existe).");
        }
    }

    @FXML
    public void eliminarDeReparto() {
        FilaReparto seleccion = tablaReparto.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarAlerta("Aviso", "Selecciona una fila para eliminar.");
            return;
        }
        mostrarAlerta("Información", "Función de eliminar pendiente de implementar en DAO.");
    }

    @FXML
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --- CLASE INTERNA PARA LA TABLA ---
    public static class FilaReparto {
        private final String actor;
        private final String personaje;
        private final String tipo;

        public FilaReparto(String actor, String personaje, String tipo) {
            this.actor = actor;
            this.personaje = personaje;
            this.tipo = tipo;
        }

        public String getActor() { return actor; }
        public String getPersonaje() { return personaje; }
        public String getTipo() { return tipo; }
    }
}