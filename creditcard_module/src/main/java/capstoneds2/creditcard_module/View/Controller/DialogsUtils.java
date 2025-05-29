package capstoneds2.creditcard_module.View.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class DialogsUtils {
    // Classe utilitária para usar ai
    public static String solicitarEntradaViaDialog(String titulo, String mensagem) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(mensagem);
        dialog.setContentText(null);

        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }

    public static void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
    public static String solicitarSenhaViaDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirmação");
        dialog.setHeaderText("Digite a senha do novo cartão");
        dialog.setContentText("Senha:");
        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }
}
