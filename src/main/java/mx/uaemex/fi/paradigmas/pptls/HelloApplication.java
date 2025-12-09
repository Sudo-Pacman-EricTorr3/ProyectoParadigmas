package mx.uaemex.fi.paradigmas.pptls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private Connection conexion;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        String url="jdbc:postgresql://database-1.cnsiwgwsie1g.us-east-2.rds.amazonaws.com/pptls?user=postgres&password=Admin-AWS-123";
        conexion= DriverManager.getConnection(url);
        System.out.println("conexion establecida ");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProyectoParadigmas.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 740, 610);
        GameController controlador= fxmlLoader.getController();
        stage.setTitle("Pantalla Juego");
        stage.setScene(scene);
        stage.show();
        if(controlador!=null){
            controlador.setConexion(conexion);
            System.out.println("conexion en controlador");
        }else{
            System.out.println("No se obtuvo el controlador");
        }


    }
}
