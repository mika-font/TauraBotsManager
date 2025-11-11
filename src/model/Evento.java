package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Evento extends Atividade {
    private static final java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String local;
    private Date dataInicio;
    private Date dataFim;

    public Evento(String titulo, String descricao, String status, Date prazo, String local, Date dataInicio,
            Date dataFim) {
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

        String Nlinha = linha.substring(linha.indexOf(":") + 1).trim();
        String[] partes = Nlinha.split(";");

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
        return "EVENTO: " + getId() + ";" + getTitulo() + ";" + getDescricao() + ";" + getStatus() + ";"
                + dateFormat.format(getPrazo()) + ";"
                + getLocal() + ";" + dateFormat.format(getDataInicio()) + ";" + dateFormat.format(getDataFim());
    }
}