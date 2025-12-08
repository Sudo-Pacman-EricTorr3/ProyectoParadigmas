package mx.uaemex.fi.paradigmas.pptls;

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

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        LoginController Loginctrl;
        String url = "jdbc:postgresql://database-1.cnsiwgwsie1g.us-east-2.rds.amazonaws.com/pptls?user=postgres&password=Admin-AWS-123";

        Connection conn = DriverManager.getConnection(url);
        JugadoresServiceLocal servicioLocal = new JugadoresServiceLocal();
        JugadoresDAOPsqlImp dao = new JugadoresDAOPsqlImp();
        dao.setConexion(conn);
        servicioLocal.setDao(dao);

        JugadoresServiceLocal servicioOnline = new JugadoresServiceLocal();
        servicioOnline.setDao(dao);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Iniciar Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Loginctrl = fxmlLoader.getController();
        Loginctrl.setServicioLocal(servicioLocal);
        Loginctrl.setServicioOnline(servicioOnline);
    }
}
