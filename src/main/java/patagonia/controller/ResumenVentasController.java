package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import patagonia.Main;
import patagonia.model.*;
import java.io.IOException;

public class ResumenVentasController {

    @FXML private Label lblMonto;
    @FXML private Label lblMonto1;
    
    @FXML private ToggleButton btnCatamaran;
    @FXML private ToggleButton btnFerry;
    @FXML private ToggleButton btnWellboat;

    @FXML private ToggleGroup grupoBarcos; 
    @FXML private ToggleGroup grupoBarcos1;
    @FXML private ToggleGroup grupoBarcos2;
    
    @FXML private Label lblDestino; 
    @FXML private Label lblDestino1; 
    @FXML private Label lblDestino2;
    
    @FXML private javafx.scene.control.Button btnSalir;

    @FXML
    public void initialize() {
        calcularEstadisticas();
    }

    @FXML
    void seleccionarBarco(ActionEvent event) {
        calcularEstadisticas();
    }

    private void calcularEstadisticas() {
        int totalVentas = 0;
        int cantidadPasajes = 0;

        Class<? extends Embarcacion> tipoFiltro = null;
        
        if (btnCatamaran != null && btnCatamaran.isSelected()) tipoFiltro = CatamaranLiviano.class;
        else if (btnFerry != null && btnFerry.isSelected()) tipoFiltro = FerryMediano.class;
        else if (btnWellboat != null && btnWellboat.isSelected()) tipoFiltro = WellboatGranCapacidad.class;

        for (Viaje v : Main.listaViajes) {
            if (tipoFiltro != null && !tipoFiltro.isInstance(v.getEmbarcacionAsignada())) {
                continue;
            }

            for (Pasaje p : v.getListaPasajes()) {
                totalVentas += p.getPreciofinal();
                cantidadPasajes++;
            }
        }

        if (lblMonto != null) lblMonto.setText("$ " + totalVentas);
        if (lblMonto1 != null) lblMonto1.setText(String.valueOf(cantidadPasajes));
        
        if (lblDestino != null) lblDestino.setText("Filtrado");
    }

    @FXML
    void volverInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuGerente.fxml"));
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Patagonia Wellboat - Menú Gerente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}