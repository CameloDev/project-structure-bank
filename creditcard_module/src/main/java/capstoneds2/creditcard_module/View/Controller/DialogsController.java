package capstoneds2.creditcard_module.View.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class DialogsController {

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
}
