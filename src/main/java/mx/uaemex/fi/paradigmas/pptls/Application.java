package mx.uaemex.fi.paradigmas.pptls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.controller.LoginController;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresServiceLocal;
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
        LoginController LoginCtrl;
        String url = "jdbc:postgresql://database-1.cnsiwgwsie1g.us-east-2.rds.amazonaws.com/pptls?user=postgres&password=Admin-AWS-123";

        JugadoresServiceLocal servicioLocal = new JugadoresServiceLocal();
        JugadoresServiceLocal servicioOnline = new JugadoresServiceLocal();


        //instanciación de servicios de records
        //modo local
        //no necesita DAO ni Conexión, usa Memoria RAM  o sea el ArrayList
        RecordsServiceLocal servicioRecordsLocal = new RecordsServiceLocal();

        //para el modo online pero aqui lo dejo pendiente para a conexion a el servicio de Gael o la que se
        // vaya a base de datos que se vaya a usar
        RecordsServiceOnline servicioRecordsOnline = new RecordsServiceOnline();
        try {
            conn = DriverManager.getConnection(url);

            JugadoresDAOPsqlImp dao = new JugadoresDAOPsqlImp();
            dao.setConexion(conn);

            servicioLocal.setDao(dao);
            servicioOnline.setDao(dao);

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

        LoginCtrl = fxmlLoader.getController();
        LoginCtrl.setServicioLocal(servicioLocal);
        LoginCtrl.setServicioOnline(servicioOnline);
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
