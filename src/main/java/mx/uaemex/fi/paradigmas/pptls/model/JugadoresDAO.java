package mx.uaemex.fi.paradigmas.pptls.model;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import java.util.ArrayList;

public interface JugadoresDAO {

    public Jugador insertar(Jugador j);
    public ArrayList<Jugador> consultar();
    public ArrayList<Jugador> consultar(Jugador j);
    public void actualizar(Jugador j);
    public void borrar(Jugador j);
}
