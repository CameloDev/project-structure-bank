package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Service.CartaoService;
import capstoneds2.creditcard_module.Service.Exceptions.CustomException;
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

import static capstoneds2.creditcard_module.View.Controller.DialogsUtils.*;

@Controller
public class DashboardController {

    @FXML
    private Button btnSolicitarCartao, btnBloquearCartao, btnSegundaVia, btnVisualizarFatura, btnAumentarLimite;

    @FXML
    private TableView<HistoricoCartao> tabelaTransacoes;

    @FXML
    private TableColumn<HistoricoCartao, String> colData,  colDescricao, colParcelamento;

    @FXML
    private TableColumn<HistoricoCartao, Long> colCartaoId;

    @FXML
    private Label FaturaLabel,LimiteLabel, VencimentoLabel, lblNumeroFinal, lblStatus, lblLimite;

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

        });
        btnBloquearCartao.setOnAction(event -> {
            bloquearCartao();

        });

        inicializarTabelaTransacoes();
        carregarHistorico();
        listarCartao();
    }

    // Solicitar Cartao
    private void solicitarCartao() {
        try {
            String senha = solicitarSenhaViaDialog();
            BandeiraCartao bandeira = BandeiraCartao.visa;

            CartaoRegister cartaoRegister = new CartaoRegister(
                    true, // Add Dialog to Verify it
                    false,
                    bandeira,
                    senha
            );

            cartaoService.gerarCartao(cartaoRegister);
            carregarHistorico();
            mostrarAlerta("Cartão", "Cartão solicitado com sucesso!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível solicitar o cartão.", Alert.AlertType.ERROR);
        }
    }
    // Bloquear Cartao
    private void bloquearCartao() {
        try {
            String senha = solicitarEntradaViaDialog("Confirmação de senha", "Digite a senha do cartão:");
            if (senha == null || senha.isBlank()) {
                mostrarAlerta("Erro", "Senha não informada.", Alert.AlertType.WARNING);
                return;
            }

            String motivo = solicitarEntradaViaDialog("Motivo do bloqueio", "Informe o motivo do bloqueio:");
            if (motivo == null || motivo.isBlank()) {
                mostrarAlerta("Erro", "Motivo do bloqueio não informado.", Alert.AlertType.WARNING);
                return;
            }
            cartaoService.bloquearCartao(1L, senha, motivo);
            /*
             Onde eu setei um padrao eu vou listar por account ou fazer uma lista para que o cara liste qual o cartao
            */

            carregarHistorico();
            mostrarAlerta("Cartão", "Cartão bloqueado com sucesso!", Alert.AlertType.INFORMATION);
        } catch (CustomException e) {
            mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro inesperado", "Ocorreu um erro ao bloquear o cartão.", Alert.AlertType.ERROR);
        }
    }

    // Listar Cartao
    public void listarCartao() {
        List<Cartao> cartoes = cartaoService.listarCartoesAtivos();

        // Filtra pelo ID 1 (caso exista) -> irei usar ClienteID entao é apenas teste para manter uma base
        Optional<Cartao> cartaoOptional = cartoes.stream()
                .filter(c -> c.getId() == 1L)
                .findFirst();

        if (cartaoOptional.isPresent()) {
            Cartao cartao = cartaoOptional.get();

            String numero = cartao.getNumero();
            lblNumeroFinal.setText(numero.substring(numero.length() - 4));
            lblStatus.setText(cartao.getStatusCartao().name().toLowerCase());
            lblLimite.setText(String.format("R$ %.2f", cartao.getLimite_disponivel()));
        } else {
            // Trata caso o cartão com ID 1 não exista
            lblNumeroFinal.setText("----");
            lblStatus.setText("Indisponível");
            lblLimite.setText("R$ 0.00");
        }
    }

    // Visualizar Fatura
    public void visualizarFatura(){

    }

    // Tabela
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
    // Tabela
    private void carregarHistorico() {
        try {

            List<HistoricoCartao> historico = historicoCartaoService.listarTodosOsHistoricos();

            listaTransacoes.setAll(historico);

            inicializarTabelaTransacoes();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar histórico de transações.", Alert.AlertType.ERROR);
        }
    }
}
