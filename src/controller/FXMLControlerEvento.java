package controller;

import app.App;
import app.TauraManager;
import model.Atividade;
import model.Evento;
import view.EventoTabData;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.SimpleDateFormat;

public class FXMLControlerEvento implements Initializable {
    private TauraManager Manager;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Campos de entrada
    @FXML private TextField txt_titulo;
    @FXML private TextArea txt_descricao;
    @FXML private TextField txt_local;
    @FXML private ComboBox<String> combo_status;
    @FXML private DatePicker date_prazo;
    @FXML private DatePicker date_inicio;
    @FXML private DatePicker date_fim;
    
    // Botões
    @FXML private Button btn_cadastrar;
    @FXML private Button btn_atualizar;
    @FXML private Button btn_remover;
    @FXML private Button btn_limpar;
    
    // Tabela
    @FXML private TableView<EventoTabData> table_eventos;
    @FXML private TableColumn<EventoTabData, Integer> col_id;
    @FXML private TableColumn<EventoTabData, String> col_titulo;
    @FXML private TableColumn<EventoTabData, String> col_local;
    @FXML private TableColumn<EventoTabData, String> col_status;
    @FXML private TableColumn<EventoTabData, String> col_prazo;
    @FXML private TableColumn<EventoTabData, String> col_inicio;
    @FXML private TableColumn<EventoTabData, String> col_fim;
    
    // Label de feedback
    @FXML private Label lbl_feedback;
    
    private ObservableList<EventoTabData> listaEventos;

    public void setManager(TauraManager manager) {
        this.Manager = manager;
        carregarEventosNaTabela();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializa o ComboBox com os status válidos
        combo_status.setItems(FXCollections.observableArrayList(
            "Pendente", "Em Progresso", "Concluído"
        ));
        
        // Configura as colunas da tabela
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col_local.setCellValueFactory(new PropertyValueFactory<>("local"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_prazo.setCellValueFactory(new PropertyValueFactory<>("prazo"));
        col_inicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        col_fim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        
        // Lista observável para a tabela
        listaEventos = FXCollections.observableArrayList();
        table_eventos.setItems(listaEventos);
        
        // Listener para seleção na tabela
        table_eventos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherCamposComEvento(newSelection);
                }
            }
        );
        
        // Tenta obter o Manager do App
        if (this.Manager == null) {
            this.Manager = App.getManager();
        }
        
        // Carrega eventos se Manager está disponível
        if (this.Manager != null) {
            carregarEventosNaTabela();
        }
    }

    @FXML
    public void handleCadastrar(ActionEvent event) {
        try {
            if (!validarCampos()) {
                return;
            }
            
            // Converte LocalDate para Date
            Date prazo = Date.from(date_prazo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dataInicio = Date.from(date_inicio.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dataFim = Date.from(date_fim.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Valida se data fim é depois da data início
            if (dataFim.before(dataInicio)) {
                mostrarFeedback("A data de fim não pode ser anterior à data de início.", "warning");
                return;
            }
            
            // Cria novo evento
            Evento novoEvento = new Evento(
                txt_titulo.getText().trim(),
                txt_descricao.getText().trim(),
                combo_status.getValue(),
                prazo,
                txt_local.getText().trim(),
                dataInicio,
                dataFim
            );
            
            // Adiciona ao Manager
            TauraManager.adicionarAtividade(novoEvento);
            
            // Atualiza a tabela
            carregarEventosNaTabela();
            
            // Limpa os campos
            limparCampos();
            
            mostrarFeedback("Evento cadastrado com sucesso!", "success");
            
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
            EventoTabData selecionado = table_eventos.getSelectionModel().getSelectedItem();
            
            if (selecionado == null) {
                mostrarFeedback("Selecione um evento na tabela para atualizar.", "warning");
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            // Busca o evento original
            Evento eventoOriginal = (Evento) TauraManager.buscarAtividade(selecionado.getId());
            
            // Converte LocalDate para Date
            Date prazo = Date.from(date_prazo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dataInicio = Date.from(date_inicio.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dataFim = Date.from(date_fim.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Valida data fim
            if (dataFim.before(dataInicio)) {
                mostrarFeedback("A data de fim não pode ser anterior à data de início.", "warning");
                return;
            }
            
            // Cria evento atualizado
            Evento eventoAtualizado = new Evento(
                txt_titulo.getText().trim(),
                txt_descricao.getText().trim(),
                combo_status.getValue(),
                prazo,
                txt_local.getText().trim(),
                dataInicio,
                dataFim
            );
            
            // Atualiza no Manager
            TauraManager.atualizarAtividade(eventoOriginal, eventoAtualizado);
            
            // Recarrega a tabela
            carregarEventosNaTabela();
            
            // Limpa campos
            limparCampos();
            
            mostrarFeedback("Evento atualizado com sucesso!", "success");
            
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
            EventoTabData selecionado = table_eventos.getSelectionModel().getSelectedItem();
            
            if (selecionado == null) {
                mostrarFeedback("Selecione um evento na tabela para remover.", "warning");
                return;
            }
            
            // Confirmação
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Remoção");
            confirmacao.setHeaderText("Remover Evento");
            confirmacao.setContentText("Tem certeza que deseja remover o evento '" + selecionado.getTitulo() + "'?");
            
            Optional<ButtonType> resultado = confirmacao.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Busca e remove o evento
                Evento evento = (Evento) TauraManager.buscarAtividade(selecionado.getId());
                TauraManager.removerAtividade(evento);
                
                // Recarrega a tabela
                carregarEventosNaTabela();
                
                // Limpa campos
                limparCampos();
                
                mostrarFeedback("Evento removido com sucesso!", "success");
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
        table_eventos.getSelectionModel().clearSelection();
        mostrarFeedback("", "");
    }

    private void carregarEventosNaTabela() {
        listaEventos.clear();
        
        try {
            var atividades = TauraManager.listarAtividades();
            for (Atividade atividade : atividades) {
                if (atividade instanceof Evento) {
                    Evento evento = (Evento) atividade;
                    listaEventos.add(new EventoTabData(evento));
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Nenhum evento para carregar: " + e.getMessage());
        } catch (Exception e) {
            mostrarFeedback("Erro ao carregar eventos: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void preencherCamposComEvento(EventoTabData evento) {
        try {
            Evento eventoCompleto = (Evento) TauraManager.buscarAtividade(evento.getId());
            
            txt_titulo.setText(eventoCompleto.getTitulo());
            txt_descricao.setText(eventoCompleto.getDescricao());
            txt_local.setText(eventoCompleto.getLocal());
            combo_status.setValue(eventoCompleto.getStatus());
            
            // Converte Date para LocalDate
            date_prazo.setValue(eventoCompleto.getPrazo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            date_inicio.setValue(eventoCompleto.getDataInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            date_fim.setValue(eventoCompleto.getDataFim().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            
        } catch (Exception e) {
            mostrarFeedback("Erro ao carregar dados do evento: " + e.getMessage(), "error");
        }
    }

    private void limparCampos() {
        txt_titulo.clear();
        txt_descricao.clear();
        txt_local.clear();
        combo_status.setValue(null);
        date_prazo.setValue(null);
        date_inicio.setValue(null);
        date_fim.setValue(null);
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
        
        if (txt_local.getText().trim().isEmpty()) {
            mostrarFeedback("O local não pode ser vazio.", "warning");
            return false;
        }
        
        if (combo_status.getValue() == null) {
            mostrarFeedback("Selecione um status.", "warning");
            return false;
        }
        
        if (date_prazo.getValue() == null) {
            mostrarFeedback("Selecione a data do prazo.", "warning");
            return false;
        }
        
        if (date_inicio.getValue() == null) {
            mostrarFeedback("Selecione a data de início.", "warning");
            return false;
        }
        
        if (date_fim.getValue() == null) {
            mostrarFeedback("Selecione a data de fim.", "warning");
            return false;
        }
        
        return true;
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