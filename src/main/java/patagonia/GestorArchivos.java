package patagonia;

import java.io.*; // salida e entrada del archivo
import java.util.List; // listas
import patagonia.model.*; 

public class GestorArchivos {
    
    private static final String ARCHIVO_DATOS = "datos_patagonia.txt";

    public static void guardarDatos(List<Usuario> usuarios, List<Destino> destinos, List<Embarcacion> flota, List<Viaje> viajes, List<Encomienda> listaEncomiendas) {
        // guarda todo directo como objetos
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            
            oos.writeObject(usuarios);
            oos.writeObject(destinos);
            oos.writeObject(flota);
            oos.writeObject(viajes);
            oos.writeObject(listaEncomiendas);
            
            System.out.println("Datos guardados...");

        } catch (IOException e) {
            // si falla muestra el error
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public static boolean cargarDatos(List<Usuario> usuarios, List<Destino> destinos, List<Embarcacion> flota, List<Viaje> viajes, List<Encomienda> listaEncomiendas) {
        
        File archivo = new File(ARCHIVO_DATOS);
        
        // verificamos si el archivo existe
        // si no existe mandamos un false al main
        if (!archivo.exists()) {
            return false;
        }

        // abrimos el flujo de lectura
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
        //lee el contenido en binario y lo traduce para que tu programa lo entienda como objetos
        
            // leemos los objetos del archivo
            List<Usuario> u = (List<Usuario>) ois.readObject();
            List<Destino> d = (List<Destino>) ois.readObject();
            List<Embarcacion> f = (List<Embarcacion>) ois.readObject();
            List<Viaje> v = (List<Viaje>) ois.readObject();
            List<Encomienda> e = (List<Encomienda>) ois.readObject();

            // limpiamos la lista actual antes de cargar los datos
            usuarios.clear(); 
            usuarios.addAll(u);
            
            destinos.clear(); 
            destinos.addAll(d);
            
            flota.clear(); 
            flota.addAll(f);
            
            viajes.clear(); 
            viajes.addAll(v);
            
            listaEncomiendas.clear();
            listaEncomiendas.addAll(e);
            
            System.out.println("Datos cargados del txt exitosamente");
            return true;

        } catch (Exception e) {
            // si el archivo esta corrupto manda un false
            System.err.println("Error al cargar: " + e.getMessage());
            return false;
        }
    }
}