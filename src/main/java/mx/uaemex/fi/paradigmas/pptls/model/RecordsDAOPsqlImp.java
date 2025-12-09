package mx.uaemex.fi.paradigmas.pptls.model;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.model.data.Juego;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class RecordsDAOPsqlImp extends AbstractSqlDAO implements RecordsDAO{

    @Override
    public Record insertar(Record r) {
        PreparedStatement pstmt;
        String sql;
        ArrayList<Record> consultados;
        int jugadorId, juegoId, puntaje;
        Date fecha;

        try {
            sql = "INSERT INTO record (jugador_id, juego_id, fecha, puntaje) VALUES (?, ?, ?, ?)";
            pstmt = this.conexion.prepareStatement(sql);

            jugadorId = r.getJugador().getId();
            juegoId = r.getJuego().getId();
            puntaje = r.getRecord();
            fecha = r.getFecha();

            if (jugadorId <= 0 || juegoId <= 0) {
                throw new RuntimeException("Datos incompletos: Se requiere ID de jugador y juego.");
            }

            pstmt.setInt(1, jugadorId);
            pstmt.setInt(2, juegoId);

            if (fecha != null) {
                pstmt.setDate(3, new java.sql.Date(fecha.getTime()));
            } else {
                // Si viene nula, asignamos la fecha actual y actualizamos el objeto local
                Date fechaActual = new java.util.Date();
                pstmt.setDate(3, new java.sql.Date(fechaActual.getTime()));
                r.setFecha(fechaActual);
            }

            pstmt.setInt(4, puntaje);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        consultados = this.consultar(r);

        if (consultados != null && !consultados.isEmpty()) {
            return consultados.get(0);
        }

        return null;
    }

    @Override
    public ArrayList<Record> consultar() {
        String sql;
        Statement stmt;
        ResultSet resultado;
        int id, jugadorId, juegoId, puntaje;
        Date fecha;
        Record r;
        Jugador j;
        Juego g;

        try {
            sql = "SELECT * FROM record";
            stmt = this.conexion.createStatement();
            resultado = stmt.executeQuery(sql);
            ArrayList<Record> records = new ArrayList<>();

            while (resultado.next()) {
                id = resultado.getInt("id");
                jugadorId = resultado.getInt("jugador_id");
                juegoId = resultado.getInt("juego_id");
                fecha = resultado.getDate("fecha");
                puntaje = resultado.getInt("puntaje");

                r = new Record();
                r.setId(id);

                j = new Jugador();
                j.setId(jugadorId);
                r.setJugador(j);

                g = new Juego();
                g.setId(juegoId);
                r.setJuego(g);

                r.setFecha(fecha);
                r.setRecord(puntaje);

                records.add(r);
            }
            return records;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Record> consultar(Record r) {
        ArrayList<Record> encontrados;
        StringBuilder sql;
        int numColumnas = 0;
        Statement stmt;
        ResultSet resultado;
        Record record;
        int id, puntaje;
        Date fecha;
        Jugador jugador;
        Juego juego;

        try {
            encontrados = new ArrayList<>();
            stmt = this.conexion.createStatement();

            // Paso (1):
            sql = new StringBuilder("SELECT * FROM record");

            // Paso (2): Construcción dinámica del WHERE

            id = r.getId();
            if (id > 0) {
                sql.append(" WHERE (id=" + id);
                numColumnas++;
            }

            jugador = r.getJugador();
            if (jugador != null && r.getJugador().getId() > 0) {
                int idJugador = jugador.getId();
                if (numColumnas != 0) {
                    sql.append(" AND jugador_id=" + idJugador);
                } else {
                    sql.append(" WHERE (jugador_id=" + idJugador);
                }
                numColumnas++;
            }

            juego = r.getJuego();
            if (juego != null && r.getJuego().getId() > 0) {
                int idJuego = juego.getId();
                if (numColumnas != 0) {
                    sql.append(" AND juego_id=" + idJuego);
                } else {
                    sql.append(" WHERE (juego_id=" + idJuego);
                }
                numColumnas++;
            }

            puntaje = r.getRecord();
            if (puntaje > 0) {
                if (numColumnas != 0) {
                    sql.append(" AND puntaje=").append(r.getRecord());
                } else {
                    sql.append(" WHERE (puntaje=").append(r.getRecord());
                }
                numColumnas++;
            }

            fecha = r.getFecha();
            if (fecha != null) {
                // Convertimos a Timestamp para incluir la hora exacta en la consulta
                java.sql.Timestamp fechaSql = new java.sql.Timestamp(fecha.getTime());

                if (numColumnas != 0) {
                    // Se usan comillas simples '' para fechas en SQL
                    sql.append(" AND fecha='" + fechaSql + "'");
                } else {
                    sql.append(" WHERE (fecha='" + fechaSql + "'");
                }
                numColumnas++;
            }

            if (numColumnas != 0) {
                sql.append(")");
            }

            resultado = stmt.executeQuery(sql.toString());

            // Paso (4):
            while (resultado.next()) {
                record = new Record();
                record.setId(resultado.getInt("id"));

                Jugador j = new Jugador();
                j.setId(resultado.getInt("jugador_id"));
                record.setJugador(j);

                Juego g = new Juego();
                g.setId(resultado.getInt("juego_id"));
                record.setJuego(g);

                record.setFecha(resultado.getTimestamp("fecha"));
                record.setRecord(resultado.getInt("puntaje"));

                encontrados.add(record);
            }

            return encontrados;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(Record r) {
        String sql;
        PreparedStatement stmt;
        try{
            sql="update record set (jugador=?,juego=?,fecha=?,record=?)";
            stmt=this.conexion.prepareStatement(sql);
            stmt.setInt(1, r.getJugador().getId());
            stmt.setInt(2,r.getJuego().getId());
            stmt.setDate(3,new java.sql.Date(r.getFecha().getTime()));
            stmt.setInt(4,r.getRecord());
            stmt.setInt(5,r.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(Record r) {
        String sql;
        PreparedStatement pstmt;

        try {
            sql = "DELETE FROM record WHERE jugador_id=? AND juego_id=? AND fecha=?";
            pstmt = this.conexion.prepareStatement(sql);

            pstmt.setInt(1, r.getJugador().getId());
            pstmt.setInt(2, r.getJuego().getId());
            pstmt.setDate(3, new java.sql.Date(r.getFecha().getTime()));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
