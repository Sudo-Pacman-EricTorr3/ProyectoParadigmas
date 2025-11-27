package mx.uaemex.fi.paradigmas.pptls.model.data;

public class Juego extends ElementoConID implements Data{
    private String nombre;

    public Juego() {
        this.nombre = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
