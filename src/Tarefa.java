import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Tarefa extends Atividade {
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
    public void exibirDetalhes() {
        System.out.println("Tarefa: " + getTitulo());
        System.out.println("Descrição: " + getDescricao());
        System.out.println("Status: " + getStatus());
        System.out.println("Prazo: " + getPrazo());
        System.out.println("Tipo: " + getTipo());
        System.out.println("Responsáveis: " + getNomesResponsaveis());
        System.out.println("-----------------------");
    }
}