package app;

// FILES IMPORTS
import model.Membro;
import model.Atividade;
import model.Tarefa;
import model.Evento;

// EXCEPTIONS IMPORTS
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.io.FileNotFoundException;

// FILE IMPORTS
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

// COLLECTIONS IMPORTS
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// OTHER IMPORTS
import java.util.Scanner;

public class TauraManager {
    private static final String DADOS_MEMBROS_FILE = "dados_membros.txt";
    private static final String DADOS_ATIVIDADE_FILE = "dados_atividades.txt";

    // Manipulação de Dados de Membros, Tarefas e Eventos
    private static Map<String, Membro> membros = new HashMap<>();
    private static List<Atividade> atividades = new ArrayList<>();

    // Construtor
    public TauraManager() {}

    // Métodos de Persistência de Dados
    public static void salvarDados() {
        // Salva os dados de membros em dados_membros.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter(DADOS_MEMBROS_FILE, false))) {
            membros.values().forEach(membro -> {
                writer.println(membro.toString());
            });
            System.out.println("Dados de Membros salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de membros: " + e.getMessage());
        }

        // Salva os dados de atividades em dados_atividades.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter(DADOS_ATIVIDADE_FILE, false))) {
            atividades.forEach(atividade -> {
                writer.println(atividade.toString());
            });
            System.out.println("Dados de Atividades salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de atividades: " + e.getMessage());
        }
    }

    public static void carregarDados() {
        System.out.println("Carregando dados...");
        membros.clear();
        atividades.clear();

        try (Scanner leitor = new Scanner(new File(DADOS_MEMBROS_FILE))) {
            // Enquanto houver linhas no arquivo
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty())
                    continue;
                try { // Tenta parsear a linha em um membro
                    Membro membro = Membro.parseMembro(linha);
                    membros.put(String.valueOf(membro.getMatricula()), membro);
                } catch (IllegalArgumentException e) {
                    System.err.println("Erro ao carregar membro: " + e.getMessage());
                }
            }
            System.out.println("Dados de Membros carregados com sucesso.");
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo de dados de membros não encontrado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao carregar dados de membros: " + e.getMessage());
        }

        try (Scanner leitor = new Scanner(new File(DADOS_ATIVIDADE_FILE))) {
            // Enquanto houver linhas no arquivo
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty())
                    continue;
                // Determina o tipo de atividade pela linha
                String tipo = "";
                if (linha.startsWith("TAREFA: ;")) {
                    tipo = "TAREFA";
                } else if (linha.startsWith("EVENTO: ;")) {
                    tipo = "EVENTO";
                } else {
                    System.err.println("Linha com formato desconhecido: " + linha);
                    continue;
                }

                try { // Tenta parsear a linha na atividade correta
                    if (tipo.equals("TAREFA")) {
                        Atividade atividade = Tarefa.parseTarefa(linha);
                        adicionarAtividade(atividade);
                    } else if (tipo.equals("EVENTO")) {
                        Atividade atividade = Evento.parseEvento(linha);
                        adicionarAtividade(atividade);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao carregar atividade da linha: " + linha);
                    System.err.println("Detalhes do erro: " + e.getMessage());
                }
            }
            System.out.println("Dados de Atividades carregados com sucesso.");
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo de dados de atividades não encontrado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao carregar dados de atividades: " + e.getMessage());
        }

        // Ajusta o ID para evitar conflitos
        int maxId = atividades.stream()
                .mapToInt(Atividade::getId)
                .max()
                .orElse(0);
        Atividade.resetIds(maxId);
        System.out.println("Dados carregados com sucesso.");
    }

    // CRUD para Membros - Id único: matrícula

    // Adiciona um novo membro à coleção
    public static void adicionarMembro(Membro membro) throws IllegalArgumentException {
        String matricula = String.valueOf(membro.getMatricula());

        // Verifica se o membro já existe pela matrícula
        if (membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " já existe.");
        }

        membros.put(matricula, membro);
    }

    // Atualiza um membro existente na coleção
    public static void atualizarMembro(Membro membro) throws IllegalArgumentException {
        String matricula = String.valueOf(membro.getMatricula());

        if (!membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }

        membros.put(matricula, membro);
    }

    // Remove um membro da coleção pela matrícula
    public static void removerMembro(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula não pode ser vazia.");
        }

        if (!membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }

        membros.remove(matricula);
    }

    // Busca um membro pela matrícula
    public static Membro buscarMembro(String matricula) throws IllegalArgumentException {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula de busca não pode ser vazia.");
        }

        Membro membro = membros.get(matricula);

        if (membro == null) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }
        return membro;
    }

    // Lista todos os membros cadastrados na coleção
    public static List<Membro> listarMembros() throws IllegalArgumentException {
        if (membros.isEmpty()) {
            throw new IllegalArgumentException("Nenhum membro cadastrado.");
        }
        // Retorna uma lista imutável dos membros
        return Collections.unmodifiableList(new ArrayList<>(membros.values()));
    }

    // CRUD para Atividades - Id único: id

    // Adiciona uma nova atividade à coleção
    public static void adicionarAtividade(Atividade atividade) throws IllegalArgumentException {
        if (atividade == null) {
            throw new IllegalArgumentException("Atividade não pode ser nula.");
        }

        if (atividades.contains(atividade)) {
            throw new IllegalArgumentException("Atividade já existe.");
        }

        atividades.add(atividade);
    }

    // Remove uma atividade da coleção
    public static void removerAtividade(Atividade atividade) throws IllegalArgumentException {
        if (atividade == null) {
            throw new IllegalArgumentException("Atividade não pode ser nula.");
        }

        if (!atividades.contains(atividade)) {
            throw new IllegalArgumentException("Atividade não encontrada.");
        }

        atividades.remove(atividade);
    }

    // Atualiza uma atividade existente na coleção
    public static void atualizarAtividade(Atividade atividadeAntiga, Atividade atividadeNova) throws IllegalArgumentException {
        if (atividadeAntiga == null || atividadeNova == null) {
            throw new IllegalArgumentException("Atividades não podem ser nulas.");
        }

        // Encontra o índice da atividade antiga
        int id = atividades.indexOf(atividadeAntiga);
        if (id == -1) { // Atividade antiga não encontrada
            throw new IllegalArgumentException("Atividade antiga não encontrada.");
        }

        atividades.set(id, atividadeNova);
    }

    // Busca uma atividade pelo ID
    public static Atividade buscarAtividade(int id) throws IllegalArgumentException {
        for (Atividade atividade : atividades) {
            if (atividade.getId() == id) {
                return atividade;
            }
        }
        throw new IllegalArgumentException("Atividade com ID: " + id + " não encontrada.");
    }

    // Lista todas as atividades cadastradas na coleção
    public static List<Atividade> listarAtividades() {
        return Collections.unmodifiableList(atividades);
    }

    // Retorna a lista de membros
    public static List<Membro> getMembros() {
        return listarMembros();
    }

    // Retorna listas filtradas de Tarefas e Eventos
    public static List<Atividade> getTarefas() {
        return atividades.stream()
                .filter(a -> a instanceof Tarefa)
                .toList();
    }

    public static List<Atividade> getEventos() {
        return atividades.stream()
                .filter(a -> a instanceof Evento)
                .toList();
    }
}
