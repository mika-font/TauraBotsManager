// JAVA FX IMPORTS
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

// COLLECTIONS IMPORTS
import java.util.List;          // Para manipulação de lista em Atividades
import java.util.ArrayList;     // Para manipulação de lista em Atividades
import java.util.Collections;   // Para manipulação de listas imutáveis
import java.util.Map;           // Para manipulação de mapas em Membros
import java.util.HashMap;       // Para manipulação de mapas em Membros

// EXCEPTION IMPORTS
import java.lang.IllegalArgumentException; 
import java.io.FileNotFoundException;
import java.io.IOException;

// FILE IMPORTS
import java.io.FileWriter; 
import java.io.FileReader;

public class App extends Application {
    // private static final String FXML_FILE_PATH = "/fxml/MainView.fxml";
    private static final String APP_TITLE = "Taura Bots Manager";
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 600;
    // private static final String DADOS_MEMBROS_FILE = "";
    // private static final String DADOS_ATIVIDADE_FILE = "";

    // Manipulação de Dados de Membros, Tarefas e Eventos
    private Map<String, Membro> membros = new HashMap<>();
    private List<Atividade> atividades = new ArrayList<>();

    public void salvarDados() throws IOException {
        // Implementar lógica para salvar dados em arquivos
    }

    public void carregarDados() throws IOException, FileNotFoundException {
        // Implementar lógica para carregar dados de arquivos
    }

    // CRUD para Membros - Id único: matrícula
    public void adicionarMembro(Membro membro) throws IllegalArgumentException {
        String matricula = String.valueOf(membro.getMatricula());

        if (membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " já existe.");
        }

        membros.put(matricula, membro);
    }

    public void atualizarMembro(Membro membro) throws IllegalArgumentException {
        String matricula = String.valueOf(membro.getMatricula());

        if (!membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }

        membros.put(matricula, membro);
    }

    public void removerMembro(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula não pode ser vazia.");
        }

        if (!membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }

        membros.remove(matricula);
    }

    public Membro buscarMembro(String matricula) throws IllegalArgumentException {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula de busca não pode ser vazia.");
        }

        Membro membro = membros.get(matricula);

        if (membro == null) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }
        return membro;
    }

    public List<Membro> listarMembros() throws IllegalArgumentException {
        if (membros.isEmpty()) {
            throw new IllegalArgumentException("Nenhum membro cadastrado.");
        }
        return Collections.unmodifiableList(new ArrayList<>(membros.values()));
    }

    // CRUD para Atividades - Id único: id
    public void adicionarAtividade(Atividade atividade) throws IllegalArgumentException {
        if (atividade == null) {
            throw new IllegalArgumentException("Atividade não pode ser nula.");
        }

        if (atividades.contains(atividade)) {
            throw new IllegalArgumentException("Atividade já existe.");
        }

        atividades.add(atividade);
    }

    public void removerAtividade(Atividade atividade) throws IllegalArgumentException {
        if (atividade == null) {
            throw new IllegalArgumentException("Atividade não pode ser nula.");
        }

        if (!atividades.contains(atividade)) {
            throw new IllegalArgumentException("Atividade não encontrada.");
        }

        atividades.remove(atividade);
    }

    public void atualizarAtividade(Atividade atividadeAntiga, Atividade atividadeNova) throws IllegalArgumentException {
        if (atividadeAntiga == null || atividadeNova == null) {
            throw new IllegalArgumentException("Atividades não podem ser nulas.");
        }

        int id = atividades.indexOf(atividadeAntiga);
        if (id == -1) {
            throw new IllegalArgumentException("Atividade antiga não encontrada.");
        }

        atividades.set(id, atividadeNova);
    }

    public Atividade buscarAtividade(int id) throws IllegalArgumentException {
        for (Atividade atividade : atividades) {
            if (atividade.getId() == id) {
                return atividade;
            }
        }
        throw new IllegalArgumentException("Atividade com ID: " + id + " não encontrada.");
    }

    public List<Atividade> listarAtividades() throws IllegalArgumentException {
        if (atividades.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma atividade cadastrada.");
        } 

        return atividades;
    }

    // Interface Gráfica com JavaFX
    @Override
    public void start(javafx.stage.Stage primaryStage) throws Exception {
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setWidth(APP_WIDTH);
        primaryStage.setHeight(APP_HEIGHT);
        primaryStage.show();
        try {
            carregarDados();
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}