package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuAsistenteController {

    @FXML private Button btnViajePersonal;
    @FXML private Button btnEncomiendas;
    @FXML private Button btnExcepciones;
    @FXML private Button btnSalir;
    @FXML
    public void initialize() {
        btnViajePersonal.setOnAction(event -> {
            irPantalla("/patagonia/view/ViajePersonal.fxml", "Gestión de Viajes");
        });

        btnEncomiendas.setOnAction(event -> { 
            irPantalla("/patagonia/view/Encomienda.fxml", "Gestión de Encomiendas");
        });

        btnExcepciones.setOnAction(event -> {
            irPantalla("/patagonia/view/Excepciones.fxml", "Registro de Excepciones");
        });
    }
    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            // volvemos al login de colaboradores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patagonia/view/LoginColaboradores.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void irPantalla(String rutaFxml, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();
            
            
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.setTitle("Patagonia Wellboat - " + tituloVentana);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("error de Navegación", "No se pudo cargar: " + rutaFxml + "\nverifica el nombre del archivo.");
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