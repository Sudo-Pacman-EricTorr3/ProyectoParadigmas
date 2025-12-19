package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecordsController {

    @FXML private AnchorPane contenedorPrincipal;
    @FXML private ImageView imagenFondo;
    @FXML private Label lblTitulo;

    @FXML private TableView<Record> tablaRecords;
    @FXML private TableColumn<Record, String> colJuego;
    @FXML private TableColumn<Record, Integer> colPuntaje;
    @FXML private TableColumn<Record, String> colFecha;

    private Jugador jugadorActual;
    private RecordsService servicioRecords;
    private Stage ventanaMenu;

    public void inicializarDatos(Jugador jugador, RecordsService servicio, Stage menu) {
        this.jugadorActual = jugador;
        this.servicioRecords = servicio;
        this.ventanaMenu = menu;

        configurarFondo();
        lblTitulo.setText("Historial de: " + jugador.getLogin());

        configurarColumnas();

        cargarRegistros();
    }

    private void configurarFondo() {
        if (imagenFondo != null && contenedorPrincipal != null) {
            imagenFondo.fitWidthProperty().bind(contenedorPrincipal.widthProperty());
            imagenFondo.fitHeightProperty().bind(contenedorPrincipal.heightProperty());
        }
    }

    private void configurarColumnas() {
        colJuego.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getJuego().getNombre())
        );

        colPuntaje.setCellValueFactory(celda ->
                new SimpleIntegerProperty(celda.getValue().getRecord()).asObject()
        );

        colFecha.setCellValueFactory(celda -> {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaTexto = formato.format(celda.getValue().getFecha());
            return new SimpleStringProperty(fechaTexto);
        });
    }

    private void cargarRegistros() {
        try {
            Record filtro = new Record();
            filtro.setJugador(jugadorActual);

            ArrayList<Record> lista = servicioRecords.consultar(filtro);

            if (lista != null) {
                ObservableList<Record> datosTabla = FXCollections.observableArrayList(lista);
                tablaRecords.setItems(datosTabla);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la tabla: " + e.getMessage());
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        // Cerramos esta ventana
        Stage stageActual = (Stage) lblTitulo.getScene().getWindow();
        stageActual.close();

        if (ventanaMenu != null) {
            ventanaMenu.show();
        }
    }
}