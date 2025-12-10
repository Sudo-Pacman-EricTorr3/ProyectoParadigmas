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

    public void setServicioJugadoresLocal(JugadoresService s) {
        this.servicioJugadoresLocal = s;
    }
    public void setServicioJugadoresOnline(JugadoresService s) {
        this.servicioJugadoresOnline = s;
    }
    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
    }

    @FXML
    public void clickOnline() {
        this.servicioJugadores = this.servicioJugadoresOnline;
        System.out.println("Utilizando Servicio ONLINE");

        lblModoActual.setText("Modo: Multijugador (Online)");
        txtFldPassword.setDisable(false);

        btnModoOnline.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        btnModoLocal.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
    }

    @FXML
    public void clicKLocal() {
        this.servicioJugadores = this.servicioJugadoresLocal;
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
                    System.out.println("Login exitoso, abriendo juego...");
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

            menuController.setServicioJugadores(servicioJugadores);
            menuController.setJugador(jugadorActual);

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
