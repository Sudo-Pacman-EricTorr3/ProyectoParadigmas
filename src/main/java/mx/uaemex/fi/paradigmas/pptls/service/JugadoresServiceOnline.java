package mx.uaemex.fi.paradigmas.pptls.service;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAO;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;

import java.util.ArrayList;

public class JugadoresServiceOnline implements JugadoresService{
    private JugadoresDAO dao;

    @Override
    public ArrayList<Jugador> consultar() {
        return dao.consultar();
    }

    @Override
    public ArrayList<Jugador> consultar(Jugador j) {
        return dao.consultar(j);
    }

    @Override
    public Jugador insertar(Jugador j) {
        return dao.insertar(j);
    }

    @Override
    public Jugador actualizar(Jugador j) {
        dao.actualizar(j);
        return dao.consultar(j).get(0);
    }

    @Override
    public boolean borrar(Jugador j) {
        dao.borrar(j);
        return true;
    }

    public void setDao(JugadoresDAO dao) {
        this.dao = dao;
    }
}
