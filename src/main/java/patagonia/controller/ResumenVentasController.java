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
    @FXML private Label lblCantidad;
    
    @FXML private ToggleButton btnCatamaran;
    @FXML private ToggleButton btnFerry;
    @FXML private ToggleButton btnWellboat;
    @FXML private ToggleGroup grupoFiltro; 
    
    @FXML private javafx.scene.control.Button btnSalir;

    @FXML
    public void initialize() {
        if (grupoFiltro == null) grupoFiltro = new ToggleGroup();
        btnCatamaran.setToggleGroup(grupoFiltro);
        btnFerry.setToggleGroup(grupoFiltro);
        btnWellboat.setToggleGroup(grupoFiltro);

        calcularEstadisticas();
    }

    @FXML
    void seleccionarBarco(ActionEvent event) {
        calcularEstadisticas();
    }

    private void calcularEstadisticas() {
        int totalDinero = 0;
        int totalCantidad = 0;
        Class<?> claseFiltro = null;
        if (btnCatamaran.isSelected()) claseFiltro = CatamaranLiviano.class;
        else if (btnFerry.isSelected()) claseFiltro = FerryMediano.class;
        else if (btnWellboat.isSelected()) claseFiltro = WellboatGranCapacidad.class;

        for (Viaje v : Main.listaViajes) {
            if (claseFiltro != null && !claseFiltro.isInstance(v.getEmbarcacionAsignada())) {
                continue;
            }

            for (Pasaje p : v.getListaPasajes()) {
                totalDinero += p.getPreciofinal();
                totalCantidad++;
            }
        }
        if (claseFiltro == null) {
            for (Encomienda e : Main.listaEncomiendas) {
                totalDinero += e.getPrecioFinal();
                totalCantidad++;
            }
        }
        lblMonto.setText("$ " + totalDinero);
        lblCantidad.setText(String.valueOf(totalCantidad));
    }

    @FXML
    void volverInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuGerente.fxml"));
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}