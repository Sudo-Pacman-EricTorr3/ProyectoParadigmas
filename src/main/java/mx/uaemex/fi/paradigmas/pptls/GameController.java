package mx.uaemex.fi.paradigmas.pptls;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mx.uaemex.fi.paradigmas.pptls.model.JugadoresDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.model.RecordsDAOPsqlImp;
import mx.uaemex.fi.paradigmas.pptls.model.data.Juego;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;

import java.sql.Connection;
import java.util.Date;


public class GameController {



    @FXML
    private AnchorPane ApJuegoManos;
    @FXML
    private Label EleccionPc;

    @FXML
    private Button btnLagarto;

    @FXML
    private Button btnPiedra;

    @FXML
    private Button btnPpael;

    @FXML
    private Button btnSpock;

    @FXML
    private Button btnTijeras;

    @FXML
    private Label lblResultado;
    @FXML
    private ImageView imgJugador;
    @FXML
    private Label lblVictoriaJ;
    @FXML
    private Label lblVictoriasPC;
    @FXML
    private ImageView imgPC;
    @FXML
    private ImageView imgRecord;
    @FXML
    private Button Reinicio;

    @FXML
    private Label lblRecord;
    private int victoriaJugador=0;
    private int victoriaPc=0;
    private int victoriasConsecutivas=0;
    private int recordMasAlto=0;
    private RecordsDAOPsqlImp recordsDao;
    private JugadoresDAOPsqlImp jugadoresDao;
    private Jugador jugadorActual;
    private Juego juegoActual;
    private Connection conexion;

    Image imgPrs = new Image(getClass().getResource("/imagenes/PiedraRes.png").toExternalForm());//imagen de resultado de la pc para piedra
    Image imgTrs = new Image(getClass().getResource("/imagenes/TijerasRes.png").toExternalForm());//imagen de resultado de la pc para tijeras
    Image imgPPrs = new Image(getClass().getResource("/imagenes/PapelRes.png").toExternalForm());//imagen de resultado de la pc para papel
    Image imgLrs = new Image(getClass().getResource("/imagenes/LagartoRes.png").toExternalForm());//imagen de resultado de la pc para lagarto
    Image imgSrs = new Image(getClass().getResource("/imagenes/SpockRes.png").toExternalForm());//imagen de resultado de la pc spock
    Image imagenR = new Image(getClass().getResource("/imagenes/GatoRecord.png").toExternalForm());//imagen de resultado de usuario para piedra

    @FXML
    public void setConexion(Connection conexion){
        this.conexion=conexion;
        System.out.println("Conectada");
        inicializarDaos();
        initialize();
    }
    private void inicializarDaos(){
        recordsDao=new RecordsDAOPsqlImp();//inicializamos el DAO de records
        recordsDao.setConexion(this.conexion);
        jugadoresDao=new JugadoresDAOPsqlImp();
        jugadoresDao.setConexion(this.conexion);
    }
    private void initialize(){
        configurarJugadorActual();
        configurarJuegoActual();
        configurarRecord();

    }
    private void configurarJugadorActual(){
        //Temporal hasta tener controlador de login
        Jugador consulta=new Jugador();
        //esto es temporal en lo que me informo de como es la pantalla y contorlador de login

        consulta.setLogin("jugador1");

        //consulta.setLogin(login); //este es el que podre usar cuando sepa que poner en el parentesis(eso vendra de la pantalla login)
        java.util.ArrayList<Jugador> jugadores=jugadoresDao.consultar(consulta);
        if(jugadores!=null && !jugadores.isEmpty()){
            jugadorActual=jugadores.getFirst();}
    }
    private static int contadorDeJuegos=1;
    private int generarIdJuegoUnico(){
        long timestamp=System.currentTimeMillis()%10000;
        int id=(int)timestamp*1000+contadorDeJuegos;
        contadorDeJuegos++;
        return id;
    }
    private void configurarJuegoActual(){
        juegoActual =new Juego();
        int idJuego=generarIdJuegoUnico();
        juegoActual.setId(idJuego);

        System.out.println("Juego creado con ID="+ juegoActual.getId());

    }
    private void configurarRecord(){
        try{
            Record consulta =new Record();
            consulta.setJugador(jugadorActual);
            java.util.ArrayList<Record> records =recordsDao.consultar(consulta);
            if(records!=null && !records.isEmpty()){
                int MaxEncontrado=0;
                for(Record r:records){
                    if(r.getRecord()>MaxEncontrado){
                        MaxEncontrado=r.getRecord();
                    }
                }
                recordMasAlto=MaxEncontrado;

            }

        }catch(Exception e){
            System.out.println("Error cargando record :"+e);
        }



    }
    public void ActualizarLabelsV(){
        lblVictoriaJ.setText(String.valueOf(victoriaJugador));
        lblVictoriasPC.setText(String.valueOf(victoriaPc));

    }
    private void guardarNuevoRecord(){
        if(victoriasConsecutivas>recordMasAlto) {
            try {
                Record consultaExistente = new Record();
                consultaExistente.setJugador(jugadorActual);
                consultaExistente.setJuego(juegoActual);
                java.util.ArrayList<Record> recordsExistentes = recordsDao.consultar();
                if (recordsExistentes != null && !recordsExistentes.isEmpty()) {
                    Record Recordexistente = recordsExistentes.get(0);
                    if (victoriasConsecutivas > Recordexistente.getRecord()) {
                        Recordexistente.setRecord(victoriasConsecutivas);
                        recordsDao.actualizar(Recordexistente);
                        lblRecord.setText("NUEVO RECORD!!!");
                        imgRecord.setImage(imagenR);
                        System.out.println("Record Actualizado en la base ");
                    } else {
                        System.out.println("No hay Record :(");
                    }
                } else {
                    Record nuevoRecord = new Record();
                    nuevoRecord.setJugador(jugadorActual);
                    nuevoRecord.setJuego(juegoActual);
                    nuevoRecord.setRecord(victoriasConsecutivas);
                    nuevoRecord.setFecha(new Date());
                    recordsDao.insertar(nuevoRecord);
                    lblRecord.setText("NUEVO RECORD!!!");
                    imgRecord.setImage(imagenR);
                    System.out.println("Primer Record insertado correctamente");
                }
                recordMasAlto = victoriasConsecutivas;
            } catch (Exception e) {
                System.out.println("Error al guardarRecord" + e.getMessage());
            }

        }

    }
    public void PiedraEleccion(ActionEvent event){
        Image imgP = new Image(getClass().getResource("/imagenes/newPiedras.png").toExternalForm());//imagen de resultado de usuario para piedra
        imgJugador.setImage(imgP); //cambiamos imageview del usuario por imagen de piedra
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos piedra
        int pcJugada=(int)(Math.random()*5)+1;
        //determinar el ganador
        //1.Piedra,2.Tijera,3.Papel,4.Spock,5.Lagarto
        if(pcJugada==1){
            imgPC.setImage(imgPrs);//cambiamos imageview de pc por imagen de piedra
            EleccionPc.setText("PC escogio Piedra");
            lblResultado.setText("EMPATE!!, no hay ningun punto");
        }else if (pcJugada==2){
            imgPC.setImage(imgTrs);//cambiamos imageview de pc por imagen de tijera
            EleccionPc.setText("PC escogio Tijeras");
            lblResultado.setText("Ganaste,tu piedra aplasta sus tijeras ");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if(pcJugada==3){
            imgPC.setImage(imgPPrs);
            EleccionPc.setText("PC escogio Papel");
            lblResultado.setText("Perdiste,su papel envuelve tu piedra ");
            victoriaPc++;
        }else if(pcJugada==4){
            imgPC.setImage(imgSrs);
            EleccionPc.setText("PC escogio Spock");
            lblResultado.setText("Perdiste,Spock vaporiza tu piedra ");
            victoriaPc++;
        }else if(pcJugada==5){
            imgPC.setImage(imgLrs);
            EleccionPc.setText("PC escogio Lagarto");
            lblResultado.setText("Ganaste,tu piedra aplasta su lagarto");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }
        ActualizarLabelsV();
    }
    public void PapelEleccion(ActionEvent event){
        Image imgPp = new Image(getClass().getResource("/imagenes/newPapel.png").toExternalForm());//imagen de resultado de usuario para piedra
        imgJugador.setImage(imgPp);
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos Papel
        int pcJugada=(int)(Math.random()*5)+1;
        //determinar el ganador
        //1.Piedra,2.Tijera,3.Papel,4.Spock,5.Lagarto
        if(pcJugada==1){
            imgPC.setImage(imgPrs);
            EleccionPc.setText("PC escogio Piedra");
            lblResultado.setText("Ganaste,tu papel envuelve su piedra ");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if (pcJugada==2){
            imgPC.setImage(imgTrs);
            EleccionPc.setText("PC escogio Tijeras");
            lblResultado.setText("Perdiste,sus tijeras cortan tu papel");
            victoriaPc++;
        }else if(pcJugada==3){
            imgPC.setImage(imgPp);
            EleccionPc.setText("PC escogio Papel");
            lblResultado.setText("EMPATE!!, no hay ningun punto");
        }else if(pcJugada==4){
            imgPC.setImage(imgSrs);
            EleccionPc.setText("PC escogio Spock");
            lblResultado.setText("Ganaste,tu papel desautoriza Spock");
            victoriaJugador++;
            victoriasConsecutivas++;
        }else if(pcJugada==5){
            imgPC.setImage(imgLrs);
            EleccionPc.setText("PC escogio Lagarto");
            lblResultado.setText("Perdiste,su lagarto devora papel ");
            victoriaPc++;
        }
        ActualizarLabelsV();
    }
    public void TijerasEleccion(ActionEvent event){
        Image imgT = new Image(getClass().getResource("/imagenes/newTijeras.png").toExternalForm());//imagen de resultado de usuario para piedra
        imgJugador.setImage(imgT);
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos Tijeras
        int pcJugada=(int)(Math.random()*5)+1;
        //determinar el ganador
        //1.Piedra,2.Tijera,3.Papel,4.Smock,5.Lagarto
        if(pcJugada==1){
            imgPC.setImage(imgPrs);
            EleccionPc.setText("PC escogio Piedra");
            lblResultado.setText("Perdiste,su piedra aplsta tus tijeras ");
            victoriaPc++;
        }else if (pcJugada==2){
            imgPC.setImage(imgTrs);
            EleccionPc.setText("PC escogio Tijeras");
            lblResultado.setText("Empate!!!, no hay punto");
        }else if(pcJugada==3){
            imgPC.setImage(imgPPrs);
            EleccionPc.setText("PC escogio Papel");
            lblResultado.setText("Ganaste,tus tijeras cortan su papel");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if(pcJugada==4){
            imgPC.setImage(imgSrs);
            EleccionPc.setText("PC escogio Spock");
            lblResultado.setText("Perdiste ,Spock rompe tus tijeras ");
            victoriaPc++;
        }else if(pcJugada==5){
            imgPC.setImage(imgLrs);
            EleccionPc.setText("PC escogio Lagarto");
            lblResultado.setText("Ganaste,tus tijeras decapitan su lagarto");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }
        ActualizarLabelsV();
    }
    public void SpockEleccion(ActionEvent event){
        Image imgS = new Image(getClass().getResource("/imagenes/newSpock.jpg.png").toExternalForm());//imagen de resultado de usuario para piedra
        imgJugador.setImage(imgS);
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos Tijeras
        int pcJugada=(int)(Math.random()*5)+1;
        //determinar el ganador
        //1.Piedra,2.Tijera,3.Papel,4.Spock,5.Lagarto
        if(pcJugada==1){
            imgPC.setImage(imgPrs);
            EleccionPc.setText("PC escogio Piedra");
            lblResultado.setText("Ganaste,Spock vaporiza su piedra");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if (pcJugada==2){
            imgPC.setImage(imgTrs);
            EleccionPc.setText("PC escogio Tijeras");
            lblResultado.setText("Ganaste,Spock vaporiza su piedra ");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if(pcJugada==3){
            imgPC.setImage(imgPPrs);
            EleccionPc.setText("PC escogio Papel");
            lblResultado.setText("Perdiste,su papel desautoriza Spock");
            victoriaPc++;
        }else if(pcJugada==4){
            imgPC.setImage(imgSrs);
            EleccionPc.setText("PC escogio Spock");
            lblResultado.setText("EMPATE!!!,no hay punto ");
        }else if(pcJugada==5){
            imgPC.setImage(imgLrs);
            EleccionPc.setText("PC escogio Lagarto");
            lblResultado.setText("Perdiste,su legarto envenena a tu spock");
            victoriaPc++;
        }
        ActualizarLabelsV();
    }
    public void LagartoEleccion(ActionEvent event){
        Image imgL = new Image(getClass().getResource("/imagenes/newLagarto.png").toExternalForm());//imagen de resultado de usuario para piedra
        imgJugador.setImage(imgL);
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos Tijeras
        int pcJugada=(int)(Math.random()*5)+1;
        //determinar el ganador
        //1.Piedra,2.Tijera,3.Papel,4.Smock,5.Lagarto
        if(pcJugada==1){
            imgPC.setImage(imgPrs);
            EleccionPc.setText("PC escogio Piedra");
            lblResultado.setText("Perdiste,su piedra aplasta tu lagarto");
            victoriaPc++;
        }else if (pcJugada==2){
            imgPC.setImage(imgTrs);
            EleccionPc.setText("PC escogio Tijeras");
            lblResultado.setText("Perdiste,sus tijeras decapitan tu lagarto");
            victoriaPc++;
        }else if(pcJugada==3){
            imgPC.setImage(imgPPrs);
            EleccionPc.setText("PC escogio Papel");
            lblResultado.setText("Ganaste ,tu lagarto devora su papel");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if(pcJugada==4){
            imgPC.setImage(imgSrs);
            EleccionPc.setText("PC escogio Spock");
            lblResultado.setText("Ganaste ,tu lagarto envenena spock");
            victoriaJugador++;
            victoriasConsecutivas++;
            guardarNuevoRecord();
        }else if(pcJugada==5){
            imgPC.setImage(imgLrs);
            EleccionPc.setText("PC escogio Lagarto");
            lblResultado.setText("Empate!!!,no hay punto");
        }
        ActualizarLabelsV();
    }
    @FXML
    public void Reinicio(){
        victoriaJugador=0;
        victoriaPc=0;
        victoriasConsecutivas=0;
        lblRecord.setText("");
        imgRecord.setImage(null);
        ActualizarLabelsV();

    }
}

