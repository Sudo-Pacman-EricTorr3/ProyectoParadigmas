package mx.uaemex.fi.paradigmas.pptls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.controller.LoginController;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAOLocalImp;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.model.RecordsDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresServiceLocal;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresServiceOnline;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsServiceLocal;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsServiceOnline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application extends javafx.application.Application {

    Connection conn;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        LoginController LoginController;
        String url = "jdbc:postgresql://database-1.cnsiwgwsie1g.us-east-2.rds.amazonaws.com/pptls?user=postgres&password=Admin-AWS-123";

        JugadoresServiceLocal servicioJugadoresLocal = new JugadoresServiceLocal();
        JugadoresServiceOnline servicioJugadoresOnline = new JugadoresServiceOnline();
        RecordsServiceLocal servicioRecordsLocal = new RecordsServiceLocal();
        RecordsServiceOnline servicioRecordsOnline = new RecordsServiceOnline();

        try {
            conn = DriverManager.getConnection(url);

            JugadoresDAOPsqlImp daoJugadoresOnline = new JugadoresDAOPsqlImp();
            daoJugadoresOnline.setConexion(conn);
            servicioJugadoresOnline.setDao(daoJugadoresOnline);
            RecordsDAOPsqlImp daoRecordsOnline=new RecordsDAOPsqlImp();
            daoRecordsOnline.setConexion(conn);
            servicioRecordsOnline.setDao(daoRecordsOnline);
            JugadoresDAOLocalImp daoJugadoresLocal = new JugadoresDAOLocalImp();
            servicioJugadoresLocal.setDao(daoJugadoresLocal);


            System.out.println("Conexión a la BD exitosa.");

        } catch (SQLException e) {
            System.out.println("Error: No se pudo conectar a la Base de Datos.");
            System.out.println("Detalle del error: " + e.getMessage());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Iniciar Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        LoginController = fxmlLoader.getController();
        LoginController.setServicioJugadoresLocal(servicioJugadoresLocal);
        LoginController.setServicioJugadoresOnline(servicioJugadoresOnline);
        LoginController.setServicioRecordsLocal(servicioRecordsLocal);
        LoginController.setServicioRecordsOnline(servicioRecordsOnline);
        LoginController.clickOnline();
    }

    @Override
    public void stop() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("Conexión a BD cerrada correctamente.");
        }
        super.stop();
    }
}
