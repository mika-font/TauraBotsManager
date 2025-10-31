import java.util.Date;

abstract class Atividade {
    protected static int Ids = 0;
    
    protected int id;
    protected String titulo;
    protected String descricao;
    protected String status;
    protected Date prazo;

    public Atividade(String titulo, String descricao, String status, Date prazo) {
        this.id = Ids;
        setTitulo(titulo);
        setDescricao(descricao);
        setStatus(status);
        setPrazo(prazo);
        Ids++;
    }

    public int getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }

    public Date getPrazo() {
        return prazo;
    }

    public void setTitulo(String titulo) {
        if (validarTitulo(titulo)) {
            this.titulo = titulo;
        }
    }

    public void setDescricao(String descricao) {
        if (validarDescricao(descricao)) {
            this.descricao = descricao;
        }
    }

    public void setStatus(String status) {
        if (validarStatus(status)) {
            this.status = status;
        }
    }

    public void setPrazo(Date prazo) {
        if (validarPrazo(prazo)) {
            this.prazo = prazo;
        }
    }

    // Métodos de validação
    public boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio.");
        }

        if (titulo.length() > 100) {
            throw new IllegalArgumentException("O título não pode exceder 100 caracteres.");
        }

        return true;
    }

    public boolean validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser nula ou vazia.");
        }

        if (descricao.length() > 500) {
            throw new IllegalArgumentException("A descrição não pode exceder 500 caracteres.");
        }

        return true;
    }

    public boolean validarStatus(String status) {
        String[] statusValidos = { "Pendente", "Em Progresso", "Concluído" };
        for (String s : statusValidos) {
            if (s.equalsIgnoreCase(status)) {
                return true;
            }
        }
        throw new IllegalArgumentException("Status inválido. Valores válidos são: Pendente, Em Progresso, Concluído.");
    }

    public boolean validarPrazo(Date prazo) {
        if (prazo == null) {
            throw new IllegalArgumentException("O prazo não pode ser nulo.");
        }

        Date hoje = new Date();
        if (prazo.before(hoje)) {
            throw new IllegalArgumentException("O prazo não pode ser uma data passada.");
        }

        return true;
    }

    public abstract void exibirDetalhes();
}

public class Evento extends Atividade {
    private String local;
    private Date dataInicio;
    private Date dataFim;

    public Evento(String titulo, String descricao, String status, Date prazo, String local, Date dataInicio, Date dataFim) {
        super(titulo, descricao, status, prazo);
        setLocal(local);
        setDataInicio(dataInicio);
        setDataFim(dataFim);
    }

    public String getLocal() {
        return local;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    // Métodos de Validação
    public boolean validarLocal(String local) {
        if (local == null || local.trim().isEmpty()) {
            throw new IllegalArgumentException("O local não pode ser nulo ou vazio.");
        }

        if (local.length() > 100) {
            throw new IllegalArgumentException("O local não pode exceder 100 caracteres.");
        }

        return true;
    }

    public boolean validarDataInicio(Date dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("A data de início não pode ser nula.");
        }
        return true;
    }

    public boolean validarDataFim(Date dataFim, Date dataInicio) {
        if (dataFim == null) {
            throw new IllegalArgumentException("A data de fim não pode ser nula.");
        }
        if (dataFim.before(dataInicio)) {
            throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início.");
        }
        return true;
    }

    // Método Abstrato de Atividade
    @Override
    public void exibirDetalhes() {
        System.out.println("ID: " + getId());
        System.out.println("Evento: " + getTitulo());
        System.out.println("Descrição: " + getDescricao());
        System.out.println("Status: " + getStatus());
        System.out.println("Prazo: " + getPrazo());
        System.out.println("Local: " + getLocal());
        System.out.println("Data de Início: " + getDataInicio());
        System.out.println("Data de Fim: " + getDataFim());
    }
}