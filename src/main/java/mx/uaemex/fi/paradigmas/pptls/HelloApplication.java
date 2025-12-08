package mx.uaemex.fi.paradigmas.pptls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.controller.LoginController;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresServiceLocal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        LoginController ctrl;
        String url = "jdbc:postgresql://database-1.cnsiwgwsie1g.us-east-2.rds.amazonaws.com/pptls?user=postgres&password=Admin-AWS-123";

        Connection conn = DriverManager.getConnection(url);
        JugadoresServiceLocal servicioLocal = new JugadoresServiceLocal();

        JugadoresDAOPsqlImp dao = new JugadoresDAOPsqlImp();

        dao.setConexion(conn);
        servicioLocal.setDao(dao);

        JugadoresService servicio = servicioLocal;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 450);
        stage.setTitle("Iniciar Sesión");
        stage.setScene(scene);
        stage.show();

        ctrl = fxmlLoader.getController();
        ctrl.setServicio(servicio);
    }
}
