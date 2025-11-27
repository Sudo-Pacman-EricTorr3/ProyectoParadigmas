package mx.uaemex.fi.paradigmas.pptls.model.data;

public abstract class ElementoConID {
    protected int id;

    public ElementoConID() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
