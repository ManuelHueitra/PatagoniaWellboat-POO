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
    @FXML private TextField txtTarifa;
    @FXML private TextField txtTotal;
    @FXML private DatePicker dateFecha;
    
    @FXML private ToggleButton btnCatamaran;
    @FXML private ToggleButton btnFerry;
    @FXML private ToggleButton btnWellboat;
    @FXML private ToggleGroup grupoBarcos;

    @FXML private TextField txtAsientoElegido; 

    private int precioCalculado = 0;
    private String asientoSeleccionado = null;

    @FXML
    public void initialize() {
        ObservableList<Destino> misDestinos = FXCollections.observableArrayList(Main.listaDestinos);
        cmbDestino.setItems(misDestinos);
        cmbDestino.setOnAction(e -> calcularTotal());
    }

    @FXML
    void abrirMapaAsientos(ActionEvent event) {
        if (dateFecha.getValue() == null || cmbDestino.getValue() == null) {
            mostrarAlerta("Atención", "Selecciona Fecha y Destino antes de ver los asientos.");
            return;
        }
        if (grupoBarcos.getSelectedToggle() == null) {
            mostrarAlerta("Atención", "Debes seleccionar un tipo de Barco primero.");
            return;
        }

        String rutaFxml = "";
        String tituloVentana = "";

        if (btnCatamaran.isSelected()) {
            rutaFxml = "/patagonia/view/Barco_Catamaran.fxml";
            tituloVentana = "Asientos - Catamarán";
        } else if (btnFerry.isSelected()) {
            rutaFxml = "/patagonia/view/Barco_Ferry.fxml";
            tituloVentana = "Asientos - Ferry";
        } else if (btnWellboat.isSelected()) {
            rutaFxml = "/patagonia/view/Barco_Wellboat.fxml";
            tituloVentana = "Asientos - Wellboat";
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            BarcoWellboatController controllerBarco = loader.getController();
            Viaje viajeActual = buscarViajeActual();
            
            controllerBarco.inicializarDatos(viajeActual, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(tituloVentana);
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el mapa: " + rutaFxml + "\nVerifica que el archivo exista en src/main/resources/patagonia/view/");
        }
    }

    public void recibirAsientoSeleccionado(String asiento) {
        this.asientoSeleccionado = asiento;
        mostrarAlerta("Asiento Seleccionado", "Has elegido el asiento: " + asiento);
        if (txtAsientoElegido != null) {
            txtAsientoElegido.setText(asiento);
        }
    }

    @FXML
    void realizarVenta(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || txtRut.getText().isEmpty() || 
            cmbDestino.getValue() == null || grupoBarcos.getSelectedToggle() == null || 
            dateFecha.getValue() == null) {
            mostrarAlerta("Faltan datos", "Por favor completa todos los campos.");
            return;
        }

        if (asientoSeleccionado == null) {
            mostrarAlerta("Falta Asiento", "Debes seleccionar un asiento antes de guardar.");
            return;
        }

        String nombre = txtNombre.getText();
        String rut = txtRut.getText();
        Destino destino = cmbDestino.getValue();
        LocalDate fecha = dateFecha.getValue();
        
        Viaje viajeCorrespondiente = buscarViajeActual();
        
        if (viajeCorrespondiente == null) {
            Embarcacion barcoDeLaFlota = buscarBarcoDisponible();
            if (barcoDeLaFlota == null) {
                mostrarAlerta("Error de Flota", "No hay barcos disponibles de ese tipo en la flota.");
                return;
            }
            viajeCorrespondiente = new Viaje(fecha, "10:00", barcoDeLaFlota, destino);
            Main.listaViajes.add(viajeCorrespondiente);
        }

        Cliente cliente = new Cliente(nombre, rut, 0); 
        Pasaje nuevoPasaje = new Pasaje(asientoSeleccionado, precioCalculado, cliente); 

        if (viajeCorrespondiente.agregarPasaje(nuevoPasaje)) {
            Main.guardarCambios();
            irPantallaFinalizacion();
        } else {
            mostrarAlerta("Error", "No se pudo vender: el barco está lleno.");
        }
    }

    private Viaje buscarViajeActual() {
        LocalDate fecha = dateFecha.getValue();
        Destino destino = cmbDestino.getValue();
        if (fecha == null || destino == null) return null;

        for (Viaje v : Main.listaViajes) {
            if (v.getFecha().equals(fecha) && v.getDestino().getNombre().equals(destino.getNombre())) {
                if (validarTipoBarco(v.getEmbarcacionAsignada())) {
                    return v;
                }
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
        return null;
    }

    @FXML void seleccionarBarco(ActionEvent event) { calcularTotal(); }
    
    @FXML void aplicarDescuento(ActionEvent event) {
        if (precioCalculado > 0) {
            int descuento = (int) (precioCalculado * 0.10);
            txtTotal.setText("$ " + (precioCalculado - descuento));
        }
    }

    @FXML void quitarDescuento(ActionEvent event) { calcularTotal(); }

    private void calcularTotal() {
        if (cmbDestino.getValue() == null) return;
        int precioBase = cmbDestino.getValue().getPrecioPasaje();
        txtTarifa.setText("$ " + precioBase);
        int recargo = 0;
        if (btnFerry.isSelected()) recargo = 5000;
        else if (btnWellboat.isSelected()) recargo = 10000;
        this.precioCalculado = precioBase + recargo;
        txtTotal.setText("$ " + precioCalculado);
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
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}