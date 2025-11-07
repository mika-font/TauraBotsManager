import java.util.Date;
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

    public static Tarefa parseTarefa(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            throw new IllegalArgumentException("A linha fornecida é nula ou vazia.");
        }

        String[] partes = linha.split(";");

        if (partes.length != 7) {
            throw new IllegalArgumentException("Formato de linha inválido para criar uma Tarefa.");
        }

        String titulo = partes[1];
        String descricao = partes[2];
        String status = partes[3];
        Date prazo;
        List<Membro> membrosResponsaveis = new ArrayList<>();
        String tipo = partes[6];

        try {
            prazo = dateFormat.parse(partes[4]);
        } catch (java.text.ParseException e) {
            throw new IllegalArgumentException("Erro ao converter data: " + e.getMessage());
        }

        String[] matriculasStr = partes[5].split(",");
        for (String matriculaStr : matriculasStr) {
            int matricula = Integer.parseInt(matriculaStr.trim());
            Membro membro = TauraManager.buscarMembro(String.valueOf(matricula));
            membrosResponsaveis.add(membro);
        }

        return new Tarefa(titulo, descricao, status, prazo, membrosResponsaveis, tipo);
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
       return "TAREFA: " + getId() + ";" + getTitulo() + ";" + getDescricao() + ";" + getStatus() + ";" + dateFormat.format(getPrazo()) + ";"
               + getMatriculasResponsaveis() + ";" + getTipo();
    }
}