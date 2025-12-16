package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.util.Date;

public class MenuController {

    @FXML
    private Label lblBienvenida;
    @FXML
    private TableView<Record> tblRecords;
    @FXML
    private TableColumn<Record, String> colJugador;
    @FXML
    private TableColumn<Record, String> colJuego;
    @FXML
    private TableColumn<Record, Number> colPuntaje;
    @FXML
    private TableColumn<Record, Date> colFecha;

    //Servicios y Datos
    private JugadoresService servicioJugadores;
    private RecordsService servicioRecords;
    private Jugador jugadorActual;

    // Este metodo se cargara automáticamente cuando se cargue eel fxml
    @FXML
    public void initialize() {
        // Configuramos qué dato va en cada columna
        if (colJugador != null) {
            //Extraemos el nombre del objeto Jugador dentro del Record
            colJugador.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getJugador().getLogin()));

            //Extraemos el nombre del Juego
            colJuego.setCellValueFactory(cellData ->
                    new SimpleStringProperty("PPTLS"));

            //Extraemos puntaje
            colPuntaje.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getRecord()));

            //Extraemos la fecha
            colFecha.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>(cellData.getValue().getFecha()));
        }
    }

    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;
        if (lblBienvenida != null) {
            lblBienvenida.setText("! Bienvenido, " + jugador.getLogin() + " !");
        }
    }

    public void setServicioRecords(RecordsService servicio) {
        this.servicioRecords = servicio;
        //Si la tabla existe en esta vista, cargamos los datos inmediatamente
        if (tblRecords != null && servicioRecords != null) {
            cargarDatosTabla();
        }
    }

    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
    }

    //Metodo para consultar y llenar la tabla
    private void cargarDatosTabla() {
        try {
            //Llamamos al servicio (Tal como dice el diagrama de secuencia)
            var listaRecords = servicioRecords.consultarRecords();

            //Convertimos a ObservableList para JavaFX
            ObservableList<Record> datos = FXCollections.observableArrayList(listaRecords);

            //Llenamos la tabla
            tblRecords.setItems(datos);

        } catch (Exception e) {
            System.out.println("Error al cargar records: " + e.getMessage());
        }
    }

    @FXML
    public void cerrarVentana(javafx.event.ActionEvent event) {
        javafx.scene.Node source = (javafx.scene.Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }

}