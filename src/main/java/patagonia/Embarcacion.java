package patagonia;
import java.io.Serializable;
// clase Embarcacion padre
public abstract class Embarcacion implements Serializable {
    protected String patente;
    protected int capacidadPasajeros;
    protected double capacidadCarga;

    public Embarcacion(String patente, int capacidadPasajeros, double capacidadCarga) {
        this.patente = patente;
        this.capacidadPasajeros = capacidadPasajeros;
        this.capacidadCarga = capacidadCarga;
    }
    public String getPatente() {
        return patente;
    }
    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }
    public double getCapacidadCarga() {
        return capacidadCarga;
    }
}
