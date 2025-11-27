package patagonia;

import java.io.Serializable;

public class Encomienda implements Serializable {
    private double peso;
    private int precioFinal;
    private String descripcion; // descripción del contenido
    private Cliente cliente; // Quién envía

    public Encomienda(double peso, int precioFinal, String descripcion, Cliente cliente) {
        this.peso = peso;
        this.precioFinal = precioFinal;
        this.descripcion = descripcion;
        this.cliente = cliente;
    }

    public double getPeso() {
        return peso; 
    }
    public int getPrecioFinal() {
        return precioFinal; 
    }
    public String getDescripcion() {
        return descripcion; 
    }
    public Cliente getCliente() {
        return cliente; 
    }
    
}