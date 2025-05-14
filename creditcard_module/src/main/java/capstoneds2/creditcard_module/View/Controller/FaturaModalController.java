package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Model.Fatura;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class FaturaModalController {

    @FXML
    private TableView<Fatura> tabelaFaturas;
    @FXML private TableColumn<Fatura, String> colDataVencimento;
    @FXML private TableColumn<Fatura, String> colDataFechamento;
    @FXML private TableColumn<Fatura, String> colValor;
    @FXML private TableColumn<Fatura, String> colStatus;

    @Setter
    private Stage stage;

    public void carregarFaturas(List<Fatura> faturas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colDataVencimento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataVencimento().format(formatter))
        );
        colDataFechamento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataFechamento().format(formatter))
        );
        colValor.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getValor_total()))
        );
        colStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatusFatura().name())
        );

        tabelaFaturas.getItems().setAll(faturas);
    }

    @FXML
    private void fechar() {
        stage.close();
    }
}
