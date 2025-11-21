package patagonia;

public class Destino {
    private String nombre;
    private int precioPasaje;
    private int precioEncomientaKg;

public Destino(String nombre, int precioPasaje, int precioEncomientaKg) {
        this.nombre = nombre;
        this.precioPasaje = precioPasaje;
        this.precioEncomientaKg = precioEncomientaKg;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecioPasaje() {
        return precioPasaje;
    }

    public int getPrecioEncomientaKg() {
        return precioEncomientaKg;
    }
}

