package app;

import controller.FXMLControlerApp;
import controller.FXMLControlerEvento;
import controller.FXMLControlerMembro;
import controller.FXMLControlerTarefa;

// JAVA FX IMPORTS
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;

public class App extends Application {
    private static final String FXML_FILE_CENTRAL = "resources/central.fxml";
    private static final String FXML_FILE_MEMBRO = "resources/formMembro.fxml";
    private static final String FXML_FILE_TAREFA = "resources/formTarefa.fxml";
    private static final String FXML_FILE_EVENTO = "resources/formEvento.fxml";
    private static final String APP_TITLE = "Taura Bots Manager";
    private static final String ICON_FILE_PATH = "/resources/logo_taura.png";

    private static TauraManager Manager;

    private static Stage Stage;
    private static Scene central;
    private static Scene formMembro;
    private static Scene formTarefa;
    private static Scene formEvento;

    private static FXMLControlerApp controllerApp;
    private static FXMLControlerMembro controllerMembro;
    private static FXMLControlerTarefa controllerTarefa;
    private static FXMLControlerEvento controllerEvento;

    // Interface Gráfica com JavaFX
    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        Stage = primaryStage;
        Manager = new TauraManager();
        primaryStage.setTitle(APP_TITLE);

        try {
            primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream(ICON_FILE_PATH)));
        } catch (Exception e) {
            System.err.println("Ícone não encontrado: " + e.getMessage());
        }

        // Cache de Cenas
        FXMLLoader loaderCentral = new FXMLLoader(
            getClass().getClassLoader().getResource(FXML_FILE_CENTRAL)
        );
        Parent centralRoot = loaderCentral.load();
        controllerApp = loaderCentral.getController();
        controllerApp.setManager(Manager);
        central = new Scene(centralRoot, 900, 600);

        FXMLLoader loaderMembro = new FXMLLoader(
            getClass().getClassLoader().getResource(FXML_FILE_MEMBRO)
        );
        Parent membroRoot = loaderMembro.load();
        controllerMembro = loaderMembro.getController();
        controllerMembro.setManager(Manager);
        formMembro = new Scene(membroRoot, 700, 550);

        FXMLLoader loaderTarefa = new FXMLLoader(
            getClass().getClassLoader().getResource(FXML_FILE_TAREFA)
        );
        Parent tarefaRoot = loaderTarefa.load();
        controllerTarefa = loaderTarefa.getController();
        controllerTarefa.setManager(Manager);
        formTarefa = new Scene(tarefaRoot, 920, 650);

        FXMLLoader loaderEvento = new FXMLLoader(
            getClass().getClassLoader().getResource(FXML_FILE_EVENTO)
        );
        Parent eventoRoot = loaderEvento.load();
        controllerEvento = loaderEvento.getController();
        controllerEvento.setManager(Manager);
        formEvento = new Scene(eventoRoot, 800, 600);

        primaryStage.setScene(central);

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Salvando dados antes de sair...");
            try {
                Manager.salvarDados();
                System.out.println("Dados salvos com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao salvar dados na saída: " + e.getMessage());
            }
        });
        primaryStage.show();
    }

    public static void setScene(String str) {
        switch (str) {
            case "central":
                Stage.setScene(central);
                Stage.sizeToScene();
                Stage.centerOnScreen();
                break;
            case "formMembro":
                Stage.setScene(formMembro);
                Stage.sizeToScene();
                Stage.centerOnScreen();
                break;
            case "formTarefa":
                Stage.setScene(formTarefa);
                Stage.sizeToScene();
                Stage.centerOnScreen();
                break;
            case "formEvento":
                Stage.setScene(formEvento);
                Stage.sizeToScene();
                Stage.centerOnScreen();
                break;
            default:
                System.err.println("Cena desconhecida: " + str);
        }
    }

    public static TauraManager getManager() {
        return Manager;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}