package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuGerenteController {

    @FXML
    void verResumenVentas(ActionEvent event) {
        irPantalla(event, "/patagonia/view/ResumenVentas.fxml", "Resumen de Ventas");
    }

    @FXML
    void programarEmergencia(ActionEvent event) {
        irPantalla(event, "/patagonia/view/ProgramarViajeExtraordinario.fxml", "Programar Viaje de Emergencia");
    }

    @FXML
    void autorizarDescuento(ActionEvent event) {
        irPantalla(event, "/patagonia/view/AutorizarDescuento.fxml", "Autorizar Descuento");
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        //volver al login de colaboradores
        irPantalla(event, "/patagonia/view/LoginColaboradores.fxml", "Patagonia Wellboat - Acceso Colaboradores");
    }

    private void irPantalla(ActionEvent event, String rutaFxml, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.setTitle(tituloVentana);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Navegación", "No se pudo cargar: " + rutaFxml + "\nVerifica que el archivo FXML exista en la carpeta resources.");
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