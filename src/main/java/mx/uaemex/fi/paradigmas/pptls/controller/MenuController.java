package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;

public class MenuController {

    @FXML
    private Label lblBienvenida;

    private JugadoresService servicioJugadores;
    private Jugador jugadorActual;


    public void setJugador(Jugador jugador) {
        this.jugadorActual = jugador;

        if (lblBienvenida != null) {
            lblBienvenida.setText("! Bienvenido, " + jugador.getLogin() + " !");
        }
    }

    public void setServicioJugadores(JugadoresService s) {
        this.servicioJugadores = s;
    }
}