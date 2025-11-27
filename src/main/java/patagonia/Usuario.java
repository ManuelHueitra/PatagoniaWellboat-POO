package patagonia;
import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String password;
    private String rol; // "Gerente" o "Asistente"
    
    public Usuario(String nombre, String password, String rol ) {
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;

    }

    // Getters necesarios para el LoginController
    public String getNombre() {
        return nombre; }
    public String getPassword() {
        return password; }
    public String getRol() {
        return rol; }
}
