// JAVA FX IMPORTS
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;

public class App extends Application {
    private static final String FXML_FILE_PATH = "/resources/central.fxml";
    private static final String APP_TITLE = "Taura Bots Manager";
    // private static final String CSS_FILE_PATH = "/resources/style.css";
    private static final String ICON_FILE_PATH = "/resources/logo_taura.png";

    private static TauraManager Manager;

    // Interface Gráfica com JavaFX
    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        Manager = new TauraManager(); // Inicializa o gerenciador de dados

        primaryStage.setTitle(APP_TITLE);
        primaryStage.getIcons().add(new javafx.scene.image.Image(ICON_FILE_PATH));

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_PATH));
        Parent root = loader.load();
        FXMLControlerApp controller = loader.getController();
        controller.setManager(Manager);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        /*primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Salvando dados antes de sair...");
            try {
                Manager.salvarDados();
                System.out.println("Dados salvos com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao salvar dados na saída: " + e.getMessage());
            }
        });*/

        primaryStage.show();
    }

    public static TauraManager getManager() {
        return Manager;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}