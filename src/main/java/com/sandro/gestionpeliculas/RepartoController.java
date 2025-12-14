package com.sandro.gestionpeliculas;

import com.sandro.gestionpeliculas.dao.ActorDAO;
import com.sandro.gestionpeliculas.dao.RepartoDAO;
import com.sandro.gestionpeliculas.modelo.Actor;
import com.sandro.gestionpeliculas.modelo.Actuacion;
import com.sandro.gestionpeliculas.modelo.Pelicula;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class RepartoController implements Initializable {

    @FXML private Label lblTituloPelicula;
    @FXML private TableView<Actuacion> tablaReparto;
    @FXML private TableColumn<Actuacion, String> colActor;
    @FXML private TableColumn<Actuacion, String> colPersonaje;
    @FXML private TableColumn<Actuacion, String> colTipo;

    @FXML private ComboBox<Actor> comboActores;
    @FXML private TextField txtPersonaje;
    @FXML private TextField txtTipoPapel;

    private RepartoDAO repartoDAO = new RepartoDAO();
    private ActorDAO actorDAO = new ActorDAO();

    // La película sobre la que estamos trabajando
    private Pelicula peliculaActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar columnas
        // Nota: 'nombreActor' es un método getNombreActor() que creamos en Actuacion
        colActor.setCellValueFactory(new PropertyValueFactory<>("nombreActor"));
        colPersonaje.setCellValueFactory(new PropertyValueFactory<>("personaje"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoPapel"));

        cargarComboActores();
    }

    // Este método lo llamaremos desde la pantalla de Películas para pasarle los datos
    public void initData(Pelicula p) {
        this.peliculaActual = p;
        lblTituloPelicula.setText("Reparto de: " + p.getTitulo());
        cargarTabla();
    }

    private void cargarTabla() {
        if (peliculaActual != null) {
            tablaReparto.setItems(FXCollections.observableArrayList(
                    repartoDAO.obtenerReparto(peliculaActual.getId())
            ));
        }
    }

    private void cargarComboActores() {
        comboActores.setItems(FXCollections.observableArrayList(actorDAO.obtenerTodos()));

        // Configurar para que se vea el nombre en el ComboBox
        comboActores.setConverter(new StringConverter<Actor>() {
            @Override
            public String toString(Actor actor) {
                return (actor != null) ? actor.getNombre() : "";
            }
            @Override
            public Actor fromString(String string) { return null; }
        });
    }

    @FXML
    void agregarActor(ActionEvent event) {
        Actor actor = comboActores.getValue();
        String personaje = txtPersonaje.getText();
        String rol = txtTipoPapel.getText();

        if (actor == null || personaje.isEmpty()) {
            mostrarAlerta("Error", "Debes seleccionar un actor y poner un nombre de personaje.");
            return;
        }

        if (repartoDAO.agregarActor(peliculaActual.getId(), actor.getId(), personaje, rol)) {
            cargarTabla();
            txtPersonaje.clear();
            txtTipoPapel.clear();
            comboActores.getSelectionModel().clearSelection();
        } else {
            mostrarAlerta("Error", "No se pudo añadir (¿quizás ya está en el reparto?).");
        }
    }

    @FXML
    void eliminarActor(ActionEvent event) {
        Actuacion seleccionada = tablaReparto.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una fila para quitarla.");
            return;
        }

        if (repartoDAO.eliminarActor(seleccionada.getIdPelicula(), seleccionada.getActor().getId())) {
            cargarTabla();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar.");
        }
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}