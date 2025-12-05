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

    private int precioCalculado = 0;

    @FXML
    public void initialize() {
        ObservableList<Destino> misDestinos = FXCollections.observableArrayList(Main.listaDestinos);
        cmbDestino.setItems(misDestinos);
        
        cmbDestino.setOnAction(e -> calcularTotal());
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
            precioCalculado = totalConDescuento; 
        }
    }

    @FXML
    void quitarDescuento(ActionEvent event) {
        calcularTotal(); //recalcula el precio original
    }

    private void calcularTotal() {
        if (cmbDestino.getValue() == null) return;
        
        int precioBase = cmbDestino.getValue().getPrecioPasaje();
        txtTarifa.setText("$ " + precioBase);

        int recargo = 0;
        if (btnFerry.isSelected()) recargo = 5000;
        else if (btnWellboat.isSelected()) recargo = 10000;

        this.precioCalculado = precioBase + recargo;
        
        // mostramos al usuario
        txtTotal.setText("$ " + precioCalculado);
    }

    @FXML
    void realizarVenta(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || txtRut.getText().isEmpty() || 
            cmbDestino.getValue() == null || grupoBarcos.getSelectedToggle() == null || 
            dateFecha.getValue() == null) {
            mostrarAlerta("Faltan datos", "Por favor completa todos los campos.");
            return;
        }

        String nombre = txtNombre.getText();
        String rut = txtRut.getText();
        Destino destino = cmbDestino.getValue();
        LocalDate fecha = dateFecha.getValue();
        
        // buscamos un viaje existente para esa fecha y destino
        Viaje viajeCorrespondiente = null;
        for (Viaje v : Main.listaViajes) {
            if (v.getFecha().equals(fecha) && v.getDestino().getNombre().equals(destino.getNombre())) {
                viajeCorrespondiente = v;
                break;
            }
        }
        
        // si no existe viaje crearlo buscando un barco de la flota
        if (viajeCorrespondiente == null) {
            Embarcacion barcoDeLaFlota = buscarBarcoDisponible();
            
            if (barcoDeLaFlota == null) {
                mostrarAlerta("error de Flota", "no hay barcos disponibles de ese tipo en la flota.");
                return;
            }
            
            //creamos el viaje con el barco encontrado
            viajeCorrespondiente = new Viaje(fecha, "10:00", barcoDeLaFlota, destino);
            Main.listaViajes.add(viajeCorrespondiente);
        }

        //crear Pasaje
        Cliente cliente = new Cliente(nombre, rut, 0);
        // usamos la variable precioCalculado
        Pasaje nuevoPasaje = new Pasaje(0, precioCalculado, cliente); 

        // intentar agregar pasaje y verifica capacidad
        if (viajeCorrespondiente.agregarPasaje(nuevoPasaje)) {
            Main.guardarCambios();
            irPantallaFinalizacion();
        } else {
            mostrarAlerta("error", "no se pudo vender: el barco para esta fecha está lleno.");
        }
    }

    private Embarcacion buscarBarcoDisponible() {
        // recorremos la lista de barcos
        for (Embarcacion e : Main.listaEmbarcaciones) {
            if (btnCatamaran.isSelected() && e instanceof CatamaranLiviano) return e;
            if (btnFerry.isSelected() && e instanceof FerryMediano) return e;
            if (btnWellboat.isSelected() && e instanceof WellboatGranCapacidad) return e;
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