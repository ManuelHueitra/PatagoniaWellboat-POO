package patagonia;
import java.io.Serializable;

public class pasaje implements Serializable {
    private int numeroAsiento;
    private int preciofinal;
    private Cliente cliente;

    public pasaje(int numeroAsiento, int preciofinal, Cliente cliente) {
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