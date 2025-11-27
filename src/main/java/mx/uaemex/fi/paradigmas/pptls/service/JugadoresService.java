package mx.uaemex.fi.paradigmas.pptls.service;

import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;

import java.util.ArrayList;

public interface JugadoresService {
    public ArrayList<Jugador> consultar();
    public ArrayList<Jugador> consultar(Jugador j);
    public Jugador insertar(Jugador j);
    public Jugador actualizar(Jugador j);
    public boolean borrar(Jugador j);
}
