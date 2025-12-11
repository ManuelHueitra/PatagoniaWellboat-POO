package patagonia.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import patagonia.Main;
import patagonia.model.*;
import java.io.IOException;
import java.time.LocalDate;

public class ProgramarViajeController {

    @FXML private ComboBox<Destino> cmbDestino;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtHora;
    
    @FXML private ToggleButton btnCatamaran;
    @FXML private ToggleButton btnFerry;
    @FXML private ToggleButton btnWellboat;
    @FXML private ToggleGroup grupoBarcos;
    
    @FXML private Button btnSalir;

    @FXML
    public void initialize() {
        ObservableList<Destino> misDestinos = FXCollections.observableArrayList(Main.listaDestinos);
        cmbDestino.setItems(misDestinos);
        
        txtHora.setText("08:00"); 
    }

    @FXML
    void realizarProgramacion(ActionEvent event) {
        if (dateFecha.getValue() == null || cmbDestino.getValue() == null || grupoBarcos.getSelectedToggle() == null) {
            mostrarAlerta("Faltan datos", "Seleccione fecha, destino y tipo de barco.");
            return;
        }

        LocalDate fecha = dateFecha.getValue();
        Destino destino = cmbDestino.getValue();
        String hora = "09:00"; 

        Embarcacion barcoAsignado = buscarBarcoDisponible();
        
        if (barcoAsignado == null) {
            mostrarAlerta("Sin Flota", "No hay barcos de este tipo disponibles en el sistema.");
            return;
        }

        Viaje nuevoViaje = new Viaje(fecha, hora, barcoAsignado, destino);
        
        Main.listaViajes.add(nuevoViaje);
        Main.guardarCambios();

        mostrarAlerta("Éxito", "Viaje programado correctamente: " + barcoAsignado.getPatente());
        volverMenu(event);
    }

    private Embarcacion buscarBarcoDisponible() {
        for (Embarcacion e : Main.listaEmbarcaciones) {
            if (btnCatamaran.isSelected() && e instanceof CatamaranLiviano) return e;
            if (btnFerry.isSelected() && e instanceof FerryMediano) return e;
            if (btnWellboat.isSelected() && e instanceof WellboatGranCapacidad) return e;
        }
        if (btnCatamaran.isSelected()) return new CatamaranLiviano("CAT-NEW");
        if (btnFerry.isSelected()) return new FerryMediano("FER-NEW");
        return new WellboatGranCapacidad("WELL-NEW");
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuGerente.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    @FXML
    void seleccionarBarco(ActionEvent event) {
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}