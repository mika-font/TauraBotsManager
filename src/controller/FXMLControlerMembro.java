package controller;

import app.App;
import app.TauraManager;
import model.Membro;
import view.MembroTabData;

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

public class FXMLControlerMembro implements Initializable {
    private TauraManager Manager;

    // Campos de entrada
    @FXML private TextField txt_nome;
    @FXML private TextField txt_email;
    @FXML private TextField txt_telefone;
    @FXML private TextField txt_matricula;
    @FXML private ComboBox<String> combo_cargo;
    
    // Botões
    @FXML private Button btn_cadastrar;
    @FXML private Button btn_atualizar;
    @FXML private Button btn_remover;
    @FXML private Button btn_limpar;
    
    // Tabela
    @FXML private TableView<MembroTabData> table_membros;
    @FXML private TableColumn<MembroTabData, String> col_nome;
    @FXML private TableColumn<MembroTabData, String> col_email;
    @FXML private TableColumn<MembroTabData, String> col_telefone;
    @FXML private TableColumn<MembroTabData, String> col_cargo;
    @FXML private TableColumn<MembroTabData, Integer> col_matricula;
    
    // Label de feedback
    @FXML private Label lbl_feedback;
    
    private ObservableList<MembroTabData> listaMembros;
    private Membro membroSelecionado;

    public void setManager(TauraManager manager) {
        this.Manager = manager;
        carregarMembrosNaTabela();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializa o ComboBox com os cargos válidos
        combo_cargo.setItems(FXCollections.observableArrayList(
            "Membro Padrão", "Capitão", "Financeiro"
        ));
        
        // Configura as colunas da tabela
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        col_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        col_matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        
        // Lista observável para a tabela
        listaMembros = FXCollections.observableArrayList();
        table_membros.setItems(listaMembros);
        
        // Listener para seleção na tabela
        table_membros.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherCamposComMembro(newSelection);
                }
            }
        );
        
        // Tenta obter o Manager do App
        if (this.Manager == null) {
            this.Manager = App.getManager();
        }
        
        // Carrega membros se Manager está disponível
        if (this.Manager != null) {
            carregarMembrosNaTabela();
        }
    }

    @FXML
    public void handleCadastrar(ActionEvent event) {
        try {
            // Valida campos vazios
            if (!validarCampos()) {
                return;
            }
            
            // Cria novo membro
            String nome = txt_nome.getText().trim();
            String email = txt_email.getText().trim();
            String telefone = txt_telefone.getText().trim();
            String cargo = combo_cargo.getValue();
            int matricula = Integer.parseInt(txt_matricula.getText().trim());
            
            Membro novoMembro = new Membro(nome, email, telefone, cargo, matricula);
            
            // Adiciona ao Manager
            TauraManager.adicionarMembro(novoMembro);
            
            // Atualiza a tabela
            carregarMembrosNaTabela();
            
            // Limpa os campos
            limparCampos();
            
            // Feedback de sucesso
            mostrarFeedback("Membro cadastrado com sucesso!", "success");
            
        } catch (NumberFormatException e) {
            mostrarFeedback("Matrícula deve ser um número válido.", "error");
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
            MembroTabData selecionado = table_membros.getSelectionModel().getSelectedItem();
            
            if (selecionado == null) {
                mostrarFeedback("Selecione um membro na tabela para atualizar.", "warning");
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            // Busca o membro original
            Membro membroOriginal = TauraManager.buscarMembro(String.valueOf(selecionado.getMatricula()));
            
            // Atualiza os dados
            membroOriginal.setNome(txt_nome.getText().trim());
            membroOriginal.setEmail(txt_email.getText().trim());
            membroOriginal.setTelefone(txt_telefone.getText().trim());
            membroOriginal.setCargo(combo_cargo.getValue());
            
            // Atualiza no Manager
            TauraManager.atualizarMembro(membroOriginal);
            
            // Recarrega a tabela
            carregarMembrosNaTabela();
            
            // Limpa campos
            limparCampos();
            
            mostrarFeedback("Membro atualizado com sucesso!", "success");
            
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
            MembroTabData selecionado = table_membros.getSelectionModel().getSelectedItem();
            
            if (selecionado == null) {
                mostrarFeedback("Selecione um membro na tabela para remover.", "warning");
                return;
            }
            
            // Confirmação
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Remoção");
            confirmacao.setHeaderText("Remover Membro");
            confirmacao.setContentText("Tem certeza que deseja remover " + selecionado.getNome() + "?");
            
            Optional<ButtonType> resultado = confirmacao.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Remove do Manager
                TauraManager.removerMembro(String.valueOf(selecionado.getMatricula()));
                
                // Recarrega a tabela
                carregarMembrosNaTabela();
                
                // Limpa campos
                limparCampos();
                
                mostrarFeedback("Membro removido com sucesso!", "success");
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
        table_membros.getSelectionModel().clearSelection();
        mostrarFeedback("", "");
    }

    private void carregarMembrosNaTabela() {
        listaMembros.clear();
        
        try {
            var membros = TauraManager.listarMembros();
            for (Membro membro : membros) {
                listaMembros.add(new MembroTabData(membro));
            }
        } catch (IllegalArgumentException e) {
            // Nenhum membro cadastrado ainda
            System.out.println("Nenhum membro para carregar: " + e.getMessage());
        } catch (Exception e) {
            mostrarFeedback("Erro ao carregar membros: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void preencherCamposComMembro(MembroTabData membro) {
        txt_nome.setText(membro.getNome());
        txt_email.setText(membro.getEmail());
        txt_telefone.setText(membro.getTelefone());
        combo_cargo.setValue(membro.getCargo());
        txt_matricula.setText(String.valueOf(membro.getMatricula()));
        txt_matricula.setDisable(true); // Não permite alterar matrícula
    }

    private void limparCampos() {
        txt_nome.clear();
        txt_email.clear();
        txt_telefone.clear();
        combo_cargo.setValue(null);
        txt_matricula.clear();
        txt_matricula.setDisable(false);
        membroSelecionado = null;
    }

    private boolean validarCampos() {
        if (txt_nome.getText().trim().isEmpty()) {
            mostrarFeedback("O nome não pode ser vazio.", "warning");
            return false;
        }
        
        if (txt_email.getText().trim().isEmpty()) {
            mostrarFeedback("O email não pode ser vazio.", "warning");
            return false;
        }
        
        if (txt_telefone.getText().trim().isEmpty()) {
            mostrarFeedback("O telefone não pode ser vazio.", "warning");
            return false;
        }
        
        if (combo_cargo.getValue() == null) {
            mostrarFeedback("Selecione um cargo.", "warning");
            return false;
        }
        
        if (txt_matricula.getText().trim().isEmpty()) {
            mostrarFeedback("A matrícula não pode ser vazia.", "warning");
            return false;
        }
        
        try {
            Integer.parseInt(txt_matricula.getText().trim());
        } catch (NumberFormatException e) {
            mostrarFeedback("Matrícula deve ser um número válido.", "warning");
            return false;
        }
        
        return true;
    }

    private void mostrarFeedback(String mensagem, String tipo) {
        if (lbl_feedback != null) {
            lbl_feedback.setText(mensagem);
            
            // Remove estilos anteriores
            lbl_feedback.getStyleClass().removeAll("feedback-success", "feedback-error", "feedback-warning");
            
            // Adiciona estilo apropriado
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
            // Fallback se não houver label
            System.out.println("[" + tipo.toUpperCase() + "] " + mensagem);
        }
    }
}