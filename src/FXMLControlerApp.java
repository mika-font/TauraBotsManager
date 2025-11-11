import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class FXMLControlerApp implements Initializable {
    private TauraManager Manager;

    @FXML
    private Button btn_salvar_dados;
    @FXML
    private Button btn_gerir_membros;
    @FXML
    private Button btn_gerir_atividades;
    @FXML
    private AnchorPane navegation;

    public void setManager(TauraManager manager) {
        this.Manager = manager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Manager = App.getManager();
    }

    @FXML
    public void handleSalvarDados(ActionEvent event) {
        if (Manager != null) {
            try {
                Manager.salvarDados();
                System.out.println("Dados salvos com sucesso.");
                // TODO: Adicionar feedback visual (ex: Label de sucesso)
            } catch (Exception e) {
                System.err.println("Erro ao salvar dados: " + e.getMessage());
                // TODO: Mostrar alerta de erro
            }
        }
    }

    @FXML
    public void handleCarregarDados(ActionEvent event) {
        if (Manager != null) {
            try {
                Manager.carregarDados();
                System.out.println("Dados carregados com sucesso. Membros: " + Manager.listarMembros().size());
                // TODO: Recarregar tabelas e listas na UI
            } catch (IllegalArgumentException e) {
                 System.out.println(e.getMessage()); // Ex: "Nenhum membro cadastrado."
            } catch (Exception e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
            }
        }
    }

    // Navegar para outras telas
    @FXML
    public void handleGerirMembros(ActionEvent event) {
        System.out.println("Navegando para Gerenciar Membros...");
        /*
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gerenciarMembros.fxml"));
            AnchorPane membroView = loader.load();
            MembroController controller = loader.getController();
            controller.setManager(this.manager);

            // Substitui o conteúdo do painel principal (por exemplo, dentro do FlowPane)
            // Aqui estamos apenas substituindo o TabPane por simplicidade de teste.
            // mainTabContent.getParent().getChildren().set(mainTabContent.getParent().getChildren().indexOf(mainTabContent), membroView);

        } catch (IOException e) {
             System.err.println("Não foi possível carregar a tela de membros: " + e.getMessage());
        }*/
    }

    @FXML
    public void handleGerirAtividades(ActionEvent event) {
        System.out.println("Navegando para Gerenciar Atividades...");
        // Similar ao handleGerirMembros, mas para atividades
    }

    @FXML
    public void handleSair(ActionEvent event) {
        System.out.println("Saindo da aplicação...");
        System.exit(0);
    }
}
