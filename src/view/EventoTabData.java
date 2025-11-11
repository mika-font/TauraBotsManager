package view;

import model.Evento;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import java.text.SimpleDateFormat;

public class EventoTabData {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    private final IntegerProperty id;
    private final StringProperty titulo;
    private final StringProperty descricao;
    private final StringProperty local;
    private final StringProperty status;
    private final StringProperty prazo;
    private final StringProperty dataInicio;
    private final StringProperty dataFim;

    public EventoTabData(Evento evento) {
        this.id = new SimpleIntegerProperty(evento.getId());
        this.titulo = new SimpleStringProperty(evento.getTitulo());
        this.descricao = new SimpleStringProperty(evento.getDescricao());
        this.local = new SimpleStringProperty(evento.getLocal());
        this.status = new SimpleStringProperty(evento.getStatus());
        this.prazo = new SimpleStringProperty(dateFormat.format(evento.getPrazo()));
        this.dataInicio = new SimpleStringProperty(dateFormat.format(evento.getDataInicio()));
        this.dataFim = new SimpleStringProperty(dateFormat.format(evento.getDataFim()));
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

    public StringProperty localProperty() {
        return local;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty prazoProperty() {
        return prazo;
    }

    public StringProperty dataInicioProperty() {
        return dataInicio;
    }

    public StringProperty dataFimProperty() {
        return dataFim;
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

    public String getLocal() {
        return local.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getPrazo() {
        return prazo.get();
    }

    public String getDataInicio() {
        return dataInicio.get();
    }

    public String getDataFim() {
        return dataFim.get();
    }
}