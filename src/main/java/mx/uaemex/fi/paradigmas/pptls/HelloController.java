package mx.uaemex.fi.paradigmas.pptls;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    
    @FXML private TableView<Record> tablaRecords;
    @FXML private TableColumn<Record, String> colJugador;
    @FXML private TableColumn<Record, String> colJuego;
    @FXML private TableColumn<Record, String> colFecha;
    @FXML private TableColumn<Record, Integer> colPuntaje;

    //lista, si agregas algo aquí, la tabla se actualiza sola
    private ObservableList<Record> listaDeRecords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //inicializar la lista
        listaDeRecords = FXCollections.observableArrayList();

        // Busca automáticamente el método getRecord() en tu clase Record
        colPuntaje.setCellValueFactory(new PropertyValueFactory<>("record"));

        //Objetos anidados (Jugador)
        //extraemos el nombre del objeto Jugador.
        colJugador.setCellValueFactory(cellData -> {
            if (cellData.getValue().getJugador() != null) {
                return new SimpleStringProperty(cellData.getValue().getJugador().toString());
            } else {
                return new SimpleStringProperty("Sin Jugador");
            }
        });

        //Objetos anidados (Juego)
        colJuego.setCellValueFactory(cellData -> {
            if (cellData.getValue().getJuego() != null) {
                return new SimpleStringProperty(cellData.getValue().getJuego().toString());
            } else {
                return new SimpleStringProperty("Sin Juego");
            }
        });

        //Fecha
        colFecha.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFecha() != null) {
                return new SimpleStringProperty(cellData.getValue().getFecha().toString());
            } else {
                return new SimpleStringProperty("");
            }
        });

        //ASIGNAR LA LISTA A LA TABLA
        tablaRecords.setItems(listaDeRecords);

        //esto es Para ver que funcione)
        cargarDatosPrueba();
    }

    private void cargarDatosPrueba() {
        // Creamos un record falso para probar
        Record r1 = new Record();
        r1.setRecord(5000);
        r1.setFecha(new java.util.Date());


        //r1.setJugador(new Jugador()("Juan"));

        listaDeRecords.add(r1);
    }
}