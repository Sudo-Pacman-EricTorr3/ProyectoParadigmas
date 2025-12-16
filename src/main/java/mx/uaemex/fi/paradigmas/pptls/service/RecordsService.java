package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.util.ArrayList;

public interface RecordsService {

        public ArrayList<Record> consultar();
        public ArrayList<Record> consultar(Record r);
        public Record insertar(Record r);
        public Record actualizar(Record r);
        public boolean borrar(Record r);
    }


