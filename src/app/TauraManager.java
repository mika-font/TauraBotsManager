package app;

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

    public TauraManager() {
        carregarDados();
    }

    public static void salvarDados() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DADOS_MEMBROS_FILE, false))) {
            membros.values().forEach(membro -> {
                writer.println(membro.toString());
            });
            System.out.println("Dados de Membros salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de membros: " + e.getMessage());
        }

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
        membros.clear();
        atividades.clear();

        try (Scanner leitor = new Scanner(new File(DADOS_MEMBROS_FILE))) {
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty())
                    continue;
                try {
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
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty())
                    continue;

                String tipo = linha.substring(0, linha.indexOf(";"));

                try {
                    if (tipo.equals("TAREFA: ")) {
                        Atividade atividade = Tarefa.parseTarefa(linha);
                        adicionarAtividade(atividade);
                    } else if (tipo.equals("EVENTO: ")) {
                        Atividade atividade = Evento.parseEvento(linha);
                        adicionarAtividade(atividade);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao carregar dados de atividade: " + e.getMessage());
                }
            }
            System.out.println("Dados de Atividades carregados com sucesso.");
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo de dados de atividades não encontrado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao carregar dados de atividades: " + e.getMessage());
        }

        int maxId = atividades.stream()
                .mapToInt(Atividade::getId)
                .max()
                .orElse(0);
        Atividade.resetIds(maxId);
    }

    // CRUD para Membros - Id único: matrícula
    public static void adicionarMembro(Membro membro) throws IllegalArgumentException {
        String matricula = String.valueOf(membro.getMatricula());

        if (membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " já existe.");
        }

        membros.put(matricula, membro);
    }

    public static void atualizarMembro(Membro membro) throws IllegalArgumentException {
        String matricula = String.valueOf(membro.getMatricula());

        if (!membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }

        membros.put(matricula, membro);
    }

    public static void removerMembro(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula não pode ser vazia.");
        }

        if (!membros.containsKey(matricula)) {
            throw new IllegalArgumentException("Membro com a matrícula: " + matricula + " não encontrado.");
        }

        membros.remove(matricula);
    }

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

    public static List<Membro> listarMembros() throws IllegalArgumentException {
        if (membros.isEmpty()) {
            throw new IllegalArgumentException("Nenhum membro cadastrado.");
        }
        return Collections.unmodifiableList(new ArrayList<>(membros.values()));
    }

    // CRUD para Atividades - Id único: id
    public static void adicionarAtividade(Atividade atividade) throws IllegalArgumentException {
        if (atividade == null) {
            throw new IllegalArgumentException("Atividade não pode ser nula.");
        }

        if (atividades.contains(atividade)) {
            throw new IllegalArgumentException("Atividade já existe.");
        }

        atividades.add(atividade);
    }

    public static void removerAtividade(Atividade atividade) throws IllegalArgumentException {
        if (atividade == null) {
            throw new IllegalArgumentException("Atividade não pode ser nula.");
        }

        if (!atividades.contains(atividade)) {
            throw new IllegalArgumentException("Atividade não encontrada.");
        }

        atividades.remove(atividade);
    }

    public static void atualizarAtividade(Atividade atividadeAntiga, Atividade atividadeNova)
            throws IllegalArgumentException {
        if (atividadeAntiga == null || atividadeNova == null) {
            throw new IllegalArgumentException("Atividades não podem ser nulas.");
        }

        int id = atividades.indexOf(atividadeAntiga);
        if (id == -1) {
            throw new IllegalArgumentException("Atividade antiga não encontrada.");
        }

        atividades.set(id, atividadeNova);
    }

    public static Atividade buscarAtividade(int id) throws IllegalArgumentException {
        for (Atividade atividade : atividades) {
            if (atividade.getId() == id) {
                return atividade;
            }
        }
        throw new IllegalArgumentException("Atividade com ID: " + id + " não encontrada.");
    }

    public static List<Atividade> listarAtividades() {
        return Collections.unmodifiableList(atividades);
    }

    public static List<Membro> getMembros() {
        return listarMembros();
    }

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
