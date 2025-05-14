package capstoneds2.creditcard_module.View.Controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class DialogsController {

    private String solicitarEntradaViaDialog(String titulo, String mensagem) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(mensagem);
        dialog.setContentText(null);

        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }
    // Global -- irei colocar em outra class
    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
