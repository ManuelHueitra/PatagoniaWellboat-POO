package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import patagonia.model.Pasaje;
import patagonia.model.Viaje;
import java.util.ArrayList;
import java.util.List;

public class BarcoWellboatController {

    @FXML private GridPane gridAsientos;
    @FXML private GridPane gridCamarotes;

    private String seleccionActual = "";
    private Button botonAnterior = null;
    
    private VentaPasajesController controladorPadre;
    private Viaje viajeActual;

    // Colores
    private final String COLOR_NORMAL = "#58CCA3";
    private final String COLOR_SELECCIONADO = "#00695c"; 
    private final String COLOR_OCUPADO = "#d32f2f"; 

    public void inicializarDatos(Viaje viaje, VentaPasajesController controller) {
        this.viajeActual = viaje;
        this.controladorPadre = controller;

        //buscamos que asientos ya están vendidos en este viaje
        List<String> asientosOcupados = new ArrayList<>();
        if (viaje != null) {
            for (Pasaje p : viaje.getListaPasajes()) {
                asientosOcupados.add(p.getNumeroAsiento());
            }
        }

        //bloqueamos visualmente esos botones
        bloquearBotones(gridAsientos, asientosOcupados);
        bloquearBotones(gridCamarotes, asientosOcupados);
    }

    private void bloquearBotones(GridPane grid, List<String> ocupados) {
        for (Node node : grid.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (ocupados.contains(btn.getText())) {
                    btn.setDisable(true);
                    btn.setStyle("-fx-background-color: " + COLOR_OCUPADO + "; -fx-opacity: 0.7; -fx-text-fill: white; -fx-background-radius: 50; -fx-border-radius: 50;");
                }
            }
        }
    }

    @FXML
    public void seleccionarUbicacion(ActionEvent event) {
        Button botonNuevo = (Button) event.getSource();
        
        if (botonNuevo.isDisabled()) return;

        String texto = botonNuevo.getText(); // Ej: "A5", "C10"

        // limpiar seleccion anterior
        if (botonAnterior != null) {
            String textoAnterior = botonAnterior.getText();
            String estiloBase = textoAnterior.startsWith("A") ? 
                "-fx-background-radius: 50; -fx-border-radius: 50;" : 
                "-fx-background-radius: 10; -fx-border-radius: 10;";
            
            botonAnterior.setStyle(estiloBase + " -fx-background-color: " + COLOR_NORMAL + "; -fx-border-color: black; -fx-border-width: 2px;");
        }

        String estiloBase = texto.startsWith("A") ? 
            "-fx-background-radius: 50; -fx-border-radius: 50;" : 
            "-fx-background-radius: 10; -fx-border-radius: 10;";
        
        botonNuevo.setStyle(estiloBase + " -fx-background-color: " + COLOR_SELECCIONADO + "; -fx-text-fill: white; -fx-border-color: black; -fx-border-width: 2px;");

        seleccionActual = texto;
        botonAnterior = botonNuevo;
        
        System.out.println("Seleccionado: " + seleccionActual);
    }

    @FXML
    public void registrarEncomienda(ActionEvent event) {
        if (seleccionActual.equals("")) {
            System.out.println("Debe seleccionar un asiento.");
            return;
        }

        if (controladorPadre != null) {
            controladorPadre.recibirAsientoSeleccionado(seleccionActual);
        }

        cerrarVentana(event);
    }

    @FXML
    public void volverMenu(ActionEvent event) {
        cerrarVentana(event);
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}