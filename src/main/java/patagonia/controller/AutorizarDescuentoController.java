package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AutorizarDescuentoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private TextField txtTarifa;
    @FXML private TextField txtTotal;
    @FXML private Button btnVender;

    @FXML
    void realizarVenta(ActionEvent event) {
        String nombre = txtNombre.getText();
        String rut = txtRut.getText();
        String valorStr = txtTarifa.getText();
        String descuentoStr = txtTotal.getText();

        if (nombre.isEmpty() || rut.isEmpty() || valorStr.isEmpty() || descuentoStr.isEmpty()) {
            mostrarAlerta("Error", "Por favor completa todos los campos.");
            return;
        }

        try {
            int valor = Integer.parseInt(valorStr);
            int porcentaje = Integer.parseInt(descuentoStr);
            
            int totalFinal = valor - (valor * porcentaje / 100);
            
            System.out.println("Autorizando descuento del " + porcentaje + "% al valor " + valor + ". Nuevo total: " + totalFinal);
            
            irPantallaRealizado();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El Valor y el Descuento deben ser números enteros.");
        }
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuGerente.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Patagonia Wellboat - Menú Gerente");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo volver al menú.");
        }
    }

    private void irPantallaRealizado() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/RealizadoDescuento.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}