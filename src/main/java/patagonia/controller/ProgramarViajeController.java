package patagonia.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import patagonia.Main;
import patagonia.model.*;
import java.io.IOException;
import java.time.LocalDate;

public class ProgramarViajeController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox<Destino> cmbDestino;
    
    @FXML private ToggleButton btnCatamaran;
    @FXML private ToggleButton btnFerry;
    @FXML private ToggleButton btnWellboat;
    @FXML private ToggleGroup grupoBarcos;
    
    @FXML private TextField txtAsientoElegido;
    @FXML private TextField txtTotal;

    private String asientoSeleccionado = null;
    private Viaje viajeTemporal = null;

    @FXML
    public void initialize() {
        cmbDestino.setItems(FXCollections.observableArrayList(Main.listaDestinos));
    
        if(txtTotal != null) txtTotal.setText("$0 (EMERGENCIA)");
        grupoBarcos.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            asientoSeleccionado = null;
            if(txtAsientoElegido != null) txtAsientoElegido.setText("---");
        });
    }

    @FXML
    void abrirMapaAsientos(ActionEvent event) {
        if (grupoBarcos.getSelectedToggle() == null) {
            mostrarAlerta("Error", "Seleccione un tipo de barco primero.");
            return;
        }
        try {
            String fxmlPath = "";
            Embarcacion barcoDummy;

            if (btnCatamaran.isSelected()) {
                fxmlPath = "/patagonia/view/Barco_Catamaran.fxml";
                barcoDummy = new CatamaranLiviano("CAT-EMERGENCIA");
            } else if (btnFerry.isSelected()) {
                fxmlPath = "/patagonia/view/Barco_Ferry.fxml";
                barcoDummy = new FerryMediano("FER-EMERGENCIA");
            } else {
                fxmlPath = "/patagonia/view/Barco_Wellboat.fxml";
                barcoDummy = new WellboatGranCapacidad("WEL-EMERGENCIA");
            }
            if (dateFecha.getValue() == null || cmbDestino.getValue() == null) {
                viajeTemporal = new Viaje(LocalDate.now(), "00:00", barcoDummy, Main.listaDestinos.get(0));
            } else {
                viajeTemporal = new Viaje(dateFecha.getValue(), "00:00", barcoDummy, cmbDestino.getValue());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            asientoSeleccionado = "A1 (Auto)";
            txtAsientoElegido.setText(asientoSeleccionado);
            mostrarAlerta("Asignación", "Se ha asignado el asiento prioritario A1 para esta emergencia.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void realizarProgramacion(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || txtRut.getText().isEmpty() || 
            dateFecha.getValue() == null || cmbDestino.getValue() == null || 
            grupoBarcos.getSelectedToggle() == null) {
            mostrarAlerta("Faltan Datos", "Complete todos los campos del beneficiario y viaje.");
            return;
        }
        Embarcacion barco = crearBarcoEmergencia();
        
        Viaje viajeEmergencia = new Viaje(dateFecha.getValue(), "URGENTE", barco, cmbDestino.getValue());
        
        Cliente beneficiario = new Cliente(txtNombre.getText(), txtRut.getText(), 0);
        String asiento = (asientoSeleccionado != null) ? asientoSeleccionado : "A1";
        
        Pasaje pasajeGratis = new Pasaje(asiento, 0, beneficiario);
        viajeEmergencia.agregarPasaje(pasajeGratis);
        Main.listaViajes.add(viajeEmergencia);
        Main.guardarCambios();

        mostrarAlerta("Emergencia Programada", "Se ha generado el viaje y el ticket para: " + txtNombre.getText());
        volverMenu(event);
    }

    private Embarcacion crearBarcoEmergencia() {
        String patente = "EMERG-" + System.currentTimeMillis() % 1000;
        if (btnCatamaran.isSelected()) return new CatamaranLiviano(patente);
        if (btnFerry.isSelected()) return new FerryMediano(patente);
        return new WellboatGranCapacidad(patente);
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuGerente.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}