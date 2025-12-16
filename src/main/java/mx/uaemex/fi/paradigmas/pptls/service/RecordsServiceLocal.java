package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAO;
import mx.uaemex.fi.paradigmas.pptls.model.RecordsDAO;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.util.ArrayList;

public class RecordsServiceLocal implements RecordsService{
private RecordsDAO dao;


    public void setDao(RecordsDAO dao) {
        this.dao = dao;
    }

    @Override
    public ArrayList<Record> consultar() {
        return dao.consultar();
    }

    @Override
    public ArrayList<Record> consultar(Record r) {
        return dao.consultar(r);
    }

    @Override
    public Record insertar(Record r) {
        return dao.insertar(r);
    }

    @Override
    public Record actualizar(Record r) {
        dao.actualizar(r);
        return dao.consultar(r).get(0);
    }

    @Override
    public boolean borrar(Record r) {
        dao.borrar(r);
        return true;
    }
}
