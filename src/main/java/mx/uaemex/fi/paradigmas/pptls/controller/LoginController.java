package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.Application;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    @FXML
    private Button btnModoOnline;
    @FXML
    private Button btnModoLocal;
    @FXML
    private Label lblModoActual;
    @FXML
    private TextField txtFldLogin;
    @FXML
    private PasswordField txtFldPassword;
    @FXML
    private Label lblMensaje;

    private JugadoresService servicioJugadoresLocal;
    private JugadoresService servicioJugadoresOnline;
    private JugadoresService servicioJugadores;

    private RecordsService servicioRecordsLocal;
    private RecordsService servicioRecordsOnline;
    private RecordsService servicioRecords;

    public void setServicioJugadoresLocal(JugadoresService s) {
        this.servicioJugadoresLocal = s;
    }
    public void setServicioJugadoresOnline(JugadoresService s) {
        this.servicioJugadoresOnline = s;
    }
    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
    }

    public void setServicioRecords(RecordsService s) {
        this.servicioRecords = s;
    }
    public void setServicioRecordsOnline(RecordsService s) {
        this.servicioRecordsOnline = s;
    }
    public void setServicioRecordsLocal(RecordsService s) {
        this.servicioRecordsLocal = s;
    }

    @FXML
    public void clickOnline() {
        this.servicioJugadores = this.servicioJugadoresOnline;
        this.servicioRecords = this.servicioRecordsOnline;

        System.out.println("Utilizando Servicio ONLINE");

        lblModoActual.setText("Modo: Multijugador (Online)");
        txtFldPassword.setDisable(false);

        btnModoOnline.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        btnModoLocal.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
    }

    @FXML
    public void clicKLocal() {
        this.servicioJugadores = this.servicioJugadoresLocal;
        this.servicioRecords = this.servicioRecordsLocal;

        System.out.println("Utilizando Servicio LOCAL");

        lblModoActual.setText("Modo: Local");
        txtFldPassword.setDisable(false);

        btnModoOnline.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        btnModoLocal.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
    }

    @FXML
    protected void entrar() {
        String login;
        String password;

        ArrayList<Jugador> consultados = null;
        Jugador jugador = new Jugador();

        login = this.txtFldLogin.getText();
        password = this.txtFldPassword.getText();

        jugador.setLogin(login);
        jugador.setPassword(password);

        consultados = this.servicioJugadores.consultar(jugador);

        if (consultados == null || consultados.isEmpty()) {
            mostrarError("Error: Credenciales incorrectas"); // Usuario no existe
        } else {
            Jugador jugadorActual = consultados.get(0);

            if (jugadorActual.getPassword().equals(password)) {

                if (jugadorActual.isActivo()) {
                    System.out.println("Login exitoso, abriendo menú del juego...");
                    abrirVentanaMenu(jugadorActual);
                } else {
                    mostrarError("Error: Cuenta inactiva");
                }

            } else {
                mostrarError("Error: Credenciales incorrectas");
            }
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setVisible(true);

        System.out.println(mensaje);
    }

    private void abrirVentanaMenu(Jugador jugadorActual) {
        try {
            Stage stageActual = (Stage) txtFldLogin.getScene().getWindow();
            stageActual.close();

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            MenuController menuController = fxmlLoader.getController();

            menuController.setJugador(jugadorActual);

            menuController.setServicioJugadoresLocal(servicioJugadoresLocal);
            menuController.setServicioJugadoresOnline(servicioJugadoresOnline);

            menuController.setServicioRecordsLocal(servicioRecordsLocal);
            menuController.setServicioRecordsOnline(servicioRecordsOnline);

            menuController.setServicioRecords(servicioRecords);
            menuController.setServicioJugadores(servicioJugadores);

            Stage stageNuevo = new Stage();
            stageNuevo.setTitle("Juego PPTLS");
            stageNuevo.setScene(scene);
            stageNuevo.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar el juego.");
        }
    }

    @FXML
    protected void registrarse() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) txtFldLogin.getScene().getWindow();
            stage.setTitle("Registro");
            stage.setScene(scene);

            RegisterController registerController = fxmlLoader.getController();

            registerController.setServicioJugadoresOnline(this.servicioJugadoresOnline);
            registerController.setServicioJugadoresLocal(this.servicioJugadoresLocal);

            registerController.setServicioRecordsLocal(this.servicioRecordsLocal);
            registerController.setServicioRecordsOnline(this.servicioRecordsOnline);

            if (this.servicioJugadores == this.servicioJugadoresOnline) {
                registerController.clickOnline();
            } else {
                registerController.clicKLocal();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
