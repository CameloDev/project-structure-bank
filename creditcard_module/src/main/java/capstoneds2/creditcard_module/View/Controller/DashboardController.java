package capstoneds2.creditcard_module.View.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController {

    @FXML
    private Label saudacaoLabel;

    public void initialize() {
        saudacaoLabel.setText("Olá, Vitória Barbosa 👋");
    }
}