package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import patagonia.Main;
import patagonia.model.Usuario;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIngresar;
    @FXML private Button btnVolver;

    @FXML
    void btnIngresarAction(ActionEvent event) {
        String usuarioIngresado = txtUsuario.getText();
        String passwordIngresado = txtContrasena.getText();

        if (usuarioIngresado.isEmpty() || passwordIngresado.isEmpty()) {
            mostrarAlerta("Error", "Por favor, escribe usuario y contraseña.");
            return; 
        }

        boolean encontrado = false;
        Usuario usuarioLogueado = null;

        for (Usuario u : Main.listaUsuarios) {
            if (u.getNombre().equals(usuarioIngresado) && u.getPassword().equals(passwordIngresado)) {
                usuarioLogueado = u;
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            mostrarMensaje("Acceso Concedido", "Bienvenido, " + usuarioLogueado.getNombre());
            
            if (usuarioLogueado.getRol().equalsIgnoreCase("Gerente") || 
            usuarioLogueado.getRol().equalsIgnoreCase("Asistente")) {
            irPantalla("/patagonia/view/MenuAsistente.fxml");
            } else {
                mostrarAlerta("Aviso", "Este menú es solo para personal.");
            }

        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    void volverInicio(ActionEvent event) {
        irPantalla("/patagonia/view/MenuPrincipal.fxml");
    }

    private void irPantalla(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml)); 
            Parent root = loader.load();
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); //error al no cargar la pantalla
            mostrarAlerta("Error", "No se pudo cargar la pantalla: " + rutaFxml);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}