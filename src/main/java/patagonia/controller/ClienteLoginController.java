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

public class ClienteLoginController {

    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtRutCliente;
    @FXML private Button btnIngresar;

    @FXML
    void ingresarCliente(ActionEvent event) {
        String nombre = txtNombreCliente.getText().trim();
        String rut = txtRutCliente.getText().trim();

        if (nombre.isEmpty() || rut.isEmpty()) {
            mostrarAlerta("error", "escribe tu Nombre y RUT.");
        } else {
            
            irPantallaSeguimiento(nombre, rut);
        }
    }

    @FXML
    void volverInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuPrincipal.fxml"));
            Stage stage = (Stage) txtNombreCliente.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void irPantallaSeguimiento(String nombre, String rut) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patagonia/view/SeguimientoPedido.fxml"));
            Parent root = loader.load();

            SeguimientoController controller = loader.getController();
            controller.cargarDatosCliente(nombre, rut);

            Stage stage = (Stage) txtNombreCliente.getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("error", "No se pudo cargar la pantalla de seguimiento.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}