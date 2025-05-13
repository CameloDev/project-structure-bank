package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Service.CartaoService;
import capstoneds2.creditcard_module.Service.HistoricoCartaoService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @FXML
    private Button btnSolicitarCartao;

    @FXML
    private Button btnBloquearCartao;

    @FXML
    private Button btnSegundaVia;

    @FXML
    private Button btnVisualizarFatura;

    @FXML
    private Button btnAumentarLimite;

    @FXML
    private TableView<HistoricoCartao> tabelaTransacoes;

    @FXML
    private TableColumn<HistoricoCartao, String> colData;

    @FXML
    private TableColumn<HistoricoCartao, String> colDescricao;

    @FXML
    private TableColumn<HistoricoCartao, String> colParcelamento;

    @FXML
    private TableColumn<HistoricoCartao, Long> colCartaoId;

    private final HistoricoCartaoService historicoCartaoService;
    private final CartaoService cartaoService;

    private final ObservableList<HistoricoCartao> listaTransacoes = FXCollections.observableArrayList();

    public DashboardController(CartaoService cartaoService, HistoricoCartaoService historicoCartaoService) {
        this.historicoCartaoService = historicoCartaoService;
        this.cartaoService = cartaoService;
    }

    @FXML
    public void initialize() {
        btnSolicitarCartao.setOnAction(event -> {
            solicitarCartao();
            System.out.println("Solicitando cartao");
        });

        inicializarTabelaTransacoes(); // <-- aqui configuramos a tabela
        carregarHistorico();           // <-- e carregamos os dados
    }

    private void inicializarTabelaTransacoes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        colData.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDataAlteracao().format(formatter)
                )
        );
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("detalhes"));
        colParcelamento.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colCartaoId.setCellValueFactory(cellData -> {
            Long idCartao = cellData.getValue().getCartao().getId();
            return new SimpleObjectProperty<>(idCartao);
        });

        tabelaTransacoes.setItems(listaTransacoes);
    }

    private void carregarHistorico() {
        try {
            List<HistoricoCartao> historico = historicoCartaoService.listarTodosOsHistoricos();
            listaTransacoes.setAll(historico);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar histórico de transações.", Alert.AlertType.ERROR);
        }
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
