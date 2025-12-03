package patagonia.model;

import java.io.Serializable;

public class Pasaje implements Serializable {
    private int numeroAsiento;
    private int preciofinal;
    private Cliente cliente;

    public Pasaje(int numeroAsiento, int preciofinal, Cliente cliente) {
        this.numeroAsiento = numeroAsiento;
        this.preciofinal = preciofinal;
        this.cliente = cliente;
    }
    public int getNumeroAsiento() {
        return numeroAsiento;
    }
    public int getPreciofinal() {
        return preciofinal;
    }
    public Cliente getCliente() {
        return cliente;
    }
    
}