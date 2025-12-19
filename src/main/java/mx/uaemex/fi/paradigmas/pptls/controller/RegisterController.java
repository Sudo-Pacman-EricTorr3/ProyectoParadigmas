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

public class RegisterController {
    @FXML
    private Button btnModoOnline;
    @FXML
    private Button btnModoLocal;
    @FXML
    private Label lblModoActual;
    @FXML
    private TextField txtFldLogin;
    @FXML
    private TextField txtFldEmail;
    @FXML private
    PasswordField txtFldPassword;
    @FXML private
    PasswordField txtFldConfirmPassword;
    @FXML private
    Label lblMensaje;

    private JugadoresService servicioJugadoresOnline;
    private JugadoresService servicioJugadoresLocal;
    private JugadoresService servicioJugadores;

    private RecordsService servicioRecordsLocal;
    private RecordsService servicioRecordsOnline;
    private RecordsService servicioRecords;

    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
    }
    public void setServicioJugadoresOnline(JugadoresService s) {
        this.servicioJugadoresOnline = s;
    }
    public void setServicioJugadoresLocal(JugadoresService s) {
        this.servicioJugadoresLocal = s;
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
    protected void clickOnline() {
        this.servicioJugadores = servicioJugadoresOnline;
        this.servicioRecords = servicioRecordsOnline;

        System.out.println("Registrando en Servicio ONLINE");

        lblModoActual.setText("Registrando en: Servicio Online (Multijugador)");
        txtFldPassword.setDisable(false);

        btnModoOnline.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        btnModoLocal.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
    }

    @FXML
    protected void clicKLocal() {
        this.servicioJugadores = servicioJugadoresLocal;
        this.servicioRecords = servicioRecordsLocal;

        System.out.println("Registrando en Servicio LOCAL");

        lblModoActual.setText("Registrando en: Servicio Local");
        txtFldPassword.setDisable(false);

        btnModoOnline.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        btnModoLocal.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
    }

    @FXML
    protected void registrarse() {
        String login = txtFldLogin.getText();
        String password = txtFldPassword.getText();
        String confirmPassword = txtFldConfirmPassword.getText();
        String correo = txtFldEmail.getText();

        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || correo.isEmpty()) {
            mostrarError("Error: Todos los campos son obligatorios");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarError("Error: Las contraseñas no coinciden");
            return;
        }

        Jugador nuevoJugador = new Jugador();
        nuevoJugador.setLogin(login);
        nuevoJugador.setPassword(password);
        nuevoJugador.setCorreo(correo);
        nuevoJugador.setActivo(true);

        try {
            Jugador jugadorRegistrado = servicioJugadores.insertar(nuevoJugador);
            System.out.println("Registro exitoso: " + jugadorRegistrado.getLogin());
            abrirVentanaMenu(jugadorRegistrado);

        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
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
            stageNuevo.setResizable(false);
            stageNuevo.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar el juego.");
        }
    }

    @FXML
    private void volverAlLogin() {
        try {
            Stage stage = (Stage) txtFldLogin.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Iniciar Sesión");
            stage.setScene(scene);

            LoginController loginController = fxmlLoader.getController();

            loginController.setServicioJugadoresOnline(servicioJugadoresOnline);
            loginController.setServicioJugadoresLocal(servicioJugadoresLocal);

            loginController.setServicioRecordsLocal(servicioRecordsLocal);
            loginController.setServicioRecordsOnline(servicioRecordsOnline);

            if (this.servicioJugadores == this.servicioJugadoresOnline) {
                loginController.clickOnline();
            } else {
                loginController.clicKLocal();
            }

        } catch (IOException e) {
            mostrarError("Error: No se pudo registrar.");
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setVisible(true);

        System.out.println(mensaje);
    }
}