package controller;

import app.App;
import app.TauraManager;
import model.Atividade;
import model.Tarefa;
import model.Membro;
import view.TarefaTabData;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;

public class FXMLControlerTarefa implements Initializable {
    private TauraManager Manager;

    // Campos de entrada
    @FXML
    private TextField txt_titulo;
    @FXML
    private TextArea txt_descricao;
    @FXML
    private ComboBox<String> combo_status;
    @FXML
    private ComboBox<String> combo_tipo;
    @FXML
    private DatePicker date_prazo;
    @FXML
    private ListView<String> list_membros_disponiveis;
    @FXML
    private ListView<String> list_responsaveis;

    // Botões
    @FXML
    private Button btn_adicionar_responsavel;
    @FXML
    private Button btn_remover_responsavel;
    @FXML
    private Button btn_cadastrar;
    @FXML
    private Button btn_atualizar;
    @FXML
    private Button btn_remover;
    @FXML
    private Button btn_limpar;

    // Tabela
    @FXML
    private TableView<TarefaTabData> table_tarefas;
    @FXML
    private TableColumn<TarefaTabData, Integer> col_id;
    @FXML
    private TableColumn<TarefaTabData, String> col_titulo;
    @FXML
    private TableColumn<TarefaTabData, String> col_tipo;
    @FXML
    private TableColumn<TarefaTabData, String> col_status;
    @FXML
    private TableColumn<TarefaTabData, String> col_prazo;
    @FXML
    private TableColumn<TarefaTabData, String> col_responsaveis;

    // Label de feedback
    @FXML
    private Label lbl_feedback;

    private ObservableList<TarefaTabData> listaTarefas;
    private ObservableList<String> membrosDisponiveis;
    private ObservableList<String> responsaveisSelecionados;

    // Mapeia matrícula -> nome para facilitar conversões
    private java.util.Map<String, Membro> mapaMatriculaMembro;

    public void setManager(TauraManager manager) {
        this.Manager = manager;
        carregarMembrosDisponiveis();
        carregarTarefasNaTabela();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializa os ComboBoxes
        combo_status.setItems(FXCollections.observableArrayList(
                "Pendente", "Em Progresso", "Concluído"));

        combo_tipo.setItems(FXCollections.observableArrayList(
                "Limpeza", "Manutenção", "Projeto"));

        // Configura as colunas da tabela
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col_tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_prazo.setCellValueFactory(new PropertyValueFactory<>("prazo"));
        col_responsaveis.setCellValueFactory(new PropertyValueFactory<>("responsaveis"));

        // Lista observável para a tabela
        listaTarefas = FXCollections.observableArrayList();
        table_tarefas.setItems(listaTarefas);

        // Listas para membros
        membrosDisponiveis = FXCollections.observableArrayList();
        responsaveisSelecionados = FXCollections.observableArrayList();

        list_membros_disponiveis.setItems(membrosDisponiveis);
        list_responsaveis.setItems(responsaveisSelecionados);

        // Permite seleção múltipla nas listas
        list_membros_disponiveis.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list_responsaveis.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Listener para seleção na tabela
        table_tarefas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        preencherCamposComTarefa(newSelection);
                    }
                });

        // Inicializa mapa
        mapaMatriculaMembro = new java.util.HashMap<>();

        // Tenta obter o Manager do App
        if (this.Manager == null) {
            this.Manager = App.getManager();
        }

        // Carrega dados se Manager está disponível
        if (this.Manager != null) {
            carregarMembrosDisponiveis();
            carregarTarefasNaTabela();
        }
    }

    @FXML
    public void handleVoltar(ActionEvent event) {
        App.setScene("central");
    }

    @FXML
    public void handleAdicionarResponsavel(ActionEvent event) {
        var selecionados = list_membros_disponiveis.getSelectionModel().getSelectedItems();

        if (selecionados.isEmpty()) {
            mostrarFeedback("Selecione pelo menos um membro para adicionar.", "warning");
            return;
        }

        for (String membro : selecionados) {
            if (!responsaveisSelecionados.contains(membro)) {
                responsaveisSelecionados.add(membro);
            }
        }

        list_membros_disponiveis.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleRemoverResponsavel(ActionEvent event) {
        var selecionados = list_responsaveis.getSelectionModel().getSelectedItems();

        if (selecionados.isEmpty()) {
            mostrarFeedback("Selecione pelo menos um responsável para remover.", "warning");
            return;
        }

        if (responsaveisSelecionados.size() - selecionados.size() < 1) {
            mostrarFeedback("A tarefa deve ter pelo menos um responsável.", "warning");
            return;
        }

        responsaveisSelecionados.removeAll(selecionados);
        list_responsaveis.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleCadastrar(ActionEvent event) {
        try {
            if (!validarCampos()) {
                return;
            }

            // Converte LocalDate para Date
            Date prazo = Date.from(date_prazo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Busca os membros responsáveis
            List<Membro> membrosResponsaveis = new ArrayList<>();
            for (String membroStr : responsaveisSelecionados) {
                String matricula = extrairMatricula(membroStr);
                Membro membro = mapaMatriculaMembro.get(matricula);
                if (membro != null) {
                    membrosResponsaveis.add(membro);
                }
            }

            if (membrosResponsaveis.isEmpty()) {
                mostrarFeedback("Adicione pelo menos um responsável.", "warning");
                return;
            }

            // Cria nova tarefa
            Tarefa novaTarefa = new Tarefa(
                    txt_titulo.getText().trim(),
                    txt_descricao.getText().trim(),
                    combo_status.getValue(),
                    prazo,
                    membrosResponsaveis,
                    combo_tipo.getValue());

            // Adiciona ao Manager
            TauraManager.adicionarAtividade(novaTarefa);

            // Atualiza a tabela
            carregarTarefasNaTabela();

            // Limpa os campos
            limparCampos();

            mostrarFeedback("Tarefa cadastrada com sucesso!", "success");

        } catch (IllegalArgumentException e) {
            mostrarFeedback("Erro: " + e.getMessage(), "error");
        } catch (Exception e) {
            mostrarFeedback("Erro inesperado: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAtualizar(ActionEvent event) {
        try {
            TarefaTabData selecionado = table_tarefas.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                mostrarFeedback("Selecione uma tarefa na tabela para atualizar.", "warning");
                return;
            }

            if (!validarCampos()) {
                return;
            }

            // Busca a tarefa original
            Tarefa tarefaOriginal = (Tarefa) TauraManager.buscarAtividade(selecionado.getId());

            // Converte LocalDate para Date
            Date prazo = Date.from(date_prazo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Busca os membros responsáveis
            List<Membro> membrosResponsaveis = new ArrayList<>();
            for (String membroStr : responsaveisSelecionados) {
                String matricula = extrairMatricula(membroStr);
                Membro membro = mapaMatriculaMembro.get(matricula);
                if (membro != null) {
                    membrosResponsaveis.add(membro);
                }
            }

            if (membrosResponsaveis.isEmpty()) {
                mostrarFeedback("Adicione pelo menos um responsável.", "warning");
                return;
            }

            // Cria tarefa atualizada
            Tarefa tarefaAtualizada = new Tarefa(
                    txt_titulo.getText().trim(),
                    txt_descricao.getText().trim(),
                    combo_status.getValue(),
                    prazo,
                    membrosResponsaveis,
                    combo_tipo.getValue());

            // Atualiza no Manager
            TauraManager.atualizarAtividade(tarefaOriginal, tarefaAtualizada);

            // Recarrega a tabela
            carregarTarefasNaTabela();

            // Limpa campos
            limparCampos();

            mostrarFeedback("Tarefa atualizada com sucesso!", "success");

        } catch (IllegalArgumentException e) {
            mostrarFeedback("Erro: " + e.getMessage(), "error");
        } catch (Exception e) {
            mostrarFeedback("Erro inesperado: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRemover(ActionEvent event) {
        try {
            TarefaTabData selecionado = table_tarefas.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                mostrarFeedback("Selecione uma tarefa na tabela para remover.", "warning");
                return;
            }

            // Confirmação
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Remoção");
            confirmacao.setHeaderText("Remover Tarefa");
            confirmacao.setContentText("Tem certeza que deseja remover a tarefa '" + selecionado.getTitulo() + "'?");

            Optional<ButtonType> resultado = confirmacao.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Busca e remove a tarefa
                Tarefa tarefa = (Tarefa) TauraManager.buscarAtividade(selecionado.getId());
                TauraManager.removerAtividade(tarefa);

                // Recarrega a tabela
                carregarTarefasNaTabela();

                // Limpa campos
                limparCampos();

                mostrarFeedback("Tarefa removida com sucesso!", "success");
            }

        } catch (IllegalArgumentException e) {
            mostrarFeedback("Erro: " + e.getMessage(), "error");
        } catch (Exception e) {
            mostrarFeedback("Erro inesperado: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLimpar(ActionEvent event) {
        limparCampos();
        table_tarefas.getSelectionModel().clearSelection();
        mostrarFeedback("", "");
    }

    private void carregarMembrosDisponiveis() {
        membrosDisponiveis.clear();
        mapaMatriculaMembro.clear();

        try {
            var membros = TauraManager.listarMembros();
            for (Membro membro : membros) {
                String display = membro.getNome() + " (" + membro.getMatricula() + ")";
                membrosDisponiveis.add(display);
                mapaMatriculaMembro.put(String.valueOf(membro.getMatricula()), membro);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Nenhum membro para carregar: " + e.getMessage());
        } catch (Exception e) {
            mostrarFeedback("Erro ao carregar membros: " + e.getMessage(), "error");
        }
    }

    private void carregarTarefasNaTabela() {
        listaTarefas.clear();

        try {
            var atividades = TauraManager.listarAtividades();
            for (Atividade atividade : atividades) {
                if (atividade instanceof Tarefa) {
                    Tarefa tarefa = (Tarefa) atividade;
                    listaTarefas.add(new TarefaTabData(tarefa));
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Nenhuma tarefa para carregar: " + e.getMessage());
        } catch (Exception e) {
            mostrarFeedback("Erro ao carregar tarefas: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void preencherCamposComTarefa(TarefaTabData tarefa) {
        try {
            Tarefa tarefaCompleta = (Tarefa) TauraManager.buscarAtividade(tarefa.getId());

            txt_titulo.setText(tarefaCompleta.getTitulo());
            txt_descricao.setText(tarefaCompleta.getDescricao());
            combo_status.setValue(tarefaCompleta.getStatus());
            combo_tipo.setValue(tarefaCompleta.getTipo());
            date_prazo.setValue(tarefaCompleta.getPrazo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            // Preenche responsáveis
            responsaveisSelecionados.clear();
            for (Membro membro : tarefaCompleta.getResponsaveis()) {
                String display = membro.getNome() + " (" + membro.getMatricula() + ")";
                responsaveisSelecionados.add(display);
            }

        } catch (Exception e) {
            mostrarFeedback("Erro ao carregar dados da tarefa: " + e.getMessage(), "error");
        }
    }

    private void limparCampos() {
        txt_titulo.clear();
        txt_descricao.clear();
        combo_status.setValue(null);
        combo_tipo.setValue(null);
        date_prazo.setValue(null);
        responsaveisSelecionados.clear();
    }

    private boolean validarCampos() {
        if (txt_titulo.getText().trim().isEmpty()) {
            mostrarFeedback("O título não pode ser vazio.", "warning");
            return false;
        }

        if (txt_descricao.getText().trim().isEmpty()) {
            mostrarFeedback("A descrição não pode ser vazia.", "warning");
            return false;
        }

        if (combo_status.getValue() == null) {
            mostrarFeedback("Selecione um status.", "warning");
            return false;
        }

        if (combo_tipo.getValue() == null) {
            mostrarFeedback("Selecione um tipo.", "warning");
            return false;
        }

        if (date_prazo.getValue() == null) {
            mostrarFeedback("Selecione a data do prazo.", "warning");
            return false;
        }

        if (responsaveisSelecionados.isEmpty()) {
            mostrarFeedback("Adicione pelo menos um responsável.", "warning");
            return false;
        }

        return true;
    }

    private String extrairMatricula(String membroDisplay) {
        // Formato: "Nome (matricula)"
        int inicio = membroDisplay.lastIndexOf("(");
        int fim = membroDisplay.lastIndexOf(")");
        if (inicio != -1 && fim != -1) {
            return membroDisplay.substring(inicio + 1, fim);
        }
        return "";
    }

    private void mostrarFeedback(String mensagem, String tipo) {
        if (lbl_feedback != null) {
            lbl_feedback.setText(mensagem);
            lbl_feedback.getStyleClass().removeAll("feedback-success", "feedback-error", "feedback-warning");

            switch (tipo) {
                case "success":
                    lbl_feedback.getStyleClass().add("feedback-success");
                    break;
                case "error":
                    lbl_feedback.getStyleClass().add("feedback-error");
                    break;
                case "warning":
                    lbl_feedback.getStyleClass().add("feedback-warning");
                    break;
            }
        } else {
            System.out.println("[" + tipo.toUpperCase() + "] " + mensagem);
        }
    }
}