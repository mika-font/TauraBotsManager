import java.util.Date;
import java.text.SimpleDateFormat;

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

    public abstract String toString();
}

public class Evento extends Atividade {
    private static final java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

    public static Evento parseEvento(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            throw new IllegalArgumentException("A linha fornecida é nula ou vazia.");
        }

        String[] partes = linha.split(";");

        if (partes.length != 8) {
            throw new IllegalArgumentException("Formato de linha inválido para criar um Evento.");
        }

        String titulo = partes[1];
        String descricao = partes[2];
        String status = partes[3];
        Date prazo;
        Date dataInicio;
        Date dataFim;
        String local = partes[5];

        try {
            prazo = dateFormat.parse(partes[4]);
            dataInicio = dateFormat.parse(partes[6]);
            dataFim = dateFormat.parse(partes[7]);
        } catch (java.text.ParseException e) {
            throw new IllegalArgumentException("Erro ao converter datas: " + e.getMessage());
        }

        return new Evento(titulo, descricao, status, prazo, local, dataInicio, dataFim);
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
    public String toString() {
        return "EVENTO: " + getId() + ";" + getTitulo() + ";" + getDescricao() + ";" + getStatus() + ";" + dateFormat.format(getPrazo()) + ";"
                + getLocal() + ";" + dateFormat.format(getDataInicio()) + ";" + dateFormat.format(getDataFim());
    }
}