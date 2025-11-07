// JAVA FX IMPORTS
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {
    // private static final String FXML_FILE_PATH = "/fxml/MainView.fxml";
    private static final String APP_TITLE = "Taura Bots Manager";
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 600;

    private static TauraManager manager;

    // Interface Gráfica com JavaFX
    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        manager = new TauraManager(); // Inicializa o gerenciador de dados

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setWidth(APP_WIDTH);
        primaryStage.setHeight(APP_HEIGHT);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("projetoGUI.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static TauraManager getManager() {
        return manager;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}