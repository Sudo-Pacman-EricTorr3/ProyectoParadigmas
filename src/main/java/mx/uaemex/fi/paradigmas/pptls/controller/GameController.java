package mx.uaemex.fi.paradigmas.pptls.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mx.uaemex.fi.paradigmas.pptls.model.data.Juego;
import mx.uaemex.fi.paradigmas.pptls.model.data.Jugador;
import mx.uaemex.fi.paradigmas.pptls.model.data.Record;
import mx.uaemex.fi.paradigmas.pptls.service.RecordsService;

import java.net.URL;
import java.util.ArrayList;

public class GameController {

    @FXML private AnchorPane ApJuegoManos;
    @FXML private ImageView bgImage;

    @FXML private Label lblUsuario;
    @FXML private Label lblRachaActual;
    @FXML private Label lblRecordMaximo;

    @FXML private ImageView imgJugador;
    @FXML private Label etiquetaEleccionJugador;

    @FXML private ImageView imgPC;
    @FXML private Label etiquetaEleccionPC;
    @FXML private Label EleccionPc;

    @FXML private Label lblResultado;
    @FXML private Label lblDetalleResultado;
    @FXML private ImageView imgRecord;

    private Jugador jugadorActual;
    private RecordsService servicioRecords;
    private Stage stageMenu;
    private Record ultimoRecord;

    private int rachaActual = 0;
    private final int ID_JUEGO = 1;

    private final int PIEDRA = 1;
    private final int TIJERAS = 2;
    private final int PAPEL = 3;
    private final int SPOCK = 4;
    private final int LAGARTO = 5;

    public void setDatos(Jugador jugador, RecordsService servicio, Stage menuStage) {
        this.jugadorActual = jugador;
        this.servicioRecords = servicio;
        this.stageMenu = menuStage;

        if (lblUsuario != null) lblUsuario.setText("Jugador: " + jugador.getLogin());

        ajustarFondo();
        cargarUltimoRecord();
    }

    private void ajustarFondo() {
        if (bgImage != null && ApJuegoManos != null) {
            bgImage.fitWidthProperty().bind(ApJuegoManos.widthProperty());
            bgImage.fitHeightProperty().bind(ApJuegoManos.heightProperty());
        }
    }

    private void cargarUltimoRecord() {
        try {
            Record filtro = new Record();
            filtro.setJugador(jugadorActual);

            Juego j = new Juego(); j.setId(ID_JUEGO);
            filtro.setJuego(j);

            ArrayList<Record> lista = servicioRecords.consultar(filtro);

            if (lista != null && !lista.isEmpty()) {
                this.ultimoRecord = lista.get(0);
                lblRecordMaximo.setText("Récord a vencer: " + this.ultimoRecord.getRecord());
            } else {
                this.ultimoRecord = new Record();
                this.ultimoRecord.setRecord(0);
                lblRecordMaximo.setText("Sin récord previo");
            }
        } catch (Exception e) {
            System.out.println("Error cargando récord: " + e.getMessage());
        }
    }

    private void jugar(int eleccionJugador, String nombreImagen) {
        try {
            imgRecord.setImage(null);

            ponerImagen(imgJugador, nombreImagen);
            etiquetaEleccionJugador.setText(obtenerNombre(eleccionJugador));

            int eleccionPC = (int)(Math.random() * 5) + 1;
            mostrarPC(eleccionPC);

            if (eleccionJugador == eleccionPC) {
                lblResultado.setText("¡Empate!");
                lblDetalleResultado.setText("Ambos eligieron " + obtenerNombre(eleccionPC));
            } else if (ganaJugador(eleccionJugador, eleccionPC)) {
                rachaActual++;
                lblResultado.setText("¡Ganaste!");
                lblDetalleResultado.setText(obtenerMensajeDetalle(eleccionJugador, eleccionPC));

                if (rachaActual > ultimoRecord.getRecord()) {
                    insertarNuevoRecord();
                }
            } else {
                rachaActual = 0;
                lblResultado.setText("Perdiste...");
                lblDetalleResultado.setText(obtenerMensajeDetalle(eleccionPC, eleccionJugador));
            }

            lblRachaActual.setText("Racha Actual: " + rachaActual);

        } catch (Exception e) {
            System.out.println("Error al jugar: " + e.getMessage());
        }
    }

    private void insertarNuevoRecord() {
        try {
            Record nuevo = new Record();
            nuevo.setJugador(jugadorActual);
            Juego j = new Juego();
            j.setId(ID_JUEGO);
            nuevo.setJuego(j);
            nuevo.setRecord(rachaActual);

            servicioRecords.insertar(nuevo);

            this.ultimoRecord = nuevo;

            lblRecordMaximo.setText("¡NUEVO RÉCORD: " + rachaActual + "!");
            ponerImagen(imgRecord, "GatoRecord.png");

        } catch (Exception e) {
            System.out.println("Error guardando récord: " + e.getMessage());
        }
    }

    // --- REGLAS Y MENSAJES (Basado en tu imagen) ---
    private boolean ganaJugador(int j, int pc) {
        return (j == PIEDRA && (pc == LAGARTO || pc == TIJERAS)) ||
                (j == TIJERAS && (pc == PAPEL || pc == LAGARTO)) ||
                (j == PAPEL && (pc == PIEDRA || pc == SPOCK)) ||
                (j == SPOCK && (pc == TIJERAS || pc == PIEDRA)) ||
                (j == LAGARTO && (pc == SPOCK || pc == PAPEL));
    }

    private String obtenerMensajeDetalle(int ganador, int perdedor) {
        // Lógica exacta de la imagen que subiste
        if (ganador == TIJERAS && perdedor == PAPEL) return "Tijeras cortan papel";
        if (ganador == PAPEL && perdedor == PIEDRA) return "Papel tapa a piedra";
        if (ganador == PIEDRA && perdedor == LAGARTO) return "Piedra aplasta a lagarto";
        if (ganador == LAGARTO && perdedor == SPOCK) return "Lagarto envenena a Spock";
        if (ganador == SPOCK && perdedor == TIJERAS) return "Spock rompe tijeras";
        if (ganador == TIJERAS && perdedor == LAGARTO) return "Tijeras decapitan lagarto";
        if (ganador == LAGARTO && perdedor == PAPEL) return "Lagarto devora papel";
        if (ganador == PAPEL && perdedor == SPOCK) return "Papel desautoriza Spock";
        if (ganador == SPOCK && perdedor == PIEDRA) return "Spock vaporiza piedra";
        if (ganador == PIEDRA && perdedor == TIJERAS) return "Piedra aplasta a tijeras";

        return "Gana " + obtenerNombre(ganador);
    }

    private String obtenerNombre(int op) {
        switch(op) {
            case PIEDRA: return "Piedra";
            case TIJERAS: return "Tijeras";
            case PAPEL: return "Papel";
            case SPOCK: return "Spock";
            case LAGARTO: return "Lagarto";
            default: return "";
        }
    }

    private void mostrarPC(int pc) {
        String[] imgs = {"", "PiedraRes.png", "TijerasRes.png", "PapelRes.png", "SpockRes.png", "LagartoRes.png"};
        EleccionPc.setText("PC eligió: " + obtenerNombre(pc));
        etiquetaEleccionPC.setText(obtenerNombre(pc));
        ponerImagen(imgPC, imgs[pc]);
    }

    private void ponerImagen(ImageView iv, String nombre) {
        try {
            String ruta = "/mx/uaemex/fi/paradigmas/pptls/imagenes/" + nombre;
            URL url = getClass().getResource(ruta);

            if (url != null) {
                iv.setImage(new Image(url.toExternalForm()));
            } else {
                System.err.println("NO SE ENCUENTRA: " + nombre);
            }
        } catch (Exception e) {
            System.err.println("Excepción img: " + e.getMessage());
        }
    }

    @FXML public void PiedraEleccion(ActionEvent e)  { jugar(PIEDRA, "newPiedras.png"); }
    @FXML public void TijerasEleccion(ActionEvent e) { jugar(TIJERAS, "newTijeras.png"); }
    @FXML public void PapelEleccion(ActionEvent e)   { jugar(PAPEL, "newPapel.png"); }
    @FXML public void SpockEleccion(ActionEvent e)   { jugar(SPOCK, "newSpock.jpg.png"); } // Revisa si es .png o .jpg
    @FXML public void LagartoEleccion(ActionEvent e) { jugar(LAGARTO, "newLagarto.png"); }

    @FXML
    public void volverAlMenu(ActionEvent event) {
        Stage stageActual = (Stage) lblUsuario.getScene().getWindow();
        stageActual.close();
        if (stageMenu != null) stageMenu.show();
    }
}