package patagonia;

import java.util.Date;
import javax.swing.plaf.DesktopIconUI;
public class Viaje implements Serializable;
public class Viaje {
    private Date Fecha;
    private String hora;
    private Embarcacion embarcacionAsignada;
    private Destino destino;
    
    private int pasajerosActuales;
    private double cargaActualKg;
    
    public Viaje(Date fecha, String hora, Embarcacion embarcacionAsignada, Destino destino) {
        this.Fecha = fecha;
        this.hora = hora;
        this.embarcacionAsignada = embarcacionAsignada;
        this.destino = destino;
        this.pasajerosActuales = 0;
        this.cargaActualKg = 0.0;
    }
}
