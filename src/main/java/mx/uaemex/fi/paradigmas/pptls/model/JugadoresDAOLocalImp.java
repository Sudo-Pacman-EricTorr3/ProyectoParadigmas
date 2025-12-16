package mx.uaemex.fi.paradigmas.pptls.model;

import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;

import java.util.ArrayList;

public class JugadoresDAOLocalImp implements JugadoresDAO {

    private ArrayList<Jugador> guardadosLocal;

    public JugadoresDAOLocalImp() {
        this.guardadosLocal = new ArrayList<>();
    }

    @Override
    public Jugador insertar(Jugador j) {
        String login, password, correo;
        ArrayList<Jugador> consultados;

        login = j.getLogin();
        password = j.getPassword();
        correo = j.getCorreo();

        if(login == null || password == null || correo == null){
            throw new RuntimeException("Informacion insuficiente, NO es posible hacer el registro");
        }

        Jugador jugadorExistente = new Jugador();
        jugadorExistente.setLogin(login);

        if (!this.consultar(jugadorExistente).isEmpty()) {
            throw new RuntimeException("El login '" + login + "' ya está ocupado.");
        }

        j.setId(guardadosLocal.size() + 1);
        j.setActivo(true);

        guardadosLocal.add(j);

        consultados = this.consultar(j);
        return consultados.get(0);
    }

    @Override
    public ArrayList<Jugador> consultar() {
        return new ArrayList<>(guardadosLocal);
    }

    @Override
    public ArrayList<Jugador> consultar(Jugador j) {
        ArrayList<Jugador> encontrados;
        int id;
        String login, correo, password;

        encontrados = new ArrayList<>();

        for (Jugador jugadorGuardado : guardadosLocal) {
            boolean coincide = true;

            id = j.getId();
            if(id > 0){
                if (jugadorGuardado.getId() != id) {
                    coincide = false;
                }
            }

            login = j.getLogin();
            if(login != null){
                if (!jugadorGuardado.getLogin().equals(login)) {
                    coincide = false;
                }
            }

            password = j.getPassword();
            if (password != null){
                if (!jugadorGuardado.getPassword().equals(password)) {
                    coincide = false;
                }
            }

            correo = j.getCorreo();
            if (correo != null){
                if (!jugadorGuardado.getCorreo().equals(correo)) {
                    coincide = false;
                }
            }

            if (coincide) {
                encontrados.add(jugadorGuardado);
            }
        }

        return encontrados;
    }

    @Override
    public void actualizar(Jugador j) {
        String password, correo;
        Boolean activo;

        for (Jugador jugadorGuardado : guardadosLocal) {
            if (jugadorGuardado.getLogin().equals(j.getLogin())) {

                password = j.getPassword();
                if (password != null) {
                    jugadorGuardado.setPassword(password);
                }

                correo = j.getCorreo();
                if (correo != null) {
                    jugadorGuardado.setCorreo(correo);
                }

                activo = j.isActivo();
                if (activo) {
                    jugadorGuardado.setActivo(true);
                }

                break;
            }
        }
    }

    @Override
    public void borrar(Jugador j) {

        for (Jugador jugadorGuardado : guardadosLocal) {

            if (jugadorGuardado.getLogin().equals(j.getLogin())) {
                jugadorGuardado.setActivo(false);
                break;
            }
        }
    }
}