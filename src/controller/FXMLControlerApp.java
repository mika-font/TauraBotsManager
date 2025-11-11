package controller;

import app.App;
import app.TauraManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import model.Atividade;
import model.Evento;
import model.Membro;
import model.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;

public class FXMLControlerApp {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML private Button btnCadastrarMembro;
    @FXML private Button btnCadastrarTarefa;
    @FXML private Button btnCadastrarEvento;
    @FXML private Button btnSalvarDados;
    @FXML private Button btnCarregarDados;
    @FXML private Pane painelAvisos;

    @FXML private VBox vboxTarefas;
    @FXML private VBox vboxEventos;
    @FXML private VBox vboxMembros;

    private TauraManager manager;

    public void setManager(TauraManager manager) {
        this.manager = manager;
        carregarDados();
    }

    @FXML
    private void handleCadastrarMembro(ActionEvent event) throws Exception {
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
            TauraManager.salvarDados();
            System.out.println("Dados salvos com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    @FXML
    private void handleCarregarDados(ActionEvent event) {
        carregarDados();
    }

    private void carregarDados() {
        try {
            TauraManager.carregarDados();
            atualizarTelas();
            System.out.println("Dados carregados com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    public void atualizarTelas() {
        atualizarMembros();
        atualizarTarefas();
        atualizarEventos();
    }

    private void atualizarMembros() {
        if (vboxMembros == null) return;
        vboxMembros.getChildren().clear();

        try {
            List<Membro> listaMembros = TauraManager.listarMembros();
            
            if (listaMembros.isEmpty()) {
                Label lblVazio = new Label("Nenhum membro cadastrado.");
                lblVazio.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                vboxMembros.getChildren().add(lblVazio);
                return;
            }

            for (Membro membro : listaMembros) {
                VBox card = criarCardMembro(membro);
                vboxMembros.getChildren().add(card);
            }
        } catch (Exception e) {
            Label lblVazio = new Label("Nenhum membro cadastrado.");
            lblVazio.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            vboxMembros.getChildren().add(lblVazio);
        }
    }

    private void atualizarTarefas() {
        if (vboxTarefas == null) return;
        vboxTarefas.getChildren().clear();

        try {
            List<Atividade> listaAtividades = TauraManager.listarAtividades();
            List<Tarefa> listaTarefas = listaAtividades.stream()
                .filter(a -> a instanceof Tarefa)
                .map(a -> (Tarefa) a)
                .toList();

            if (listaTarefas.isEmpty()) {
                Label lblVazio = new Label("Nenhuma tarefa cadastrada.");
                lblVazio.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                vboxTarefas.getChildren().add(lblVazio);
                return;
            }

            for (Tarefa tarefa : listaTarefas) {
                VBox card = criarCardTarefa(tarefa);
                vboxTarefas.getChildren().add(card);
            }
        } catch (Exception e) {
            Label lblVazio = new Label("Nenhuma tarefa cadastrada.");
            lblVazio.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            vboxTarefas.getChildren().add(lblVazio);
        }
    }

    private void atualizarEventos() {
        if (vboxEventos == null) return;
        vboxEventos.getChildren().clear();

        try {
            List<Atividade> listaAtividades = TauraManager.listarAtividades();
            List<Evento> listaEventos = listaAtividades.stream()
                .filter(a -> a instanceof Evento)
                .map(a -> (Evento) a)
                .toList();

            if (listaEventos.isEmpty()) {
                Label lblVazio = new Label("Nenhum evento cadastrado.");
                lblVazio.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                vboxEventos.getChildren().add(lblVazio);
                return;
            }

            for (Evento evento : listaEventos) {
                VBox card = criarCardEvento(evento);
                vboxEventos.getChildren().add(card);
            }
        } catch (Exception e) {
            Label lblVazio = new Label("Nenhum evento cadastrado.");
            lblVazio.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
            vboxEventos.getChildren().add(lblVazio);
        }
    }

    private VBox criarCardMembro(Membro membro) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        Label lblNome = new Label(membro.getNome());
        lblNome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label lblEmail = new Label("📧 " + membro.getEmail());
        Label lblTelefone = new Label("📱 " + membro.getTelefone());
        Label lblMatricula = new Label("🆔 Matrícula: " + membro.getMatricula());
        Label lblCargo = new Label("💼 " + membro.getCargo());

        card.getChildren().addAll(lblNome, lblEmail, lblTelefone, lblMatricula, lblCargo);
        return card;
    }

    private VBox criarCardTarefa(Tarefa tarefa) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        Label lblTitulo = new Label(tarefa.getTitulo());
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label lblDescricao = new Label(tarefa.getDescricao());
        lblDescricao.setWrapText(true);
        lblDescricao.setMaxWidth(650);
        lblDescricao.setStyle("-fx-text-fill: #555;");

        HBox infoBox = new HBox(20);
        Label lblStatus = new Label("📊 " + tarefa.getStatus());
        Label lblTipo = new Label("🏷️ " + tarefa.getTipo());
        Label lblPrazo = new Label("📅 " + dateFormat.format(tarefa.getPrazo()));
        infoBox.getChildren().addAll(lblStatus, lblTipo, lblPrazo);

        Label lblResponsaveis = new Label("👥 Responsáveis: " + tarefa.getNomesResponsaveis());
        lblResponsaveis.setWrapText(true);
        lblResponsaveis.setMaxWidth(650);

        card.getChildren().addAll(lblTitulo, lblDescricao, infoBox, lblResponsaveis);
        return card;
    }

    private VBox criarCardEvento(Evento evento) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        Label lblTitulo = new Label(evento.getTitulo());
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label lblDescricao = new Label(evento.getDescricao());
        lblDescricao.setWrapText(true);
        lblDescricao.setMaxWidth(650);
        lblDescricao.setStyle("-fx-text-fill: #555;");

        Label lblLocal = new Label("📍 " + evento.getLocal());

        HBox infoBox = new HBox(20);
        Label lblStatus = new Label("📊 " + evento.getStatus());
        Label lblPrazo = new Label("📅 Prazo: " + dateFormat.format(evento.getPrazo()));
        infoBox.getChildren().addAll(lblStatus, lblPrazo);

        HBox datasBox = new HBox(20);
        Label lblInicio = new Label("🟢 Início: " + dateFormat.format(evento.getDataInicio()));
        Label lblFim = new Label("🔴 Fim: " + dateFormat.format(evento.getDataFim()));
        datasBox.getChildren().addAll(lblInicio, lblFim);

        card.getChildren().addAll(lblTitulo, lblDescricao, lblLocal, infoBox, datasBox);
        return card;
    }
}