package model;

import java.util.Date;

public abstract class Atividade {
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

    public void setId(int id) {
        this.id = id;
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

    public static void resetIds(int maxId) {
        Ids = maxId + 1;
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