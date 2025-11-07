import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class ControlerApp implements Initializable {
    private TauraManager Manager;
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Manager = App.getManager();
    }

    @FXML
    public void handleSalvarDados() {
        try {
            Manager.salvarDados();
            // Sucesso na interface gráfica pode ser indicado aqui
        } catch (Exception e) {
            // Tratar erro na interface gráfica
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
}
