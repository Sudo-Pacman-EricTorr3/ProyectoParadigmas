package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MenuController {

    // Elementos de la vista (FXML)
    @FXML private TableView<Record> tblRecords;
    @FXML private TableColumn<Record, String> colJugador;
    @FXML private TableColumn<Record, String> colJuego;
    @FXML private TableColumn<Record, Integer> colPuntaje;
    @FXML private TableColumn<Record, String> colFecha;

    private RecordsService servicioRecords;

    //metodo para recirlo desde  el login ya sea local o el online
    public void setService(RecordsService service) {
        this.servicioRecords = service;
        //cuando lo tenga lo cargamos automaticamente
        cargarDatos();
    }

    private void cargarDatos() {
        if (servicioRecords != null) {
            //Obtenemos la lista del servicio (Polimorfismo)
            ArrayList<Record> lista = servicioRecords.consultarRecords();

            //Convertimos ArrayList a ObservableList que es  lo que usa JavaFX
            ObservableList<Record> datosTabla = FXCollections.observableArrayList(lista);

            //para el Jugador entramos al objeto Jugador y sacamos su Login
            colJugador.setCellValueFactory(fila ->
                    new SimpleStringProperty(fila.getValue().getJugador().getLogin()));

            //para el Juego entramos al objeto Juego y sacamos su Nombre
            colJuego.setCellValueFactory(fila ->
                    new SimpleStringProperty(fila.getValue().getJuego().getNombre()));

            //para el puntaje
            colPuntaje.setCellValueFactory(fila ->
                    new SimpleObjectProperty<>(fila.getValue().getRecord()));

            //para la fecha
            colFecha.setCellValueFactory(fila -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(sdf.format(fila.getValue().getFecha()));
            });

            //metemos los datos a la tabla
            tblRecords.setItems(datosTabla);
        }
    }

    @FXML
    public void cerrarVentana() {
        // Cierra la ventana actual
        Stage stage = (Stage) tblRecords.getScene().getWindow();
        stage.close();
    }
}