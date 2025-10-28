import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Membro {
    private String nome;
    private String email;
    private String telefone;
    private String cargo;
    private int matricula;

    // Construtor
    public Membro(String nome, String email, String telefone, String cargo, int matricula) {
        setNome(nome);
        setEmail(email);
        setTelefone(telefone);
        setCargo(cargo);
        setMatricula(matricula);
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setNome(String nome) {
        if (validarNome(nome)) { 
            this.nome = nome;
        }
    }

    public void setEmail(String email) {
        if (validarEmail(email)) {
            this.email = email;
        }
    }

    public void setTelefone(String telefone) {
        if (validarTelefone(telefone)) {
            this.telefone = telefone;
        }
    }

    public void setCargo(String cargo) {
        if (validarCargo(cargo)) {
            this.cargo = cargo;
        }
    }

    public void setMatricula(int matricula) {
        if (validarMatricula(matricula)) {
            this.matricula = matricula;
        }
    }

    // Exibir informações do membro
    public String exibirMembro() {
        return "Nome: " + getNome() + "\nEmail: " + getEmail() + "\nTelefone: " + getTelefone() + "\nCargo: "
                + getCargo() + "\nMatrícula: " + getMatricula();
    }

    // Validação de Campos
    public boolean validarEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("O email não pode ser nulo ou vazio.");
        }

        final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }

        return true; // Email válido
    }

    public boolean validarMatricula(int matricula) throws IllegalArgumentException {
        if (matricula <= 0) {
            throw new IllegalArgumentException("A matrícula deve ser um número positivo.");
        }

        if (String.valueOf(matricula).length() != 9 && String.valueOf(matricula).length() != 10) {
            throw new IllegalArgumentException("A matrícula deve ter 9 ou 10 dígitos.");
        }

        if (!String.valueOf(matricula).matches("\\d+")) {
            throw new IllegalArgumentException("A matrícula deve conter apenas números.");
        }

        return true; // Matrícula válida
    }

    public boolean validarTelefone(String telefone) throws IllegalArgumentException {
        if (telefone == null || telefone.isEmpty()) {
            throw new IllegalArgumentException("O telefone não pode ser nulo ou vazio.");
        }

        String telefoneApenasNumeros = telefone.replaceAll("[^\\d]", "");

        if (telefoneApenasNumeros.length() < 10 || telefoneApenasNumeros.length() > 11) {
            throw new IllegalArgumentException("O telefone deve ter 10 ou 11 dígitos.");
        }

        if (!telefoneApenasNumeros.matches("\\d+")) {
            throw new IllegalArgumentException("O telefone deve conter apenas números.");
        }

        return true; // Telefone válido
    }

    public boolean validarNome(String nome) throws IllegalArgumentException {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }

        if (nome.length() < 2) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 2 caracteres.");
        }

        if (!nome.matches("[a-zA-ZÀ-ÿ ']+")) {
            throw new IllegalArgumentException("O nome deve conter apenas letras e espaços.");
        }

        return true; // Nome válido
    }

    public boolean validarCargo(String cargo) throws IllegalArgumentException {
        if (cargo == null || cargo.isEmpty()) {
            throw new IllegalArgumentException("O cargo não pode ser nulo ou vazio.");
        }

        if (!cargo.matches("[a-zA-ZÀ-ÿ ']+")) {
            throw new IllegalArgumentException("O cargo deve conter apenas letras e espaços.");
        }

        if (cargo.equals("Membro Padrão") || cargo.equals("Capitão") || cargo.equals("Financeiro")) {
            throw new IllegalArgumentException(
                    "Cargo inválido. Os cargos permitidos são: Membro Padrão, Capitão, Financeiro.");
        }

        return true; // Cargo válido
    }
}