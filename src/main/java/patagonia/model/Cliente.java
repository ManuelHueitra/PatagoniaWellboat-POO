package patagonia.model;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String rut;
    private int historialViajes;

    public Cliente(String nombre, String rut, int historialViajes) {
        this.nombre = nombre;
        this.rut = rut;
        this.historialViajes = historialViajes;
    }
    public String getNombre() {
        return nombre;
    }
    public String getRut() {
        return rut;
    }
    public int getHistorialViajes() {
        return historialViajes;
    }
    public boolean esFrecuente() {
        return this.historialViajes > 10;
    }
}