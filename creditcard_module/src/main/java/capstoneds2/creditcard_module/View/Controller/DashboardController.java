package capstoneds2.creditcard_module.View.Controller;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import capstoneds2.creditcard_module.Model.Fatura;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Service.CartaoService;
import capstoneds2.creditcard_module.Service.Exceptions.CustomException;
import capstoneds2.creditcard_module.Service.FaturaService;
import capstoneds2.creditcard_module.Service.HistoricoCartaoService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static capstoneds2.creditcard_module.View.Controller.DialogsUtils.*;

@Component
public class DashboardController {

    @FXML
    private Button btnSolicitarCartao, btnBloquearCartao, btnSegundaVia, btnVisualizarFatura, btnAumentarLimite, btnDetalhesCartao;

    @FXML
    private TableView<HistoricoCartao> tabelaTransacoes;

    @FXML
    private TableColumn<HistoricoCartao, String> colData,  colDescricao, colParcelamento;

    @FXML
    private TableColumn<HistoricoCartao, Long> colCartaoId;

    @FXML
    private Label FaturaLabel ,LimiteLabel, VencimentoLabel, lblNumeroFinal, lblStatus, lblLimite;

    private final HistoricoCartaoService historicoCartaoService;
    private final CartaoService cartaoService;
    private final FaturaService faturaService;

    private final ObservableList<HistoricoCartao> listaTransacoes = FXCollections.observableArrayList();

    public DashboardController(CartaoService cartaoService, HistoricoCartaoService historicoCartaoService, FaturaService faturaService) {
        this.historicoCartaoService = historicoCartaoService;
        this.cartaoService = cartaoService;
        this.faturaService = faturaService;
    }

    @FXML
    public void initialize() {
        btnSolicitarCartao.setOnAction(event -> {
            solicitarCartao();

        });
        btnBloquearCartao.setOnAction(event -> {
            bloquearCartao();

        });
        btnDetalhesCartao.setOnAction(event -> {
            abrirModalDetalhesCartao();

        });
        btnVisualizarFatura.setOnAction(event -> {
            visualizarFatura();

        } );

        inicializarTabelaTransacoes();
        carregarHistorico();
        listarCartao();
        atualizarValorTotalFatura(10L);
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
            cartaoService.bloquearCartao(10L, senha, motivo);
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

        //  irei usar ClienteID entao é apenas teste para manter uma base -> Teste para verificar se o clienteId esta sendo utilizado / gerado
        Optional<Cartao> cartaoOptional = cartoes.stream()
                .filter(c -> c.getId() == 10L)
                .findFirst();

        if (cartaoOptional.isPresent()) {
            Cartao cartao = cartaoOptional.get();

            String numero = cartao.getNumero();
            lblNumeroFinal.setText(numero.substring(numero.length() - 4));
            lblStatus.setText(cartao.getStatusCartao().name().toLowerCase());
            lblLimite.setText(String.format("R$ %.2f", cartao.getLimite_disponivel()));

            LimiteLabel.setText(String.format("R$ %.2f", cartao.getLimite_disponivel()));
            VencimentoLabel.setText(cartao.getData_validade().toString());

        } else {
            // Trata caso o cartão com ID 1 não exista
            lblNumeroFinal.setText("----");
            lblStatus.setText("Indisponível");
            lblLimite.setText("R$ 0.00");
        }
    }
    // Detalhes do cartao
    public void abrirModalDetalhesCartao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetalhesCartaoModal.fxml"));
            Parent root = loader.load();

            DetalhesCartaoModalController controller = loader.getController();
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Detalhes do Cartão");
            modal.setScene(new Scene(root));
            controller.setStage(modal);

            Optional<Cartao> cartaoOptional = cartaoService.listarCartoesAtivos().stream()
                    .filter(c -> c.getId() == 10L) // mudar depois para o clienteId e ele seleciona o cartao que ele quer :)
                    .findFirst();

            if (cartaoOptional.isPresent()) {
                controller.preencherDados(cartaoOptional.get());
                modal.showAndWait();
            } else {
                mostrarAlerta("Aviso", "Nenhum cartão encontrado.", Alert.AlertType.WARNING);
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir detalhes do cartão.", Alert.AlertType.ERROR);
        }
    }

    // Visualizar Fatura
    public void visualizarFatura() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FaturaModal.fxml"));
            Parent root = loader.load();

            FaturaModalController controller = loader.getController();
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Faturas do Cartão");
            modal.setScene(new Scene(root));
            controller.setStage(modal);
            
            List<Fatura> faturas = faturaService.listarFaturasPorCartao(10L); // vou fazer por id do cliente
            controller.carregarFaturas(faturas);



            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir faturas.", Alert.AlertType.ERROR);
        }
    }
    // Calcular Fatura

    public void atualizarValorTotalFatura(Long cartaoId) {
        try {
            Double total = faturaService.calcularTotalFaturaAtual(cartaoId);

            NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String valorFormatado = formatoMoeda.format(total);

            FaturaLabel.setText(valorFormatado);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível calcular o total da fatura.", Alert.AlertType.ERROR);
        }
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
