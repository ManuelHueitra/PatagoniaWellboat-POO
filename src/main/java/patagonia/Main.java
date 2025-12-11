package patagonia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import patagonia.model.*;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    //listas globales 
    public static List<Usuario> listaUsuarios = new ArrayList<>();
    public static List<Embarcacion> listaEmbarcaciones = new ArrayList<>();
    public static List<Destino> listaDestinos = new ArrayList<>();
    public static List<Viaje> listaViajes = new ArrayList<>();
    public static List<Encomienda> listaEncomiendas = new ArrayList<>(); 

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/patagonia/view/MenuPrincipal.fxml"));
        
        primaryStage.setTitle("Patagonia Wellboat - Acceso");
        primaryStage.setScene(new Scene(root)); 
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        boolean cargado = GestorArchivos.cargarDatos(listaUsuarios, listaDestinos, listaEmbarcaciones, listaViajes, listaEncomiendas);
        if (!cargado) {
            cargarDatosIniciales();
            guardarCambios();
        }
        launch(args);
    }

    public static void guardarCambios() {
        GestorArchivos.guardarDatos(listaUsuarios, listaDestinos, listaEmbarcaciones, listaViajes, listaEncomiendas);
    }

    private static void cargarDatosIniciales(){
        System.out.println("Generando datos iniciales...");
        
        listaUsuarios.add(new Gerente("admin", "admin123")); 
        
        listaUsuarios.add(new Asistente("vendedor", "1234"));
        
        listaUsuarios.add(new Usuario("cliente", "111", "Cliente"));

        // Destinos
        listaDestinos.add(new Destino("Calbuco", 15000, 4000));
        listaDestinos.add(new Destino("Chonchi", 40000, 10000));
        listaDestinos.add(new Destino("Maillen", 15000, 4000));
        listaDestinos.add(new Destino("Isla Puluqui", 20000,5000));
        listaDestinos.add(new Destino("Achao", 25000, 6000));
        listaDestinos.add(new Destino("Quinchao", 25000, 6000));
        listaDestinos.add(new Destino("Dalcahue", 30000, 8000));
        listaDestinos.add(new Destino("Curaco de Vélez", 30000, 8000));
        listaDestinos.add(new Destino("Queilen", 40000, 10000));
        listaDestinos.add(new Destino("Chonchi", 40000, 10000));
        listaDestinos.add(new Destino("Melinka", 50000, 12000));
        listaDestinos.add(new Destino("Quellón", 50000, 12000));
        // Flota
        listaEmbarcaciones.add(new CatamaranLiviano("CAT-001")); 
        listaEmbarcaciones.add(new CatamaranLiviano("CAT-002"));
        listaEmbarcaciones.add(new FerryMediano("FER-001"));

        System.out.println("Datos iniciales listos.");
    }
}