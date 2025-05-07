package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Service.CartaoService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.*;

public class HelloWorldController {

    @FXML
    private Button botaoTests;

    @FXML
    public void initialize() {
        botaoTests.setOnAction(event -> System.out.println("Hello, World!"));

    }
}
