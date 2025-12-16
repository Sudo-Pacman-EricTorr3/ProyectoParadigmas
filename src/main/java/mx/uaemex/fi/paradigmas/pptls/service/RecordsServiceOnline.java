package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.RecordsDAO;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import java.util.ArrayList;
public class RecordsServiceOnline implements RecordsService{
    private RecordsDAO daoOracle; //este es para que nos conectemos al a db o el servicio de Gael

    public void setDao(RecordsDAO dao) {
        this.daoOracle = dao;
    }

    @Override
    public ArrayList<Record> consultarRecords() {
        System.out.println("Consultando records desde ORACLE...");
        return daoOracle.consultar();
    }

    @Override
    public void guardarRecord(Record r) {
        System.out.println("Guardando record en ORACLE...");
        daoOracle.insertar(r);
    }
    public void actualizarRecord(Record r){
        daoOracle.actualizar(r);
    }

}
