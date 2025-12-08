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
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

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

    private boolean esModoOnline = true;

    private JugadoresService servicioLocal;
    private JugadoresService servicioOnline;

    public void setServicioLocal(JugadoresService s) {
        this.servicioLocal = s;
    }
    public void setServicioOnline(JugadoresService s) {
        this.servicioOnline = s;
    }

    @FXML
    protected void clickOnline() {
        esModoOnline = true;

        lblModoActual.setText("Modo: Multijugador (Online)");
        txtFldLogin.setPromptText("Usuario");
        txtFldPassword.setDisable(false);

        btnModoOnline.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        btnModoLocal.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
    }

    @FXML
    protected void clicKLocal() {

        esModoOnline = false;

        lblModoActual.setText("Modo: Local");
        txtFldLogin.setPromptText("Nickname");
        // txtFldPassword.setDisable(true);

        btnModoOnline.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        btnModoLocal.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
    }

    @FXML
    protected void Entrar() {
        String login;
        String password;

        ArrayList<Jugador> consultados = null;
        Jugador jugador = new Jugador();

        login = this.txtFldLogin.getText();
        password = this.txtFldPassword.getText();

        jugador.setLogin(login);
        jugador.setPassword(password);

        if (esModoOnline) {
            consultados = servicioOnline.consultar(jugador);
            System.out.println("Utilizando Servicio ONLINE");
        } else {
            consultados = servicioLocal.consultar(jugador);
            System.out.println("Utilizando Servicio LOCAL");
        }

        if (consultados == null || consultados.isEmpty()) {
            mostrarError("Error: Credenciales incorrectas"); // Usuario no existe
        } else {
            Jugador encontrado = consultados.get(0);

            if (encontrado.getPassword().equals(password)) {

                if (encontrado.isActivo()) {
                    System.out.println("Login exitoso, abriendo juego...");
                    abrirVentanaJuego();
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

    private void abrirVentanaJuego() {
        try {
            Stage stageActual = (Stage) txtFldLogin.getScene().getWindow();
            stageActual.close();

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stageNuevo = new Stage();
            stageNuevo.setTitle("Juego PPTLS");
            stageNuevo.setScene(scene);
            stageNuevo.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar la siguiente ventana");
        }
    }

}
