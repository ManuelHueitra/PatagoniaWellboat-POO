package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class HistorialClienteController {

    @FXML private TextField txtDestino;
    @FXML private TextField txtFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtPrecio;

    @FXML
    public void initialize() {
        // Asignación simple de valores
        txtDestino.setText("Chonchi");
        txtFecha.setText("30/11/2025");
        txtHora.setText("09:00 hrs");
        txtPrecio.setText("$ 15.000");
    }

    @FXML
    void volverInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuInicio.fxml"));
            Scene escena = txtDestino.getScene();
            Stage ventana = (Stage) escena.getWindow();
            
            ventana.setScene(new Scene(root));
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }
}