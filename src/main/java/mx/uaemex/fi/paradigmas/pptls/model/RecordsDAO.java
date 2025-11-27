package mx.uaemex.fi.paradigmas.pptls.model;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import java.util.ArrayList;

public interface RecordsDAO {

    public Record insertar(Record r);
    public ArrayList<Record> consultar();
    public ArrayList<Record> consultar(Record r);
    public void actualizar(Record r);
    public void borrar(Record r);

}
