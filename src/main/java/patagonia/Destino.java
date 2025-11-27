package patagonia;

import java.io.Serializable;

public class Destino implements Serializable{
    private String nombre;
    private int precioPasaje;
    private int precioEncomiendaKg;

public Destino(String nombre, int precioPasaje, int precioEncomiendaKg) {
        this.nombre = nombre;
        this.precioPasaje = precioPasaje;
        this.precioEncomiendaKg = precioEncomiendaKg;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecioPasaje() {
        return precioPasaje;
    }

    public int getPrecioEncomiendaKg() {
        return precioEncomiendaKg;
    }
}

