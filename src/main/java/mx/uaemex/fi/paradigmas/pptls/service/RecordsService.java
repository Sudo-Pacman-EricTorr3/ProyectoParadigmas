package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import java.util.ArrayList;

public interface RecordsService {
    public ArrayList<Record> consultarRecords();
    public void guardarRecord(Record r);
}
