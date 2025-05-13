package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Service.CartaoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class DashboardController {

    @FXML
    private Button btnSolicitarCartao;

    private final CartaoService cartaoService;

    public DashboardController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @FXML
    public void initialize() {
        btnSolicitarCartao.setOnAction(event -> {
            solicitarCartao();
            System.out.println("Solicitando cartao");
        });


    }

    private void solicitarCartao() {
        try {
            String senha = solicitarSenhaViaDialog();
            BandeiraCartao bandeira = BandeiraCartao.visa;

            CartaoRegister cartaoRegister = new CartaoRegister(
                    true,
                    false,
                    bandeira,
                    senha
            );

            cartaoService.gerarCartao(cartaoRegister);

            mostrarAlerta("Cartão", "Cartão solicitado com sucesso!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível solicitar o cartão.", Alert.AlertType.ERROR);
        }
    }

    private String solicitarSenhaViaDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirmação");
        dialog.setHeaderText("Digite a senha do novo cartão");
        dialog.setContentText("Senha:");
        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElseThrow(() -> new RuntimeException("Senha não informada"));
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
