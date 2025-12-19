package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.model.data.Juego;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RankingController {

    @FXML private AnchorPane contenedorPrincipal;
    @FXML private ImageView imagenFondo;
    @FXML private Label lblTitulo;

    @FXML private TableView<Record> tablaRanking;

    @FXML private TableColumn<Record, Integer> colLugar;
    @FXML private TableColumn<Record, String> colJugador;
    @FXML private TableColumn<Record, Integer> colPuntaje;
    @FXML private TableColumn<Record, String> colFecha;

    private RecordsService servicioRecords;
    private Stage ventanaMenu;
    private final int ID_JUEGO = 1;

    public void inicializarDatos(RecordsService servicio, Stage menu) {
        this.servicioRecords = servicio;
        this.ventanaMenu = menu;

        configurarFondo();
        configurarTabla();
        cargarRankingGlobal();
    }

    private void configurarFondo() {
        if (imagenFondo != null && contenedorPrincipal != null) {
            imagenFondo.fitWidthProperty().bind(contenedorPrincipal.widthProperty());
            imagenFondo.fitHeightProperty().bind(contenedorPrincipal.heightProperty());
        }
    }

    private void configurarTabla() {
        colLugar.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(tablaRanking.getItems().indexOf(column.getValue()) + 1));

        colLugar.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (this.getTableRow() != null && item != null) {
                    setText(String.valueOf(this.getTableRow().getIndex() + 1));
                } else {
                    setText("");
                }
            }
        });

        colJugador.setCellValueFactory(celda ->
                new SimpleStringProperty(celda.getValue().getJugador().getLogin())
        );

        colPuntaje.setCellValueFactory(celda ->
                new SimpleIntegerProperty(celda.getValue().getRecord()).asObject()
        );

        colFecha.setCellValueFactory(celda -> {
            if(celda.getValue().getFecha() != null) {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                return new SimpleStringProperty(formato.format(celda.getValue().getFecha()));
            }
            return new SimpleStringProperty("-");
        });
    }

    private void cargarRankingGlobal() {
        try {
            Record filtro = new Record();
            Juego juego = new Juego();
            juego.setId(ID_JUEGO);
            filtro.setJuego(juego);

            ArrayList<Record> todosLosRecords = servicioRecords.consultar(filtro);

            if (todosLosRecords != null) {
                ArrayList<Record> mejoresPorJugador = new ArrayList<>();
                ArrayList<Integer> idsProcesados = new ArrayList<>();

                for (Record r : todosLosRecords) {
                    int idJugador = r.getJugador().getId();

                    if (!idsProcesados.contains(idJugador)) {
                        mejoresPorJugador.add(r);
                        idsProcesados.add(idJugador);
                    }
                }

                ObservableList<Record> datos = FXCollections.observableArrayList(mejoresPorJugador);
                tablaRanking.setItems(datos);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar ranking: " + e.getMessage());
        }
    }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        Stage stageActual = (Stage) lblTitulo.getScene().getWindow();
        stageActual.close();
        if (ventanaMenu != null){
            ventanaMenu.show();
        }
    }
}