package controller;

import app.App;
import app.TauraManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class FXMLControlerApp {

    @FXML
    private Button btnCadastrarMembro;
    @FXML
    private Button btnCadastrarTarefa;
    @FXML
    private Button btnCadastrarEvento;
    @FXML
    private Button btnSalvarDados;
    @FXML
    private Button btnCarregarDados;
    @FXML
    private Pane painelAvisos;

    @FXML
    private VBox vboxTarefas;
    @FXML
    private VBox vboxEventos;
    @FXML
    private VBox vboxMembros;

    private TauraManager manager;

    public void setManager(TauraManager manager) {
        this.manager = manager;
        carregarDados(); // Carrega e exibe os dados ao inicializar
    }

    @FXML
    private void handleCadastrarMembro(ActionEvent event) throws Exception {
        System.out.println("Clicou em Cadastrar Membro");
        App.setScene("formMembro");
    }

    @FXML
    private void handleCadastrarTarefa(ActionEvent event) throws Exception {
        App.setScene("formTarefa");
    }

    @FXML
    private void handleCadastrarEvento(ActionEvent event) throws Exception {
        App.setScene("formEvento");
    }

    @FXML
    private void handleSalvarDados(ActionEvent event) {
        try {
            manager.salvarDados();
            System.out.println("Dados salvos com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCarregarDados(ActionEvent event) {
        carregarDados();
    }

    private void carregarDados() {
        try {
            manager.carregarDados();
            atualizarTelas();
            System.out.println("Dados carregados com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarTelas() {
        // Atualiza as VBox com dados do manager
        if (vboxMembros != null) {
            vboxMembros.getChildren().clear();
            // Adicionar elementos de membro dinamicamente
        }
        if (vboxTarefas != null) {
            vboxTarefas.getChildren().clear();
            // Adicionar elementos de tarefa dinamicamente
        }
        if (vboxEventos != null) {
            vboxEventos.getChildren().clear();
            // Adicionar elementos de evento dinamicamente
        }
    }
}