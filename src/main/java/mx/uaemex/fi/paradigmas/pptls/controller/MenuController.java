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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import mx.uaemex.fi.paradigmas.pptls.Application;

import java.io.IOException;
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

    // Servicios y Datos
    private JugadoresService servicioJugadores;
    private RecordsService servicioRecords;
    private Jugador jugadorActual;

    @FXML
    public void initialize() {
        // Configuramos qué dato va en cada columna
        if (colJugador != null) {
            colJugador.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getJugador().getLogin()));

            colJuego.setCellValueFactory(cellData ->
                    new SimpleStringProperty("PPTLS"));

            colPuntaje.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getRecord()));

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
        // Si la tabla existe en esta vista, cargamos los datos inmediatamente
        if (tblRecords != null && servicioRecords != null) {
            cargarDatosTabla();
        }
    }

    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
    }

    private void cargarDatosTabla() {
        try {
            var listaRecords = servicioRecords.consultarRecords();
            ObservableList<Record> datos = FXCollections.observableArrayList(listaRecords);
            tblRecords.setItems(datos);
        } catch (Exception e) {
            System.out.println("Error al cargar records: " + e.getMessage());
        }
    }

    @FXML
    public void abrirVentanaRecords() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Records.fxml"));
            Parent root = fxmlLoader.load();

            MenuController recordsController = fxmlLoader.getController();

            //Pasamos los servicios necesarios
            recordsController.setServicioRecords(this.servicioRecords);
            recordsController.setJugador(this.jugadorActual);

            Stage stage = new Stage();
            stage.setTitle("Salón de la Fama - Récords");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("Error al intentar abrir la ventana de Récords.");
            e.printStackTrace();
        }
    }

    //Metodo cerrarVentana
    @FXML
    public void cerrarVentana(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}