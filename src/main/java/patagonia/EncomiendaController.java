package patagonia;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EncomiendaController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtOrigen;
    @FXML private ComboBox<Destino> cmbDestino;
    @FXML private TextField txtPeso;
    @FXML private TextField txtTotal;
    @FXML private TextField txtDescripcion;
    @FXML private Button btnAceptar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar destinos
        cmbDestino.setItems(FXCollections.observableArrayList(Main.ListaDestinos));
        
        // Configurar el origen
        txtOrigen.setText("Puerto Montt (Chinquihue)");
        txtOrigen.setEditable(false);

        // Cada vez que escriban en el peso recalculamos el total
        txtPeso.textProperty().addListener((observable, oldValue, newValue) -> {
            // Validar que solo sean números y puntos
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtPeso.setText(oldValue);
            }
            calcularPrecio();
        });

        // Cada vez que cambien el destino, recalculamos
        cmbDestino.setOnAction(e -> calcularPrecio());
    }

    private void calcularPrecio() {
        try {
            if (cmbDestino.getValue() != null && !txtPeso.getText().isEmpty()) {
                Destino destino = cmbDestino.getValue();
                double peso = Double.parseDouble(txtPeso.getText());
                
                //Peso * Precio por Kg del destino
                int total = (int) (peso * destino.getPrecioEncomiendaKg());
                
                txtTotal.setText("$ " + total);
            } else {
                txtTotal.setText("$ 0");
            }
        } catch (NumberFormatException e) {
            txtTotal.setText("$ 0"); // En caso de error, ponemos 0
        }
    }

    // guardar encomienda
    @FXML
    void registrarEncomienda(ActionEvent event) {
        
        if (txtNombre.getText().isEmpty() || txtRut.getText().isEmpty() || 
            dateFecha.getValue() == null || cmbDestino.getValue() == null || 
            txtPeso.getText().isEmpty()) {
            
            mostrarAlerta("Error", "Por favor complete todos los campos obligatorios.");
            return;
        }

        // Crear el objeto Encomienda
        double peso = Double.parseDouble(txtPeso.getText());
        int precioFinal = Integer.parseInt(txtTotal.getText().replace("$ ", ""));
        String descripcion = txtDescripcion.getText();
        
        Cliente cliente = new Cliente(txtNombre.getText(), txtRut.getText(), 0);
        
        Encomienda nuevaEncomienda = new Encomienda(peso, precioFinal, descripcion, cliente);

        
        mostrarAlerta("Éxito", "Encomienda registrada correctamente.\n" +
                    "Destino: " + cmbDestino.getValue().getNombre() + "\n" +
                    "Total a Pagar: " + txtTotal.getText());
        
        Main.guardarCambios();
        
        limpiarFormulario();
    }

    @FXML
    void volverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/Menu_Asistente.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtRut.clear();
        txtPeso.clear();
        txtDescripcion.clear();
        txtTotal.setText("$ 0");
        cmbDestino.getSelectionModel().clearSelection();
        dateFecha.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}