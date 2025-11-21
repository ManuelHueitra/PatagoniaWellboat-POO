package patagonia;

public class Cliente {
    private String nombre;
    private String rut;
    private int historialViajes;

    public Cliente(String nombre, String rut, int historialViajes) {
        this.nombre = nombre;
        this.rut = rut;
        this.historialViajes = historialViajes;
    }
    public boolean esFrecuente() {
        return this.historialViajes > 10;
    }
}
