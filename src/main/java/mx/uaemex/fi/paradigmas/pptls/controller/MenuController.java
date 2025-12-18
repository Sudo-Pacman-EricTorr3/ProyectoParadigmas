package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.Application;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.io.IOException;
import java.util.Date;

public class MenuController {


    @FXML
    private Button btnIniciarJuego;
    @FXML
    private Button btnRecords;
    @FXML
    private Button btnRanking;
    @FXML
    private Button btnSalirSesion;
    @FXML
    private Button btnSalirJuego;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblModo;


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


    private Jugador jugadorActual;
    private JugadoresService servicioJugadores;
    private RecordsService servicioRecords;


    @FXML
    public void initialize() {

        if (btnIniciarJuego != null) {
            configurarEfectosBotones();
        }


        if (colJugador != null) {
            configurarColumnasTabla();
        }
    }


    private void configurarColumnasTabla() {
        colJugador.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJugador().getLogin()));
        colJuego.setCellValueFactory(cellData -> new SimpleStringProperty("PPTLS"));
        colPuntaje.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRecord()));
        colFecha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFecha()));
    }

    private void cargarDatosTabla() {
        try {
            if (servicioRecords != null) {
                var listaRecords = servicioRecords.consultarRecords();
                ObservableList<Record> datos = FXCollections.observableArrayList(listaRecords);
                tblRecords.setItems(datos);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar datos en la tabla: " + e.getMessage());
        }
    }


    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;
        actualizarInformacionUsuario();
    }

    public void setServicioJugadores(JugadoresService servicio) {
        this.servicioJugadores = servicio;
        actualizarInformacionUsuario();
    }

    public void setServicioRecords(RecordsService servicio) {
        this.servicioRecords = servicio;
        if (tblRecords != null && servicioRecords != null) {
            cargarDatosTabla();
        }
    }

    private void actualizarInformacionUsuario() {

        if (lblUsuario != null && jugadorActual != null) {
            lblUsuario.setText("Usuario: " + jugadorActual.getLogin());
        }
        if (lblModo != null && servicioJugadores != null) {
            String nombreServicio = servicioJugadores.getClass().getSimpleName();
            if (nombreServicio.contains("Online") || nombreServicio.contains("Psql")) {
                lblModo.setText("Modo: Multijugador (Online)");
            } else {
                lblModo.setText("Modo: Local");
            }
        }
    }


    @FXML
    protected void iniciarJuego() {
        try {
            Stage stageActual = (Stage) btnIniciarJuego.getScene().getWindow();
            stageActual.close();

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Game-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            GameController gameController = fxmlLoader.getController();
            gameController.setJugador(jugadorActual);
            gameController.setServicioRecord(servicioRecords);

            Stage stageNuevo = new Stage();
            stageNuevo.setTitle("PPTLS - Juego");
            stageNuevo.setScene(scene);
            stageNuevo.setResizable(false);
            stageNuevo.show();

        } catch (IOException e) {
            mostrarError("Error al cargar el juego: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void abrirRecords() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Records.fxml"));
            Scene scene = new Scene(fxmlLoader.load());


            MenuController recordsController = fxmlLoader.getController();
            recordsController.setServicioRecords(this.servicioRecords);
            recordsController.setJugador(this.jugadorActual);

            Stage stage = new Stage();
            stage.setTitle("Mis Récords - PPTLS");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarError("Error al abrir records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarVentana(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    @FXML
    protected void salirSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText("¿Cerrar sesión?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) volverAlLogin();
        });
    }

    @FXML
    protected void salirJuego() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Salir de la aplicación?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) System.exit(0);
        });
    }

    private void volverAlLogin() {
        try {
            Stage stage = (Stage) btnIniciarJuego.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Iniciar Sesión");
            stage.setScene(scene);


            LoginController loginController = fxmlLoader.getController();
            loginController.setServicioJugadoresLocal(servicioJugadores);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configurarEfectosBotones() {
        Button[] botones = {btnIniciarJuego, btnRecords, btnRanking, btnSalirSesion, btnSalirJuego};
        for (Button btn : botones) {
            if (btn != null) {
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #4a5568; -fx-text-fill: white; -fx-cursor: hand;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-cursor: default;"));
            }
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    protected void abrirRanking() {
        try {
            //Pedimos la lista que ya viene ordenada por puntaje DESC gracias al DAO
            var listaRecords = servicioRecords.consultarRecords();

            StringBuilder textoTop10 = new StringBuilder();
            int lugar = 1;

            //Recorremos la lista y tomamos solo los primeros 10
            for (Record r : listaRecords) {
                if (lugar > 10) break; //si pasamos del 10, nos detenemos

                String nombre = "Desconocido";
                if (r.getJugador() != null && r.getJugador().getLogin() != null) {
                    nombre = r.getJugador().getLogin();
                }

                textoTop10.append("#").append(lugar).append(" ")
                        .append(nombre)
                        .append(" - ")
                        .append(r.getRecord()).append(" pts\n");

                lugar++;
            }

            if (textoTop10.length() == 0) {
                textoTop10.append("Aún no hay récords registrados.");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ranking Global");
            alert.setHeaderText("🏆 Top 10 Mejores Jugadores 🏆");
            alert.setContentText(textoTop10.toString());
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println("Error al cargar ranking: " + e.getMessage());
        }
    }
}