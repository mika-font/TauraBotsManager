package controller;

import app.App;
import app.TauraManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXMLControlerApp implements Initializable {
    private TauraManager Manager;

    @FXML private Button btn_salvar_dados;
    @FXML private Button btn_carregar_dados;
    @FXML private Button btn_cadastrar_membro;
    @FXML private Button btn_cadastrar_evento;
    @FXML private Button btn_cadastrar_tarefa;
    @FXML private Button btn_sair;
    @FXML private AnchorPane navegation;

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
                TauraManager.salvarDados();
                
                // Mostra alerta de sucesso
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("Dados Salvos");
                alert.setContentText("Os dados foram salvos com sucesso!");
                alert.showAndWait();
                
                System.out.println("Dados salvos com sucesso.");
            } catch (Exception e) {
                // Mostra alerta de erro
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao Salvar");
                alert.setContentText("Não foi possível salvar os dados: " + e.getMessage());
                alert.showAndWait();
                
                System.err.println("Erro ao salvar dados: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Manager não inicializado");
            alert.setContentText("O sistema não está pronto. Tente reiniciar a aplicação.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCarregarDados(ActionEvent event) {
        if (Manager != null) {
            try {
                // Confirmação antes de carregar (vai sobrescrever dados atuais)
                Alert confirmacao = new Alert(AlertType.CONFIRMATION);
                confirmacao.setTitle("Confirmar Carregamento");
                confirmacao.setHeaderText("Carregar Dados");
                confirmacao.setContentText("Isso irá substituir os dados atuais pelos dados salvos. Deseja continuar?");
                
                Optional<ButtonType> resultado = confirmacao.showAndWait();
                
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    TauraManager.carregarDados();
                    
                    // Atualiza as telas abertas
                    atualizarTelaAtual();
                    
                    // Mostra sucesso
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Dados Carregados");
                    alert.setContentText("Os dados foram carregados com sucesso!\n" +
                                       "Membros: " + (Manager.listarMembros() != null ? Manager.listarMembros().size() : 0));
                    alert.showAndWait();
                    
                    System.out.println("Dados carregados com sucesso.");
                }
            } catch (IllegalArgumentException e) {
                // Nenhum dado para carregar
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Informação");
                alert.setHeaderText("Sem Dados");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                
                System.out.println(e.getMessage());
            } catch (Exception e) {
                // Erro ao carregar
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao Carregar");
                alert.setContentText("Não foi possível carregar os dados: " + e.getMessage());
                alert.showAndWait();
                
                System.err.println("Erro ao carregar dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleCadastrarMembro(ActionEvent event) {
        System.out.println("Navegando para Cadastrar Membro...");
        carregarTela("/resources/formMembro.fxml", "Membros");
    }

    @FXML
    public void handleCadastrarEvento(ActionEvent event) {
        System.out.println("Navegando para Cadastrar Evento...");
        carregarTela("/resources/formEvento.fxml", "Eventos");
    }

    @FXML
    public void handleCadastrarTarefa(ActionEvent event) {
        System.out.println("Navegando para Cadastrar Tarefa...");
        carregarTela("/resources/formTarefa.fxml", "Tarefas");
    }

    @FXML
    public void handleSair(ActionEvent event) {
        // Confirmação antes de sair
        Alert confirmacao = new Alert(AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Saída");
        confirmacao.setHeaderText("Sair da Aplicação");
        confirmacao.setContentText("Deseja salvar os dados antes de sair?");
        
        ButtonType btnSalvarSair = new ButtonType("Salvar e Sair");
        ButtonType btnSairSemSalvar = new ButtonType("Sair sem Salvar");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        confirmacao.getButtonTypes().setAll(btnSalvarSair, btnSairSemSalvar, btnCancelar);
        
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        
        if (resultado.isPresent()) {
            if (resultado.get() == btnSalvarSair) {
                try {
                    TauraManager.salvarDados();
                    System.out.println("Dados salvos antes de sair.");
                } catch (Exception e) {
                    System.err.println("Erro ao salvar dados na saída: " + e.getMessage());
                }
                System.exit(0);
            } else if (resultado.get() == btnSairSemSalvar) {
                System.out.println("Saindo sem salvar...");
                System.exit(0);
            }
            // Se cancelar, não faz nada
        }
    }

    /**
     * Carrega uma tela no painel de navegação
     */
    private void carregarTela(String fxmlPath, String nomeTela) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane view = loader.load();
            
            // Obtém o controller e configura o Manager
            Object controller = loader.getController();
            configurarController(controller);
            
            // Substitui o conteúdo do painel de navegação
            navegation.getChildren().setAll(view);
            
            // Ajusta o tamanho da view para preencher o painel
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            
            System.out.println("Tela carregada: " + nomeTela);
            
        } catch (IOException e) {
            System.err.println("Não foi possível carregar a tela " + nomeTela + ": " + e.getMessage());
            e.printStackTrace();
            
            // Mostra alerta de erro
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao Carregar Tela");
            alert.setContentText("Não foi possível carregar a tela de " + nomeTela + ".\n" +
                               "Verifique se o arquivo FXML existe: " + fxmlPath);
            alert.showAndWait();
        }
    }

    /**
     * Configura o Manager no controller carregado
     */
    private void configurarController(Object controller) {
        if (controller instanceof FXMLControlerMembro) {
            ((FXMLControlerMembro) controller).setManager(Manager);
        } else if (controller instanceof FXMLControlerEvento) {
            ((FXMLControlerEvento) controller).setManager(Manager);
        } else if (controller instanceof FXMLControlerTarefa) {
            ((FXMLControlerTarefa) controller).setManager(Manager);
        }
    }

    /**
     * Atualiza a tela atualmente carregada (usado após carregar dados)
     */
    private void atualizarTelaAtual() {
        // Recarrega a tela atual
        if (!navegation.getChildren().isEmpty()) {
            // Identifica qual tela está aberta e recarrega
            // Esta é uma solução simples - você pode melhorar guardando referência ao controller atual
            Object controller = navegation.getUserData();
            if (controller instanceof FXMLControlerMembro) {
                handleCadastrarMembro(null);
            } else if (controller instanceof FXMLControlerEvento) {
                handleCadastrarEvento(null);
            } else if (controller instanceof FXMLControlerTarefa) {
                handleCadastrarTarefa(null);
            }
        }
    }
}