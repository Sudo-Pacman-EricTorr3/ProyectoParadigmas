package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.Application;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.io.IOException;

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

    private Jugador jugadorActual;
    private JugadoresService servicioJugadores;
    private RecordsService servicioRecords;

    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;
        actualizarInformacionUsuario();
    }

    public void setServicioJugadores(JugadoresService servicio) {
        this.servicioJugadores = servicio;
    }

    public void setServicioRecords(RecordsService servicio) {
        this.servicioRecords = servicio;
    }

    @FXML
    public void initialize() {
        configurarEfectosBotones();
    }

    private void configurarEfectosBotones() {
        Button[] botones = {btnIniciarJuego, btnRecords, btnRanking, btnSalirSesion, btnSalirJuego};

        for (Button btn : botones) {
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #4a5568; -fx-text-fill: white; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; -fx-cursor: default;"));
        }
    }

    private void actualizarInformacionUsuario() {
        if (jugadorActual != null) {
            lblUsuario.setText("Usuario: " + jugadorActual.getLogin());

            if (servicioJugadores != null) {
                String nombreServicio = servicioJugadores.getClass().getSimpleName();
                if (nombreServicio.contains("Online") || nombreServicio.contains("Psql")) {
                    lblModo.setText("Modo: Multijugador (Online)");
                } else {
                    lblModo.setText("Modo: Local");
                }
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
            stageNuevo.setTitle("PPTLS - Piedra, Papel, Tijera, Lagarto, Spock");
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

            Stage stage = new Stage();
            stage.setTitle("Mis Records - PPTLS");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarError("Error al abrir records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void abrirRanking() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ranking Global");
        alert.setHeaderText("Top 10 Jugadores PPTLS");
        alert.setContentText("Función en desarrollo...\n\n" +
                "1. Usuario1 - 1500 pts\n" +
                "2. Usuario2 - 1450 pts\n" +
                "3. Usuario3 - 1400 pts\n" +
                "...\n\n" +
                "Tu posición: #5 - 1350 pts");
        alert.showAndWait();
    }

    @FXML
    protected void salirSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText("¿Cerrar sesión?");
        alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                volverAlLogin();
            }
        });
    }

    @FXML
    protected void salirJuego() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir del juego");
        alert.setHeaderText("¿Salir de PPTLS?");
        alert.setContentText("Se cerrará la aplicación completamente.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    private void volverAlLogin() {
        try {
            Stage stage = (Stage) btnIniciarJuego.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Iniciar Sesión - PPTLS");
            stage.setScene(scene);
            LoginController loginController = fxmlLoader.getController();
            loginController.setServicioJugadoresLocal(servicioJugadores);
            loginController.setServicioJugadoresOnline(servicioJugadores);

        } catch (IOException e) {
            mostrarError("Error al volver al login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ocurrió un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}