package patagonia.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {

    @FXML private Button btnCliente;
    @FXML private Button btnColaboradores;
    @FXML private Button btnSalir;

    @FXML
    public void initialize() {
        btnCliente.setOnAction(event -> irPantalla("/patagonia/view/LoginCliente.fxml"));
        btnColaboradores.setOnAction(event -> irPantalla("/patagonia/view/LoginColaboradores.fxml"));
    }

    //  cerrar el programa
    @FXML
    void salirDelPrograma() {
        Platform.exit();
    }

    private void irPantalla(String rutaFxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage stage = (Stage) btnCliente.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}