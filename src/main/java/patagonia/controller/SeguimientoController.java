package patagonia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import patagonia.Main;
import patagonia.model.Encomienda;
import patagonia.model.Pasaje;
import patagonia.model.Viaje;

import java.io.IOException;

public class SeguimientoController {

    @FXML private Label lblEstado;
    @FXML private Label lblFecha;
    @FXML private Label lblDestino;
    @FXML private Label lblMonto;
    @FXML private Label lblCantidadViajes;
    @FXML private Button btnSalir;

    public void cargarDatosCliente(String nombreUsuario, String rutBuscado) {
        boolean encontrado = false;
        int contadorViajes = 0;

        for (int i = Main.listaViajes.size() - 1; i >= 0; i--) {
            Viaje v = Main.listaViajes.get(i);
            for (Pasaje p : v.getListaPasajes()) {
                if (p.getCliente().getRut().equalsIgnoreCase(rutBuscado)) {
                    contadorViajes++;
                    if (!encontrado) {
                        mostrarDatos("Pasaje - Confirmado", v.getFecha().toString(), v.getDestino().getNombre(), "$ " + p.getPreciofinal());
                        encontrado = true;
                    }
                }
            }
        }
        if (!encontrado) {
            for (int i = Main.listaEncomiendas.size() - 1; i >= 0; i--) {
                Encomienda e = Main.listaEncomiendas.get(i);
                if (e.getCliente().getRut().equalsIgnoreCase(rutBuscado)) {
                    mostrarDatos("Encomienda - En Reparto", "Pendiente", e.getDescripcion(), "$ " + e.getPrecioFinal());
                    encontrado = true;
                    break;
                }
            }
        }
        if (lblCantidadViajes != null) {
            lblCantidadViajes.setText(String.valueOf(contadorViajes));
        }

        if (!encontrado) {
            mostrarDatos("SIN MOVIMIENTOS RECIENTES", "--/--/----", "---", "$ 0");
        }
    }

    private void mostrarDatos(String estado, String fecha, String destino, String monto) {
        lblEstado.setText(estado);
        lblFecha.setText(fecha);
        lblDestino.setText(destino);
        lblMonto.setText(monto);
    }

    @FXML
    void volverInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/LoginCliente.fxml"));
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}