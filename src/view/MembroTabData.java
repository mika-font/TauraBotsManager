package view;

import model.Membro;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class MembroTabData {
    private final StringProperty nome;
    private final StringProperty email;
    private final StringProperty telefone;
    private final StringProperty cargo;
    private final IntegerProperty matricula;

    public MembroTabData(Membro membro) {
        this.nome = new SimpleStringProperty(membro.getNome());
        this.email = new SimpleStringProperty(membro.getEmail());
        this.telefone = new SimpleStringProperty(membro.getTelefone());
        this.cargo = new SimpleStringProperty(membro.getCargo());
        this.matricula = new SimpleIntegerProperty(membro.getMatricula());
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    public StringProperty cargoProperty() {
        return cargo;
    }

    public IntegerProperty matriculaProperty() {
        return matricula;
    }
        
    public String getNome() { return nome.get(); }
    public String getEmail() { return email.get(); }
    public String getTelefone() { return telefone.get(); }
    public String getCargo() { return cargo.get(); }
    public int getMatricula() { return matricula.get(); }
}