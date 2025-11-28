package patagonia;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
    public static List<Usuario> ListaUsuarios = new ArrayList<Usuario>();
    public static List<Embarcacion> ListaEmbarcaciones = new ArrayList<Embarcacion>();
    public static List<Destino> ListaDestinos = new ArrayList<Destino>();
    //Lista para guardar los viajes creados
    public static List<Viaje> ListaViajes = new ArrayList<>();
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/patagonia/Menu_Inicio.fxml"));
        primaryStage.setTitle("Patagonia Wellboat - Acceso");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
    }
    public static void main(String[] args) {
        // Intentamos cargar datos del archivo primero
        boolean cargado = GestorArchivos.cargarDatos(ListaUsuarios, ListaDestinos, ListaEmbarcaciones, ListaViajes);
        // Si no había archivo usamos tus datos por defecto
        if (!cargado) {
            cargarDatosIniciales();
            // Y guardamos de inmediato para crear el archivo
            GestorArchivos.guardarDatos(ListaUsuarios, ListaDestinos, ListaEmbarcaciones, ListaViajes);
        }
        launch(args);
    }
    private static void cargarDatosIniciales(){
        System.out.println("Cargando datos...");
        ListaUsuarios.add(new Usuario("admin", "admin123", "Administrador")); //contraseña por admin

        // lista de destinos
        ListaDestinos.add(new Destino("Calbuco", 15000, 4000));
        ListaDestinos.add(new Destino("Maillen", 15000, 4000));
        ListaDestinos.add(new Destino("Isla Puluqui", 20000,5000));
        ListaDestinos.add(new Destino("Achao", 25000, 6000));
        ListaDestinos.add(new Destino("Quinchao", 25000, 6000));
        ListaDestinos.add(new Destino("Dalcahue", 30000, 8000));
        ListaDestinos.add(new Destino("Curaco de Vélez", 30000, 8000));
        ListaDestinos.add(new Destino("Queilen", 40000, 10000));
        ListaDestinos.add(new Destino("Chonchi", 40000, 10000));
        ListaDestinos.add(new Destino("Melinka", 50000, 12000));
        ListaDestinos.add(new Destino("Quellón", 50000, 12000));
        //lista de barcos
        ListaEmbarcaciones.add(new CatamaranLiviano("CAT-001"));
        ListaEmbarcaciones.add(new FerryMediano("FER-002"));
        ListaEmbarcaciones.add(new WellboatGranCapacidad("WEL-003"));
        System.out.println("Datos cargados correctamente: " + ListaUsuarios.size() + " usuarios, " + ListaDestinos.size() + " destinos.");
    }
    @FXML
    void irVistaCliente(ActionEvent event) {
        try {
            // Cargar la vista de Login de Cliente (la verde)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patagonia/Menu_Cliente.fxml"));
            Parent root = loader.load();
            
            // Cambiar la escena
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Acceso Clientes - Patagonia Wellboat");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir Cliente: " + e.getMessage());
        }
    }
    
}