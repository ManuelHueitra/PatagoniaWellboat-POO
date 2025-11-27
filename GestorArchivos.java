package patagonia;

import java.io.*; // salida e entrada del archivo
import java.util.List; // listas
public class GestorArchivos {
    private static final String ARCHIVO_DATOS = "datos_patagonia.dat"; // nombre del archivo
    public static void guardarDatos(List<Usuario> usuarios, List<Destino> destinos, List<Embarcacion> flota, List<Viaje> viajes) {
        // 1. Abrimos un salida  hacia el archivo datos_patagonia.dat
        // convierte la lista en bytes
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
        
        oos.writeObject(usuarios);
        oos.writeObject(destinos);
        oos.writeObject(flota);
        oos.writeObject(viajes);
        
        System.out.println("Datos guardados...");
    } catch (IOException e) {
        // Si falla muestra el error
        System.err.println("Error al guardar: " + e.getMessage());
    }
}
public static boolean cargarDatos(List<Usuario> usuarios, List<Destino> destinos, List<Embarcacion> flota, List<Viaje> viajes) {
    File archivo = new File(ARCHIVO_DATOS);
    //Verificamos si el archivo existe
    // si no existe mandamos un false al main
    if (!archivo.exists()) return false;

    // Abrimos el flujo de lectura
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
        
        // leemos los objetos del archivo
        List<Usuario> u = (List<Usuario>) ois.readObject();
        List<Destino> d = (List<Destino>) ois.readObject();
        List<Embarcacion> f = (List<Embarcacion>) ois.readObject();
        List<Viaje> v = (List<Viaje>) ois.readObject();

        // 4. Limpiamos la lista actual antes de cargar los datos
        usuarios.clear(); 
        usuarios.addAll(u);
        
        destinos.clear(); destinos.addAll(d);
        flota.clear(); flota.addAll(f);
        viajes.clear(); viajes.addAll(v);
        
        return true;
    } catch (Exception e) {
        // Si el archivo está corrupto manda un false al main
        System.err.println("Error al cargar: " + e.getMessage());
        return false;
    }
}
}
