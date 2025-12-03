package patagonia.model;

import java.io.Serializable;

public class Gerente extends Usuario implements Serializable {

    public Gerente(String nombre, String password) {
        super(nombre, password, "Gerente");
    }
}