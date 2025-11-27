package mx.uaemex.fi.paradigmas.pptls.model;
import java.sql.Connection;

public abstract class AbstractSqlDAO {
    protected Connection conexion;

    public AbstractSqlDAO() {
        this.conexion = null;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}
