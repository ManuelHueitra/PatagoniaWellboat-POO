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

    @FXML private TextField txtRutCliente;
    @FXML private Button btnIngresar;
    @FXML
    void ingresarCliente(ActionEvent event) {
        if (txtRutCliente == null) {
            System.out.println("Error: txtRutCliente es null. Revisa el fx:id en el FXML.");
            return;
        }

        String rut = txtRutCliente.getText().trim();

        if (rut.isEmpty()) {
            mostrarAlerta("Por favor ingrese su RUT.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patagonia/view/SeguimientoPedido.fxml"));
            Parent root = loader.load();

            SeguimientoController controller = loader.getController();
            controller.cargarDatosCliente("Cliente", rut);

            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Estado del Pedido");

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar el sistema de seguimiento: " + e.getMessage());
        }
    }
    @FXML
    void volverInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuPrincipal.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}