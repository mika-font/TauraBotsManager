package view;

import model.Tarefa;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import java.text.SimpleDateFormat;

public class TarefaTabData {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    private final IntegerProperty id;
    private final StringProperty titulo;
    private final StringProperty descricao;
    private final StringProperty tipo;
    private final StringProperty status;
    private final StringProperty prazo;
    private final StringProperty responsaveis;

    public TarefaTabData(Tarefa tarefa) {
        this.id = new SimpleIntegerProperty(tarefa.getId());
        this.titulo = new SimpleStringProperty(tarefa.getTitulo());
        this.descricao = new SimpleStringProperty(tarefa.getDescricao());
        this.tipo = new SimpleStringProperty(tarefa.getTipo());
        this.status = new SimpleStringProperty(tarefa.getStatus());
        this.prazo = new SimpleStringProperty(dateFormat.format(tarefa.getPrazo()));
        this.responsaveis = new SimpleStringProperty(tarefa.getNomesResponsaveis());
    }

    // Property methods para JavaFX
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty prazoProperty() {
        return prazo;
    }

    public StringProperty responsaveisProperty() {
        return responsaveis;
    }

    // Getters simples
    public int getId() {
        return id.get();
    }

    public String getTitulo() {
        return titulo.get();
    }

    public String getDescricao() {
        return descricao.get();
    }

    public String getTipo() {
        return tipo.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getPrazo() {
        return prazo.get();
    }

    public String getResponsaveis() {
        return responsaveis.get();
    }
}