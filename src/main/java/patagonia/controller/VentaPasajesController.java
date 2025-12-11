package patagonia.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class VentaPasajesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private ComboBox<Destino> cmbDestino;
    @FXML private TextField txtTotal;
    @FXML private DatePicker dateFecha;
    
    @FXML private ToggleButton btnCatamaran;
    @FXML private ToggleButton btnFerry;
    @FXML private ToggleButton btnWellboat;
    @FXML private ToggleGroup grupoBarcos;
    @FXML private ToggleButton btnSi;
    @FXML private ToggleButton btnNo;
    @FXML private ToggleGroup grupoDescuento;

    @FXML private TextField txtAsientoElegido; 

    private int precioCalculado = 0;
    private String asientoSeleccionado = null;

    @FXML
    public void initialize() {
        ObservableList<Destino> misDestinos = FXCollections.observableArrayList(Main.listaDestinos);
        cmbDestino.setItems(misDestinos);
        
        // Calcular precio al cambiar destino
        cmbDestino.setOnAction(e -> actualizarTotal());
        
        // Limpiar asiento si cambia la fecha
        dateFecha.valueProperty().addListener((obs, oldVal, newVal) -> {
            asientoSeleccionado = null;
            if(txtAsientoElegido != null) txtAsientoElegido.setText("---");
        });
    }
    private void actualizarTotal() {
        if (cmbDestino.getValue() == null) {
            txtTotal.setText("$ 0");
            return;
        }
        int precioBase = cmbDestino.getValue().getPrecioPasaje();
        int recargo = 0;
        if (btnFerry.isSelected()) recargo = 5000;
        else if (btnWellboat.isSelected()) recargo = 10000;
        
        int subtotal = precioBase + recargo;

        // 3. Aplicar Descuento
        if (btnSi.isSelected()) {
            int descuento = (int) (subtotal * 0.10);
            subtotal = subtotal - descuento;
        }

        this.precioCalculado = subtotal;
        txtTotal.setText("$ " + precioCalculado);
    }

    @FXML
    void seleccionarBarco(ActionEvent event) {
        actualizarTotal();
    }
    
    @FXML
    void aplicarDescuento(ActionEvent event) {
        actualizarTotal();
    }

    // Método llamado desde el mapa de asientos
    public void recibirAsientoSeleccionado(String asiento) {
        this.asientoSeleccionado = asiento;
        
        if (txtAsientoElegido != null) {
            txtAsientoElegido.setText(asiento);
        }
        actualizarTotal();
        
        mostrarAlerta("Asiento Confirmado", "Has elegido: " + asiento + "\nTotal a pagar: $" + precioCalculado);
    }

    @FXML
    void abrirMapaAsientos(ActionEvent event) {
        if (dateFecha.getValue() == null || cmbDestino.getValue() == null) {
            mostrarAlerta("Atención", "Selecciona Fecha y Destino antes.");
            return;
        }
        if (grupoBarcos.getSelectedToggle() == null) {
            mostrarAlerta("Atención", "Debes seleccionar un tipo de Barco.");
            return;
        }

        try {
            String rutaFxml = "";
            if (btnCatamaran.isSelected()) rutaFxml = "/patagonia/view/Barco_Catamaran.fxml";
            else if (btnFerry.isSelected()) rutaFxml = "/patagonia/view/Barco_Ferry.fxml";
            else if (btnWellboat.isSelected()) rutaFxml = "/patagonia/view/Barco_Wellboat.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            BarcoWellboatController controllerBarco = loader.getController();
            Viaje viajeActual = buscarViajeActual();
            controllerBarco.inicializarDatos(viajeActual, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el mapa de asientos.");
        }
    }

    @FXML
    void realizarVenta(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || txtRut.getText().isEmpty() || 
            cmbDestino.getValue() == null || grupoBarcos.getSelectedToggle() == null || 
            dateFecha.getValue() == null || asientoSeleccionado == null) {
            mostrarAlerta("Datos incompletos", "Verifique todos los campos y seleccione un asiento.");
            return;
        }

        // Buscar o crear viaje
        Viaje viaje = buscarViajeActual();
        if (viaje == null) {
            Embarcacion barco = buscarBarcoDisponible();
            if (barco == null) {
                mostrarAlerta("Error", "No hay barcos disponibles.");
                return;
            }
            viaje = new Viaje(dateFecha.getValue(), "10:00", barco, cmbDestino.getValue());
            Main.listaViajes.add(viaje);
        }

        Cliente cliente = new Cliente(txtNombre.getText(), txtRut.getText(), 0); 
        Pasaje pasaje = new Pasaje(asientoSeleccionado, precioCalculado, cliente); 

        if (viaje.agregarPasaje(pasaje)) {
            Main.guardarCambios();
            irPantallaFinalizacion();
        } else {
            mostrarAlerta("Error", "No se pudo completar la venta.");
        }
    }

    private Viaje buscarViajeActual() {
        LocalDate fecha = dateFecha.getValue();
        Destino destino = cmbDestino.getValue();
        if (fecha == null || destino == null) return null;

        for (Viaje v : Main.listaViajes) {
            if (v.getFecha().equals(fecha) && v.getDestino().getNombre().equals(destino.getNombre()) && 
                validarTipoBarco(v.getEmbarcacionAsignada())) {
                return v;
            }
        }
        return null;
    }

    private boolean validarTipoBarco(Embarcacion e) {
        if (btnCatamaran.isSelected() && e instanceof CatamaranLiviano) return true;
        if (btnFerry.isSelected() && e instanceof FerryMediano) return true;
        if (btnWellboat.isSelected() && e instanceof WellboatGranCapacidad) return true;
        return false;
    }

    private Embarcacion buscarBarcoDisponible() {
        for (Embarcacion e : Main.listaEmbarcaciones) {
            if (validarTipoBarco(e)) return e;
        }
        // Crear uno temporal si no hay (para que no te bloquee en pruebas)
        if (btnCatamaran.isSelected()) return new CatamaranLiviano("CAT-AUTO");
        if (btnFerry.isSelected()) return new FerryMediano("FER-AUTO");
        return new WellboatGranCapacidad("WELL-AUTO");
    }

    private void irPantallaFinalizacion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/Finalizacion.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuAsistente.fxml"));
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