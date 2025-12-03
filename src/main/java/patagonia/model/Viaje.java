package patagonia.model;

import java.io.Serializable;
import java.time.LocalDate; // Usamos LocalDate
import java.util.ArrayList;
import java.util.List;

public class Viaje implements Serializable {
    private LocalDate fecha;
    private String hora;
    private Embarcacion embarcacionAsignada;
    private Destino destino;

    private List<Pasaje> listaPasajes;
    private double cargaActualKg;

    public Viaje(LocalDate fecha, String hora, Embarcacion embarcacionAsignada, Destino destino) {
        this.fecha = fecha;
        this.hora = hora;
        this.embarcacionAsignada = embarcacionAsignada;
        this.destino = destino;
        this.listaPasajes = new ArrayList<>(); // lista vacia
        this.cargaActualKg = 0.0;
    }

    public boolean agregarPasaje(Pasaje nuevoPasaje) {
        if (embarcacionAsignada != null) {
            if (listaPasajes.size() >= embarcacionAsignada.getCapacidadPasajeros()) {
                return false; // Barco lleno
            }
        }
        listaPasajes.add(nuevoPasaje);
        return true;
    }

    public LocalDate getFecha() {
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
    public List<Pasaje> getListaPasajes() {
        return listaPasajes; 
    }
    
    // para saber cuantos van
    public int getPasajerosActuales() {
        return listaPasajes.size(); 
    }

    public boolean agregarCargaKg(double kg) {
        if (embarcacionAsignada != null) {
            if (cargaActualKg + kg > embarcacionAsignada.getCapacidadCarga()) {
                return false; // excede carga
            }
        }
        cargaActualKg += kg;
        return true;
    }

    @Override
    public String toString() {
        return "Viaje a " + destino.getNombre() + " (" + fecha + " " + hora + ")";
    }
}