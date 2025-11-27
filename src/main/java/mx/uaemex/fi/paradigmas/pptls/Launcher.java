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
        JugadoresDAOPsqlImp dao;
        ArrayList<Jugador> equipo;
        //String url = "jdbc:postgresql://localhost:5433/pptls?user=postgres&password=admin";
        String url = "jdbc:postgresql://database-1.cnsiwgwsie1g.us-east-2.rds.amazonaws.com/pptls?user=postgres&password=Admin-AWS-123";
        Jugador player = new Jugador();
        Juego juego;
        JugadoresServiceLocal servicio;

        player.setLogin("yael");
        player.setActivo(true);


        servicio = new JugadoresServiceLocal();

        try {
            Connection conn = DriverManager.getConnection(url);
            dao = new JugadoresDAOPsqlImp();
            dao.setConexion(conn);
            servicio.setDao(dao);

            servicio.actualizar(player);
            equipo = servicio.consultar(player);
            for(Jugador j:equipo){
                System.out.println(j.getLogin()+ " " + j.getCorreo() + " " + j.isActivo() + " " + j.getPassword());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
