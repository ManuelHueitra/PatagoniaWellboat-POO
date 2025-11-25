package patagonia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
    public static List<Usuario> ListaUsuarios = new ArrayList<Usuario>();
    public static List<Embarcacion> ListaEmbarcaciones = new ArrayList<Embarcacion>();
    public static List<Destino> ListaDestinos = new ArrayList<Destino>();

    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/patagonia/Login.fxml"));
        primaryStage.setTitle("Patagonia Wellboat - Acceso");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    public static void main(String[] args)
    {
        cargarDatosIniciales(); // carga datos iniciales
        launch(args); // inicia la aplicación JavaFX
    }
    private static void cargarDatosIniciales(){
        System.out.println("Cargando datos...");
        ListaUsuarios.add(new Usuario("admin", "admin123", "Administrador"));

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
}