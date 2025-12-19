package mx.uaemex.fi.paradigmas.pptls.model;

import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class RecordsDAOLocalImp implements RecordsDAO {

    private ArrayList<Record> guardadosLocal;

    public RecordsDAOLocalImp() {
        this.guardadosLocal = new ArrayList<>();
    }

    @Override
    public Record insertar(Record r) {
        ArrayList<Record> consultados;
        Timestamp fecha;

        if (r.getJugador() == null || r.getJuego() == null) {
            throw new RuntimeException("Datos incompletos: Se requiere Jugador y Juego para insertar un record.");
        }

        if (r.getJuego().getId() == 1) {
            r.getJuego().setNombre("PPTLS");
        }

        fecha = r.getFecha();

        if (fecha == null) {
            fecha = new Timestamp(new Date().getTime());
            r.setFecha(fecha);
        }

        r.setId(guardadosLocal.size() + 1);
        guardadosLocal.add(r);

        consultados = this.consultar(r);

        return consultados.get(0);
    }

    @Override
    public ArrayList<Record> consultar() {
        return new ArrayList<>(guardadosLocal);
    }

    @Override
    public ArrayList<Record> consultar(Record r) {
        ArrayList<Record> encontrados = new ArrayList<>();

        int id, jugadorId, juegoId, puntaje;
        Timestamp fecha;

        for (Record recordGuardado : guardadosLocal) {
            boolean coincide = true;

            id = r.getId();
            if (id > 0) {
                if (recordGuardado.getId() != id) {
                    coincide = false;
                }
            }

            if (r.getJugador() != null) {
                jugadorId = r.getJugador().getId();
                if (jugadorId > 0) {
                    if (recordGuardado.getJugador().getId() != jugadorId) {
                        coincide = false;
                    }
                }
            }

            if (r.getJuego() != null) {
                juegoId = r.getJuego().getId();
                if (juegoId > 0) {
                    if (recordGuardado.getJuego().getId() != juegoId) {
                        coincide = false;
                    }
                }
            }

            puntaje = r.getRecord();
            if (puntaje > 0) {
                if (recordGuardado.getRecord() != puntaje) {
                    coincide = false;
                }
            }

            fecha = r.getFecha();
            if (fecha != null) {
                if (!recordGuardado.getFecha().equals(fecha)) {
                    coincide = false;
                }
            }

            if (coincide) {
                encontrados.add(recordGuardado);
            }
        }
        encontrados.sort((r1, r2) -> Integer.compare(r2.getRecord(), r1.getRecord()));
        return encontrados;
    }

    @Override
    public void actualizar(Record r) {
        int puntaje;

        if (r.getJugador() == null || r.getJuego() == null || r.getFecha() == null) {
            System.out.println("Faltan datos para identificar el record a actualizar.");
            return;
        }

        for (Record recordGuardado : guardadosLocal) {
            boolean mismoJugador = recordGuardado.getJugador().getId() == r.getJugador().getId();
            boolean mismoJuego = recordGuardado.getJuego().getId() == r.getJuego().getId();
            boolean mismaFecha = recordGuardado.getFecha().equals(r.getFecha());

            if (mismoJugador && mismoJuego && mismaFecha) {

                puntaje = r.getRecord();
                if (puntaje > 0) {
                    recordGuardado.setRecord(puntaje);
                }
                break;
            }
        }
    }

    @Override
    public void borrar(Record r) {

        if (r.getJugador() == null || r.getJuego() == null || r.getFecha() == null) {
            throw new RuntimeException("Información incompleta para borrar el record");
        }

        for (int i = 0; i < guardadosLocal.size(); i++) {
            Record recordGuardado = guardadosLocal.get(i);

            boolean mismoJugador = recordGuardado.getJugador().getId() == r.getJugador().getId();
            boolean mismoJuego = recordGuardado.getJuego().getId() == r.getJuego().getId();
            boolean mismaFecha = recordGuardado.getFecha().equals(r.getFecha());

            if (mismoJugador && mismoJuego && mismaFecha) {
                guardadosLocal.remove(i);
                break;
            }
        }
    }
}
