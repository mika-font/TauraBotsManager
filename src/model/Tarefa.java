package model;

import app.TauraManager;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.lang.IllegalArgumentException;

public class Tarefa extends Atividade {
    private static final java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private List<Membro> responsaveis = new ArrayList<>();
    private String tipo;

    // Construtor
    public Tarefa(String titulo, String descricao, String status, Date prazo, List<Membro> membrosResponsaveis, String tipo) {
        super(titulo, descricao, status, prazo);

        if (membrosResponsaveis == null || membrosResponsaveis.isEmpty()) {
            throw new IllegalArgumentException("A Tarefa deve ter no mínimo um Membro responsável.");
        }

        this.responsaveis = membrosResponsaveis;
        setTipo(tipo);
    }

    // Getters e Setters
    public void setTipo(String tipo) {
        validarTipo(tipo);
        this.tipo = tipo;
    }

    public List<Membro> getResponsaveis() {
        return responsaveis;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNomesResponsaveis() {
        StringJoiner sj = new StringJoiner(", ");
        for (Membro membro : this.responsaveis) {
            sj.add(membro.getNome());
        }
        return sj.toString();
    }

    public String getMatriculasResponsaveis() {
        StringJoiner sj = new StringJoiner(", ");
        for (Membro membro : this.responsaveis) {
            sj.add(String.valueOf(membro.getMatricula()));
        }
        return sj.toString();
    }

    // Métodos para Gerenciar Responsáveis
    public void adicionarResponsavel(Membro membro) {
        if (!responsaveis.contains(membro)) {
            responsaveis.add(membro);
        }
    }

    public void removerResponsavel(Membro membro) {
        if (responsaveis.size() <= 1) {
            throw new IllegalArgumentException("A Tarefa deve ter no mínimo um Membro responsável.");
        }
        responsaveis.remove(membro);
    }

    // Método de Parsing
    public static Tarefa parseTarefa(String linha) throws IllegalArgumentException {
        try {
            // Verifica se a linha começa com "TAREFA: ;"
            if (!linha.startsWith("TAREFA: ;")) {
                throw new IllegalArgumentException("Formato inválido: linha não começa com 'TAREFA: ;'");
            }
            
            String dados = linha.substring(9); // Remove "TAREFA: ;"
            String[] partes = dados.split(";");

            if (partes.length < 7) {
                throw new IllegalArgumentException("Formato inválido: número insuficiente de campos. Esperado 7, recebido " + partes.length);
            }

            // Parsing dos campos
            int id = Integer.parseInt(partes[0].trim());
            String titulo = partes[1].trim();
            String descricao = partes[2].trim();
            String status = partes[3].trim();
            Date prazo = dateFormat.parse(partes[4].trim());
            String tipo = partes[6].trim();

            // Parsing dos responsáveis
            List<Membro> membrosResponsaveis = new ArrayList<>();
            String matriculasStr = partes[5].trim();
            
            // Se houver responsáveis listados, faz o parsing
            if (!matriculasStr.isEmpty() && !matriculasStr.equals("SEM_RESPONSAVEIS")) {
                String[] matriculas = matriculasStr.split(",");
                for (String matricula : matriculas) {
                    String matTrim = matricula.trim();
                    if (!matTrim.isEmpty()) {
                        try { // Busca o Membro pelo número de matrícula
                            Membro membro = TauraManager.buscarMembro(matTrim);
                            if (membro != null) {
                                membrosResponsaveis.add(membro);
                            }
                        } catch (IllegalArgumentException e) {
                            System.err.println("Aviso: Membro com matrícula '" + matTrim + "' não encontrado ao carregar tarefa.");
                        }
                    }
                }
            }

            Tarefa tarefa = new Tarefa(titulo, descricao, status, prazo, membrosResponsaveis, tipo);
            
            // Restaura o ID original
            tarefa.setId(id);
            return tarefa;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Erro ao fazer parsing da data: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erro ao fazer parsing do ID: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao fazer parsing da tarefa: " + e.getMessage());
        }
    }

    // Métodos de Validação
    public boolean validarTipo(String tipo) throws IllegalArgumentException {
        String[] tiposValidos = { "Limpeza", "Manutenção", "Projeto" };
        
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo não pode ser nulo ou vazio.");
        }

        for (String t : tiposValidos) {
            if (t.equalsIgnoreCase(tipo)) {
                return true;
            }
        }
        throw new IllegalArgumentException("Tipo inválido. Tipos válidos são: Limpeza, Manutenção, Projeto.");
    }

    // Método Abstrato de Atividade
    @Override
    public String toString() {
       return "TAREFA: ;" + getId() + ";" + getTitulo() + ";" + getDescricao() + ";" + getStatus() + ";" + dateFormat.format(getPrazo()) + ";"
               + getMatriculasResponsaveis() + ";" + getTipo();
    }
}