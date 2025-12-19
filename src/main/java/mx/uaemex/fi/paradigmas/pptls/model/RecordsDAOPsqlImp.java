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
        Timestamp fecha;

        try {
            sql = "INSERT INTO records (jugador_id, juego_id, fecha, puntaje) VALUES (?, ?, ?, ?)";
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
                fecha = new Timestamp(r.getFecha().getTime());
            } else {
                // Si viene nula, asignamos la fecha actual y actualizamos el objeto local
                Timestamp fechaActual = new Timestamp(new Date().getTime());
                r.setFecha(fechaActual);
                fecha = fechaActual;
            }

            pstmt.setTimestamp(3, fecha);
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
        Timestamp fecha;
        Record r;
        Jugador j;
        Juego g;

        try {
            sql = "SELECT r.id, r.puntaje, r.fecha, " +
                    "j.id AS jugador_id, j.login, j.correo, " +
                    "g.id AS juego_id, g.nombre AS nombre_juego " +
                    "FROM records r " +
                    "INNER JOIN jugadores j ON r.jugador_id = j.id " +
                    "INNER JOIN juegos g ON r.juego_id = g.id";

            stmt = this.conexion.createStatement();
            resultado = stmt.executeQuery(sql);
            ArrayList<Record> records = new ArrayList<>();

            while (resultado.next()) {
                id = resultado.getInt("id");
                jugadorId = resultado.getInt("jugador_id");
                juegoId = resultado.getInt("juego_id");
                fecha = resultado.getTimestamp("fecha");
                puntaje = resultado.getInt("puntaje");

                r = new Record();
                r.setId(id);

                j = new Jugador();
                j.setId(jugadorId);
                j.setLogin(resultado.getString("login"));
                j.setCorreo(resultado.getString("correo"));
                r.setJugador(j);

                g = new Juego();
                g.setId(juegoId);
                g.setNombre(resultado.getString("nombre_juego"));
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
        int id, puntaje;
        Timestamp fecha;
        Jugador jugador;
        Juego juego;

        try {
            encontrados = new ArrayList<>();
            stmt = this.conexion.createStatement();

            sql = new StringBuilder("SELECT r.id, r.puntaje, r.fecha, " +
                    "j.id AS jugador_id, j.login, j.correo, " +
                    "g.id AS juego_id, g.nombre AS nombre_juego " +
                    "FROM records r " +
                    "INNER JOIN jugadores j ON r.jugador_id = j.id " +
                    "INNER JOIN juegos g ON r.juego_id = g.id");

            id = r.getId();
            if (id > 0) {
                sql.append(" WHERE (r.id=" + id);
                numColumnas++;
            }

            jugador = r.getJugador();
            if (jugador != null && r.getJugador().getId() > 0) {
                int idJugador = jugador.getId();
                if (numColumnas != 0) {
                    sql.append(" AND r.jugador_id=" + idJugador);
                } else {
                    sql.append(" WHERE (r.jugador_id=" + idJugador);
                }
                numColumnas++;
            }

            juego = r.getJuego();
            if (juego != null && r.getJuego().getId() > 0) {
                int idJuego = juego.getId();
                if (numColumnas != 0) {
                    sql.append(" AND r.juego_id=" + idJuego);
                } else {
                    sql.append(" WHERE (r.juego_id=" + idJuego);
                }
                numColumnas++;
            }

            puntaje = r.getRecord();
            if (puntaje > 0) {
                if (numColumnas != 0) {
                    sql.append(" AND r.puntaje=" + puntaje);
                } else {
                    sql.append(" WHERE (r.puntaje=" + puntaje);
                }
                numColumnas++;
            }

            fecha = r.getFecha();
            if (fecha != null) {
                if (numColumnas != 0) {
                    sql.append(" AND r.fecha='" + fecha + "'");
                } else {
                    sql.append(" WHERE (r.fecha='" + fecha + "'");
                }
                numColumnas++;
            }

            if (numColumnas != 0) {
                sql.append(")");
            }

            sql.append(" ORDER BY r.puntaje DESC");

            resultado = stmt.executeQuery(sql.toString());

            // Paso (4):
            while (resultado.next()) {
                Record record = new Record();
                record.setId(resultado.getInt("id"));

                Jugador j = new Jugador();
                j.setId(resultado.getInt("jugador_id"));
                j.setLogin(resultado.getString("login"));
                j.setCorreo(resultado.getString("correo"));
                record.setJugador(j);

                Juego g = new Juego();
                g.setId(resultado.getInt("juego_id"));
                g.setNombre(resultado.getString("nombre_juego"));
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
        StringBuilder sql;
        int jugadorId, juegoId, puntaje, numColumnas = 0;
        Timestamp fecha;
        Statement stmt;

        try {
            stmt = this.conexion.createStatement();
            sql = new StringBuilder("UPDATE records SET");

            puntaje = r.getRecord();
            if(puntaje > 0){
                sql.append(" puntaje=" + puntaje);
                numColumnas++;
            }

            if (r.getJugador() == null || r.getJuego() == null || r.getFecha() == null) {
                throw new RuntimeException("Faltan datos para identificar el record a actualizar");
            }

            jugadorId = r.getJugador().getId();
            juegoId = r.getJuego().getId();
            fecha = r.getFecha();

            sql.append(" WHERE jugador_id=" + jugadorId);
            sql.append(" AND juego_id=" + juegoId);
            sql.append(" AND fecha='" + fecha + "'");

            if(numColumnas > 0) {
                stmt.executeUpdate(sql.toString());
            }else{
                System.out.println("No hay un puntaje para actualizar el record: " + r.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(Record r) {
        String sql;
        PreparedStatement pstmt;
        int jugadorId, juegoId, puntaje;
        Timestamp fecha;

        try {
            sql = "DELETE FROM records WHERE jugador_id=? AND juego_id=? AND fecha=?";
            pstmt = this.conexion.prepareStatement(sql);

            if (r.getJugador() == null || r.getJuego() == null || r.getFecha() == null) {
                throw new RuntimeException("Información incompleta para borrar el record");
            }

            jugadorId = r.getJugador().getId();
            juegoId = r.getJuego().getId();
            fecha = r.getFecha();

            pstmt.setInt(1, jugadorId);
            pstmt.setInt(2, juegoId);
            pstmt.setTimestamp(3, fecha);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
