package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.HelloApplication;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    @FXML
    private TextField txtFldLogin;

    @FXML
    private PasswordField txtFldPassword;

    @FXML
    private Label lblMensaje;

    private JugadoresService servicio;

    public void setServicio(JugadoresService s) {
        this.servicio = s;
    }

    @FXML
    protected void onLoginButtonClick() {
        String login;
        String password;

        ArrayList<Jugador> consultados = null;
        Jugador jugador = new Jugador();

        login = this.txtFldLogin.getText();
        password = this.txtFldPassword.getText();

        jugador.setLogin(login);
        jugador.setPassword(password);

        consultados = servicio.consultar(jugador);

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

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Menu-view.fxml"));
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
