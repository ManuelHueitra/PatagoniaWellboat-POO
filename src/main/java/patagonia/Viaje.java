package patagonia;

import java.util.Date;
import java.io.Serializable;

public class Viaje implements Serializable {
    private Date fecha;
    private String hora;
    private Embarcacion embarcacionAsignada;
    private Destino destino;

    private int pasajerosActuales;
    private double cargaActualKg;

    public Viaje(Date fecha, String hora, Embarcacion embarcacionAsignada, Destino destino) {
        this.fecha = fecha;
        this.hora = hora;
        this.embarcacionAsignada = embarcacionAsignada;
        this.destino = destino;
        this.pasajerosActuales = 0;
        this.cargaActualKg = 0.0;
    }

    // Getters
    public Date getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public Embarcacion getEmbarcacionAsignada() {
        return embarcacionAsignada;
    }

    public Destino getDestino() {
        return destino;
    }

    public int getPasajerosActuales() {
        return pasajerosActuales;
    }

    public double getCargaActualKg() {
        return cargaActualKg;
    }

    // Setters
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setEmbarcacionAsignada(Embarcacion embarcacionAsignada) {
        this.embarcacionAsignada = embarcacionAsignada;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    // Métodos para modificar pasajeros y carga con validaciones
    public boolean agregarPasajeros(int cantidad) {
        if (cantidad <= 0) return false;
        if (embarcacionAsignada != null) {
            int capacidad = embarcacionAsignada.getCapacidadPasajeros();
            if (pasajerosActuales + cantidad > capacidad) {
                return false;
            }
        }
        pasajerosActuales += cantidad;
        return true;
    }

    public boolean retirarPasajeros(int cantidad) {
        if (cantidad <= 0 || cantidad > pasajerosActuales) return false;
        pasajerosActuales -= cantidad;
        return true;
    }

    public boolean agregarCargaKg(double kg) {
        if (kg <= 0) return false;
        if (embarcacionAsignada != null) {
            double capacidad = embarcacionAsignada.getCapacidadCarga();
            if (cargaActualKg + kg > capacidad) {
                return false;
            }
        }
        cargaActualKg += kg;
        return true;
    }

    public boolean retirarCargaKg(double kg) {
        if (kg <= 0 || kg > cargaActualKg) return false;
        cargaActualKg -= kg;
        return true;
    }

    @Override
    public String toString() {
        String embar = embarcacionAsignada != null ? embarcacionAsignada.getPatente() : "sin embarcación";
        String dest = destino != null ? destino.getNombre() : "sin destino";
        return "Viaje{" +
                "fecha=" + fecha +
                ", hora='" + hora + '\'' +
                ", embarcacion=" + embar +
                ", destino=" + dest +
                ", pasajerosActuales=" + pasajerosActuales +
                ", cargaActualKg=" + cargaActualKg +
                '}';
    }
}
