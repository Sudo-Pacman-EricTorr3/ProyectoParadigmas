package mx.uaemex.fi.paradigmas.pptls.model;

import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;

public class JugadoresDAOPsqlImp extends AbstractSqlDAO implements JugadoresDAO {

    @Override
    public Jugador insertar(Jugador j) {
        PreparedStatement stmt;
        String sql;
        String login, password, correo;
        ArrayList<Jugador> consultados;

        try {
            sql = "INSERT INTO jugadores (login, password, correo, activo) VALUES (?, ?, ?, ?)";
            stmt = this.conexion.prepareStatement(sql);

            login = j.getLogin();
            password = j.getPassword();
            correo = j.getCorreo();

            if (login == null || password == null || correo == null) {
                throw new RuntimeException("Informacion insuficiente, NO es posible hacer el registro");
            }

            Jugador jugadorExistente = new Jugador();
            jugadorExistente.setLogin(login);
            if (!this.consultar(jugadorExistente).isEmpty()) {
                throw new RuntimeException("El login '" + login + "' ya está ocupado.");
            }

            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, correo);
            stmt.setBoolean(4, true);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        consultados = this.consultar(j);
        return consultados.get(0);
    }

    @Override
    public ArrayList<Jugador> consultar() {
        Statement stmt;
        String sql;
        ResultSet resultado;

        try {
            sql = "SELECT * FROM jugadores";
            stmt = this.conexion.createStatement();
            resultado = stmt.executeQuery(sql);
            ArrayList<Jugador> jugadores = new ArrayList<>();

            while (resultado.next()) {
                Jugador j = new Jugador();
                j.setId(resultado.getInt(1)); //El indice inicia en 1
                j.setLogin(resultado.getString("login"));
                j.setCorreo(resultado.getString("correo"));
                j.setPassword(resultado.getString("password"));
                j.setActivo(resultado.getBoolean("activo"));
                jugadores.add(j);
            }
            return jugadores;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Jugador> consultar(Jugador jugador) {
        ArrayList<Jugador> encontrados;
        StringBuilder sql;
        int id;
        int numColumnas = 0;
        String login, correo, password;
        Statement stmt;
        ResultSet resultado;
        Jugador player;

        try {
            encontrados = new ArrayList<>();
            stmt = this.conexion.createStatement();


            sql = new StringBuilder("SELECT * FROM jugadores");


            id = jugador.getId();
            if (id > 0) {
                sql.append(" WHERE (id=" + id);
                numColumnas++;
            }

            login = jugador.getLogin();
            if (login != null) {
                if (numColumnas != 0) {
                    sql.append(" AND login='" + login + "'");
                } else {
                    sql.append(" WHERE(login='" + login + "'");
                }
                numColumnas++;
            }

            password = jugador.getPassword();
            if (password != null) {
                if (numColumnas != 0) {
                    sql.append(" AND password='" + password + "'");
                } else {
                    sql.append(" WHERE (password='" + password + "'");
                }
                numColumnas++;
            }

            correo = jugador.getCorreo();
            if (correo != null) {
                if (numColumnas != 0) {
                    sql.append(" AND correo='" + correo + "'");
                } else {
                    sql.append(" WHERE (correo='" + correo + "'");
                }
                numColumnas++;
            }

            if (numColumnas != 0) {
                sql.append(")");
            }


            resultado = stmt.executeQuery(sql.toString());


            while (resultado.next()) {
                player = new Jugador();
                player.setId(resultado.getInt(1));
                player.setLogin(resultado.getString("login"));
                player.setPassword(resultado.getString("password"));
                player.setCorreo(resultado.getString("correo"));
                player.setActivo(resultado.getBoolean("activo"));
                encontrados.add(player);
            }

            return encontrados;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(Jugador j) {
        StringBuilder sql;
        String password, correo;
        Boolean activo;
        int numColumnas = 0;
        Statement stmt;

        try {
            stmt = this.conexion.createStatement();
            sql = new StringBuilder("UPDATE jugadores SET");

            //Password
            password = j.getPassword();
            if (password != null) {
                sql.append(" password='" + password + "'");
                numColumnas++;
            }

            //Correo
            correo = j.getCorreo();
            if (correo != null) {
                if (numColumnas > 0) {
                    sql.append(",");
                }
                sql.append(" correo='" + correo + "'");
                numColumnas++;
            }

            //Activo
            activo = j.isActivo();
            if (activo) {
                if (numColumnas > 0) {
                    sql.append(",");
                }
                sql.append(" activo='true'");
                numColumnas++;
            }

            //WHERE (Usando el login)
            sql.append(" WHERE login='" + j.getLogin() + "'");

            if (numColumnas > 0) {
                stmt.executeUpdate(sql.toString());
            } else {
                System.out.println("No hay campos a actualizar para el jugador: " + j.getLogin());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(Jugador j) {
        PreparedStatement pstmt;
        String sql = "UPDATE jugadores SET activo='false' WHERE login=?";

        try {
            pstmt = this.conexion.prepareStatement(sql);
            pstmt.setString(1, j.getLogin());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
