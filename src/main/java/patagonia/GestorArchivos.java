package patagonia;

import patagonia.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    private static final String ARCHIVO_DATOS = "datos_patagonia.txt";
    public static void guardarDatos(List<Usuario> usuarios, List<Destino> destinos, List<Embarcacion> embarcaciones, List<Viaje> viajes, List<Encomienda> encomiendas) { // <--- Agregamos Encomiendas aquí
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(usuarios);
            oos.writeObject(destinos);
            oos.writeObject(embarcaciones);
            oos.writeObject(viajes);
            oos.writeObject(encomiendas);
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean cargarDatos(List<Usuario> usuarios, List<Destino> destinos, List<Embarcacion> embarcaciones, List<Viaje> viajes,List<Encomienda> encomiendas) { // <--- Recibir la lista vacía para llenarla
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            usuarios.clear();
            destinos.clear();
            embarcaciones.clear();
            viajes.clear();
            encomiendas.clear();
            usuarios.addAll((List<Usuario>) ois.readObject());
            destinos.addAll((List<Destino>) ois.readObject());
            embarcaciones.addAll((List<Embarcacion>) ois.readObject());
            viajes.addAll((List<Viaje>) ois.readObject());
            try {
                encomiendas.addAll((List<Encomienda>) ois.readObject());
            } catch (Exception e) {
                System.out.println("Advertencia: No se encontraron encomiendas en el archivo antiguo.");
            }

            System.out.println("Datos cargados correctamente.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar datos (posiblemente archivo corrupto o versión antigua): " + e.getMessage());
            return false;
        }
    }
}