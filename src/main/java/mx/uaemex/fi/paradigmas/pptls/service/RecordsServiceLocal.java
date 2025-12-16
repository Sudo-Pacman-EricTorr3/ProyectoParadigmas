package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecordsServiceLocal implements RecordsService {
    //esta lista vivirá en la memoria RAM mientras el programa esté abierto
    //entonces si cerramos el programa se borrará
    private ArrayList<Record> memoria;

    public RecordsServiceLocal() {
        this.memoria = new ArrayList<>();
    }

    @Override
    public ArrayList<Record> consultarRecords() {

        //como si fuera un order by puntaje  en SQL
        memoria.sort((r1, r2) -> Integer.compare(r2.getRecord(), r1.getRecord()));

        return memoria;
    }

    @Override
    public void guardarRecord(Record r) {
        //lo agregamos a la lista
        memoria.add(r);
        System.out.println("Récord guardado en memoria RAM (Local).");
    }
}
