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
        // Cargar destinos
        ObservableList<Destino> misDestinos = FXCollections.observableArrayList(Main.listaDestinos);
        cmbDestino.setItems(misDestinos);
        
        // Recalcular precio al cambiar destino
        cmbDestino.setOnAction(e -> calcularTotal());
    }

    @FXML
    void abrirMapaAsientos(ActionEvent event) {
        // validar que tengamos fecha y destino para saber que viaje buscar
        if (dateFecha.getValue() == null || cmbDestino.getValue() == null) {
            mostrarAlerta("Atencion", "Por favor selecciona Fecha y Destino antes de ver los asientos.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patagonia/view/Barco_Wellboat.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del barco y pasarle los datos
            BarcoWellboatController controllerBarco = loader.getController();
            
            // buscamos el viaje actual si existe para mostrar los ocupados en rojo
            Viaje viajeActual = buscarViajeActual();
            controllerBarco.inicializarDatos(viajeActual, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Selección de Asiento");
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el mapa de asientos. Verifica que Barco_Wellboat.fxml exista.");
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
    void seleccionarBarco(ActionEvent event) {
        calcularTotal();
    }
    
    @FXML
    void aplicarDescuento(ActionEvent event) {
        if (precioCalculado > 0) {
            int descuento = (int) (precioCalculado * 0.10);
            int totalConDescuento = precioCalculado - descuento;
            txtTotal.setText("$ " + totalConDescuento);
        }
    }

    @FXML
    void quitarDescuento(ActionEvent event) {
        calcularTotal(); // Recalcula el precio original
    }

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

    private Viaje buscarViajeActual() {
        LocalDate fecha = dateFecha.getValue();
        Destino destino = cmbDestino.getValue();
        
        if (fecha == null || destino == null) return null;

        for (Viaje v : Main.listaViajes) {
            if (v.getFecha().equals(fecha) && v.getDestino().getNombre().equals(destino.getNombre())) {
                return v;
            }
        }
        return null;
    }

    @FXML
    void realizarVenta(ActionEvent event) {
        // Validaciones
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

        // Crear Pasaje
        Cliente cliente = new Cliente(nombre, rut, 0); // 0 viajes
        
        Pasaje nuevoPasaje = new Pasaje(asientoSeleccionado, precioCalculado, cliente); 

        if (viajeCorrespondiente.agregarPasaje(nuevoPasaje)) {
            Main.guardarCambios();
            irPantallaFinalizacion();
        } else {
            mostrarAlerta("Error", "No se pudo vender: el barco para esta fecha está lleno.");
        }
    }

    private Embarcacion buscarBarcoDisponible() {
        for (Embarcacion e : Main.listaEmbarcaciones) {
            if (btnCatamaran.isSelected() && e instanceof CatamaranLiviano) 
                return e;
            if (btnFerry.isSelected() && e instanceof FerryMediano) 
                return e;
            if (btnWellboat.isSelected() && e instanceof WellboatGranCapacidad) 
                return e;
        }
        return null;
    }

    private void irPantallaFinalizacion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/Finalizacion.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void volverMenu(ActionEvent event) {
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