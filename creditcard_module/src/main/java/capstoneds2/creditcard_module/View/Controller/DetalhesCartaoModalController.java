package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Model.Cartao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class DetalhesCartaoModalController {

    @FXML
    private Label lblNumero, lblCvv, lblDataEmissao, lblDataValidade,lblLimiteTotal, lblLimiteDisponivel,lblStatus;
    @Setter
    private Stage stage;

    public void preencherDados(Cartao cartao) {
        lblNumero.setText(cartao.getNumero());
        lblCvv.setText(cartao.getCvv());
        lblDataEmissao.setText(cartao.getData_emissao().toString());
        lblDataValidade.setText(cartao.getData_validade().toString());
        lblLimiteTotal.setText(String.format("R$ %.2f", cartao.getLimite_total()));
        lblLimiteDisponivel.setText(String.format("R$ %.2f", cartao.getLimite_disponivel()));
        lblStatus.setText(cartao.getStatusCartao().name());
    }

    @FXML
    private void fechar() {
        if (stage != null) {
            stage.close();
        }
    }
}
