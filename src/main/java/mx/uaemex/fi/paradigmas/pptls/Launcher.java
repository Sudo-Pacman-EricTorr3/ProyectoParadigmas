package mx.uaemex.fi.paradigmas.pptls;

import javafx.application.Application;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.model.data.Juego;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresServiceLocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class Launcher {

    public static void main(String[] args) {
        Application.launch(HelloApplication.class, args);
    }

}
