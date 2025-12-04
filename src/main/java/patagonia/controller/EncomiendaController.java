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

public class EncomiendaController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtOrigen;
    @FXML private ComboBox<Destino> cmbDestino;
    @FXML private TextField txtPeso;
    @FXML private TextField txtTarifa; 
    @FXML private TextField txtTotal; 

    @FXML
    public void initialize() {
        ObservableList<Destino> misDestinos = FXCollections.observableArrayList(Main.listaDestinos);
        cmbDestino.setItems(misDestinos);
        txtOrigen.setText("Puerto Montt (Chinquihue)");
        txtOrigen.setEditable(false);

        cmbDestino.setOnAction(event -> actualizarCalculos());
        txtPeso.setOnKeyReleased(event -> actualizarCalculos());
    }

    private void actualizarCalculos() {
        try {
            Destino destino = cmbDestino.getValue();
            if (destino != null) {
                int tarifa = destino.getPrecioEncomiendaKg();
                txtTarifa.setText("$ " + tarifa);
                String pesoTexto = txtPeso.getText();
                if (!pesoTexto.isEmpty()) {
                    double peso = Double.parseDouble(pesoTexto);
                    int total = (int) (peso * tarifa);
                    txtTotal.setText("$ " + total);
                } else {
                    txtTotal.setText("$ 0");
                }
            } else {
                txtTarifa.setText("");
                txtTotal.setText("$ 0");
            }
        } catch (NumberFormatException e) {
            txtTotal.setText("$ 0");
        }
    }

    @FXML
    void registrarEncomienda(ActionEvent event) {
        String nombre = txtNombre.getText();
        String rut = txtRut.getText();
        Destino destino = cmbDestino.getValue();
        String pesoStr = txtPeso.getText();
        String totalStr = txtTotal.getText().replace("$", "").trim();

        if (nombre.isEmpty() || rut.isEmpty() || destino == null || pesoStr.isEmpty()) {
            mostrarAlerta("Faltan datos", "Por favor completa todos los campos.");
            return;
        }

        try {
            double peso = Double.parseDouble(pesoStr);
            int precioFinal = Integer.parseInt(totalStr);
            Cliente cliente = new Cliente(nombre, rut, 0);
            String descripcion = "Encomienda a " + destino.getNombre();
            Encomienda nuevaEncomienda = new Encomienda(peso, precioFinal, descripcion, cliente);

            Main.listaEncomiendas.add(nuevaEncomienda);
            Main.guardarCambios();

            // --- CAMBIO: IR A PANTALLA FINALIZACIÓN ---
            irPantallaFinalizacion();

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar. Revisa los números.");
        }
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/Menu_Asistente.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    // finalizar
    private void irPantallaFinalizacion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/Finalizacion.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}