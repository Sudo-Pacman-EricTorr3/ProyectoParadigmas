package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.Application;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.io.IOException;
import java.util.Optional;

public class MenuController {

    @FXML private Label lblUsuario;
    @FXML private Label lblModo;
    @FXML private Button btnIniciarJuego;
    @FXML private Button btnRecords;
    @FXML private Button btnRanking;
    @FXML private Button btnCerrarSesion;

    private Jugador jugadorActual;

    private JugadoresService servicioJugadoresLocal;
    private JugadoresService servicioJugadoresOnline;
    private JugadoresService servicioJugadores;
    private RecordsService servicioRecordsLocal;
    private RecordsService servicioRecordsOnline;
    private RecordsService servicioRecords;

    public void setServicioJugadoresLocal(JugadoresService s) { this.servicioJugadoresLocal = s; }
    public void setServicioJugadoresOnline(JugadoresService s) { this.servicioJugadoresOnline = s; }

    public void setServicioRecordsLocal(RecordsService s) { this.servicioRecordsLocal = s; }
    public void setServicioRecordsOnline(RecordsService s) { this.servicioRecordsOnline = s; }

    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;
        if (lblUsuario != null && jugador != null) {
            lblUsuario.setText("Usuario: " + jugador.getLogin());
        }
    }

    public void setServicioRecords(RecordsService s) { this.servicioRecords = s; }

    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
        if (lblModo != null) {
            if (s == this.servicioJugadoresOnline) {
                lblModo.setText("Modo: Multijugador (Online)");
            } else {
                lblModo.setText("Modo: Local");
            }
        }
    }

    @FXML
    protected void iniciarJuego() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Game-view.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();

            Stage stageMenu = (Stage) btnIniciarJuego.getScene().getWindow();

            gameController.setDatos(this.jugadorActual, this.servicioRecords, stageMenu);

            Stage stageJuego = new Stage();
            stageJuego.setTitle("PPTLS - Jugando: " + jugadorActual.getLogin());
            stageJuego.setScene(new Scene(root));
            stageJuego.setMaximized(true);
            stageJuego.show();

            stageMenu.hide();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar el juego: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }

    @FXML
    protected void abrirRecords() {
        try {

            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Records-view.fxml"));
            Parent root = loader.load();

            RecordsController controller = loader.getController();

            Stage stageMenu = (Stage) btnRecords.getScene().getWindow();

            controller.inicializarDatos(this.jugadorActual, this.servicioRecords, stageMenu);

            Stage stageRecords = new Stage();
            stageRecords.setTitle("Mis Récords - PPTLS");
            stageRecords.setScene(new Scene(root));
            stageRecords.show();

            stageMenu.hide();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo cargar la ventana de récords: " + e.getMessage());
        }
    }

    @FXML
    protected void abrirRanking() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Ranking-view.fxml"));
            Parent root = loader.load();

            RankingController controller = loader.getController();
            Stage stageMenu = (Stage) btnRanking.getScene().getWindow();

            controller.inicializarDatos(this.servicioRecords, stageMenu);

            Stage stageRanking = new Stage();
            stageRanking.setTitle("Ranking Global - PPTLS");
            stageRanking.setScene(new Scene(root));
            stageRanking.show();

            stageMenu.hide();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo cargar el Ranking: " + e.getMessage());
        }
    }

    @FXML
    protected void salirJuego() {
        Platform.exit();
    }

    @FXML
    protected void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro que deseas cerrar sesión?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            volverAlLogin();
        }
    }

    private void volverAlLogin() {
        try {
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            LoginController loginController = fxmlLoader.getController();

            loginController.setServicioJugadoresLocal(this.servicioJugadoresLocal);
            loginController.setServicioJugadoresOnline(this.servicioJugadoresOnline);

            loginController.setServicioRecordsLocal(this.servicioRecordsLocal);
            loginController.setServicioRecordsOnline(this.servicioRecordsOnline);

            if (this.servicioJugadores == this.servicioJugadoresOnline) {
                loginController.clickOnline();
            } else {
                loginController.clicKLocal();
            }

            stage.setTitle("Iniciar Sesión");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al intentar regresar al Login: " + e.getMessage());
        }
    }
}