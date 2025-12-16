package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mx.uaemex.fi.paradigmas.pptls.model.data.Juego;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresService;
import mx.uaemex.fi.paradigmas.pptls.service.JugadoresServiceLocal;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsServiceLocal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GameController {


    @FXML
    private Label Login;
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
    private Jugador jugadorActual;
    private Juego juegoActual;
    private Jugador jugador;
    private JugadoresService servicioLocal;
    private RecordsService servicioRecord;
    public void setServicioLocal(JugadoresService s) {
        this.servicioLocal = s;
    }
    public void setServicioRecord(RecordsService ServicioR){
        this.servicioRecord=ServicioR;
    };
public void setJugador(Jugador jugador){

    this.jugador=jugador;
    cargarRecordExistente();
}
    Image imgPrs = new Image(getClass().getResource("/imagenes/PiedraRes.png").toExternalForm());//imagen de resultado de la pc para piedra
    Image imgTrs = new Image(getClass().getResource("/imagenes/TijerasRes.png").toExternalForm());//imagen de resultado de la pc para tijeras
    Image imgPPrs = new Image(getClass().getResource("/imagenes/PapelRes.png").toExternalForm());//imagen de resultado de la pc para papel
    Image imgLrs = new Image(getClass().getResource("/imagenes/LagartoRes.png").toExternalForm());//imagen de resultado de la pc para lagarto
    Image imgSrs = new Image(getClass().getResource("/imagenes/SpockRes.png").toExternalForm());//imagen de resultado de la pc spock
    Image imagenR = new Image(getClass().getResource("/imagenes/GatoRecord.png").toExternalForm());//imagen de resultado de usuario para piedra
    public void ActualizarLabelsV(){
        lblVictoriaJ.setText(String.valueOf(victoriaJugador));
        lblVictoriasPC.setText(String.valueOf(victoriaPc));

    }
    private void guardarNuevoRecord(){
        if(victoriasConsecutivas<=recordMasAlto) return;
            try {
                ArrayList<Record> Records=servicioRecord.consultarRecords();
                Record recordExiste=null;
                for(Record r:Records){
                    if(r.getJugador()!=null&&
                            r.getJugador().getId()==jugador.getId()&&
                    r.getJuego()!=null&&
                    r.getJuego().getId()==1){
                        recordExiste=r;
                        break;

                    }

                }
                if(recordExiste==null){
                    Record nuevo=new Record();
                    nuevo.setJugador(jugador);
                    Juego juego =new Juego();
                    juego.setId(1);
                    nuevo.setJuego(juego);
                    nuevo.setFecha(new Date());
                    nuevo.setRecord(victoriasConsecutivas);
                    servicioRecord.guardarRecord(nuevo);
                    System.out.println("Record recien insesrtado");

                }else{
                    if(victoriasConsecutivas>recordExiste.getRecord()){
                        Record Actualizar =new Record();
                        Actualizar.setId(recordExiste.getId());
                        Actualizar.setJugador(jugador);
                        Actualizar.setJuego(recordExiste.getJuego());
                        Actualizar.setFecha(new Date());
                        Actualizar.setRecord(victoriasConsecutivas);
                        servicioRecord.actualizarRecord(Actualizar);
                        System.out.println("Nuevo record");
                    }else{
                        System.out.println("Record NO superado :(");
                        return;
                    }
                }

lblRecord.setText("NUEVOOO RECORD");
                imgRecord.setImage(imagenR);
                recordMasAlto=victoriasConsecutivas;
            } catch (Exception e) {
                System.out.println("Error al guardarRecord" + e.getMessage());
            }

        }
        public void cargarRecordExistente(){
        try{
            ArrayList<Record> todosRecord=servicioRecord.consultarRecords();
            recordMasAlto=0;
            for(Record r :todosRecord){
               if (r.getJugador()!=null&&
               r.getJugador().getId()==jugador.getId()&&
               r.getJuego()!=null&&
               r.getJuego().getId()==1){
                   if(r.getRecord()>recordMasAlto)
recordMasAlto=r.getRecord();
               }
            }

        }catch(Exception e){

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
            guardarNuevoRecord();
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
        //1.Piedra,2.Tijera,3.Papel,4.Spock,5.Lagarto
        if(pcJugada==1){
            imgPC.setImage(imgPrs);
            EleccionPc.setText("PC escogio Piedra");
            lblResultado.setText("Perdiste,su piedra aplasta tus tijeras ");
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
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos Spock
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
            lblResultado.setText("Ganaste,Spock rompe sus tijeras ");
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
            lblResultado.setText("Perdiste,su lagarto envenena a tu spock");
            victoriaPc++;
        }
        ActualizarLabelsV();
    }
    public void LagartoEleccion(ActionEvent event){
        Image imgL = new Image(getClass().getResource("/imagenes/newLagarto.png").toExternalForm());//imagen de resultado de usuario para piedra
        imgJugador.setImage(imgL);
        //calculamos tiro de la pc,ya sabemos que nosotros escogimos Lagarto
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
    //Agregar boton para regresar al menu o terminar partida
}

