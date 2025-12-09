package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.RecordsDAO;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.util.ArrayList;

public class RecordsServiceLocal implements RecordsService {
    private RecordsDAO dao;

    public void setDao(RecordsDAO dao) {
        this.dao = dao;
    }

    @Override
    public ArrayList<Record> consultarRecords() {

        return dao.consultar();
    }

    @Override
    public void guardarRecord(Record r) {
        dao.insertar(r);
    }
}
