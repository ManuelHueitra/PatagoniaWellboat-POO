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
    @FXML private Button btnSalir;

    public void cargarDatosCliente(String nombre, String rut) {
        boolean encontrado = false;

        // recorremos los viajes de atras hacia adelante para ver el mas reciente
        for (int i = Main.listaViajes.size() - 1; i >= 0; i--) {
            Viaje v = Main.listaViajes.get(i);
            for (Pasaje p : v.getListaPasajes()) {
                if (p.getCliente().getRut().equalsIgnoreCase(rut)) {
                    mostrarDatos("Pasaje Comprado", v.getFecha().toString(), v.getDestino().getNombre(), "$ " + p.getPreciofinal());
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) break;
        }

        // si no encontramos pasaje buscamos en Encomiendas
        if (!encontrado) {
            for (int i = Main.listaEncomiendas.size() - 1; i >= 0; i--) {
                Encomienda e = Main.listaEncomiendas.get(i);
                if (e.getCliente().getRut().equalsIgnoreCase(rut)) {
                    mostrarDatos("Encomienda", "Fecha Reciente", e.getDescripcion(), "$ " + e.getPrecioFinal());
                    encontrado = true;
                    break;
                }
            }
        }

        // no hay nada
        if (!encontrado) {
            lblEstado.setText("SIN MOVIMIENTOS");
            lblFecha.setText("--/--/----");
            lblDestino.setText("---");
            lblMonto.setText("$ 0");
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