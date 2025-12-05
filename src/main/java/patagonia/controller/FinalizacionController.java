package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class FinalizacionController {

    @FXML
    private Button btnRegresar;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        lblMensaje.setText("Tu pasaje ha sido registrado.");
    }

    @FXML
    void volverMenuPrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patagonia/view/MenuAsistente.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Patagonia Wellboat - Menú Asistente");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al intentar volver al menú.");
        }
    }
}