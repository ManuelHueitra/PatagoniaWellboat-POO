package patagonia.controller;

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

public class ExcepcionesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtHora;
    
    @FXML private Button btnNoCobro;
    @FXML private Button btnListo;

    @FXML
    public void initialize() {
    }

    @FXML
    void registrarNoCobro(ActionEvent event) {
        if (validar()) {
            guardarExcepcion(0); // Precio 0
        }
    }

    @FXML
    void registrarExcepcion(ActionEvent event) {
        if (validar()) {
            guardarExcepcion(0);
        }
    }

    private boolean validar() {
        if (txtNombre.getText().isEmpty() || txtRut.getText().isEmpty() || dateFecha.getValue() == null) {
            mostrarAlerta("Error", "faltan datos (Nombre, RUT o Fecha).");
            return false;
        }
        return true;
    }

    private void guardarExcepcion(int precio) {
        String nombre = txtNombre.getText();
        String rut = txtRut.getText();
        LocalDate fecha = dateFecha.getValue();
        
        //buscamos si ya existe un viaje para esa fecha
        Viaje viajeEncontrado = null;
        for (Viaje v : Main.listaViajes) {
            if (v.getFecha().equals(fecha)) {
                viajeEncontrado = v;
                break;
            }
        }

        if (viajeEncontrado == null) {
            if (Main.listaDestinos.isEmpty()) {
                mostrarAlerta("Error Crítico", "No hay destinos cargados en el sistema.");
                return;
            }
            Destino destinoDefault = Main.listaDestinos.get(0); 
            Embarcacion barcoDefault = new FerryMediano("EMERGENCIA");
            viajeEncontrado = new Viaje(fecha, "00:00", barcoDefault, destinoDefault);
            Main.listaViajes.add(viajeEncontrado);
        }

        Cliente cliente = new Cliente(nombre, rut, 0);
        
        Pasaje pasaje = new Pasaje("LIBRE", precio, cliente); 

        viajeEncontrado.agregarPasaje(pasaje);
        
        Main.guardarCambios();
        
        mostrarAlerta("Éxito", "Excepción registrada correctamente.");
        irPantallaFinalizacion();
    }

    private void irPantallaFinalizacion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/Finalizacion.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { 
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de finalización.");
        }
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuAsistente.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}