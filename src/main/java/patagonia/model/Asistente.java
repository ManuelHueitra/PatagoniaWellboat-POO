package patagonia.model;

import java.io.Serializable;

public class Asistente extends Usuario implements Serializable {

    public Asistente(String nombre, String password) {
        super(nombre, password, "Asistente");
    }
}