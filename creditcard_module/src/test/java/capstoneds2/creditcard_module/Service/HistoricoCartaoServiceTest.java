package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Repository.HistoricoCartaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HistoricoCartaoServiceTest {

    @Mock
    private HistoricoCartaoRepository historicoCartaoRepository;

    @InjectMocks
    private HistoricoCartaoService historicoCartaoService;

    @Test
    void deveRegistrarHistoricoComSucesso() {
        // Arrange
        Cartao cartao = new Cartao();
        AcaoHistorico acao = AcaoHistorico.bloqueio;
        String detalhes = "Cartão bloqueado por solicitação do cliente.";

        ArgumentCaptor<HistoricoCartao> captor = ArgumentCaptor.forClass(HistoricoCartao.class);

        // Act
        historicoCartaoService.registrarHistorico(cartao, acao, detalhes);

        // Assert
        verify(historicoCartaoRepository).save(captor.capture());

        HistoricoCartao salvo = captor.getValue();
        assertEquals(cartao, salvo.getCartao());
        assertEquals(acao, salvo.getAcao());
        assertEquals(detalhes, salvo.getDetalhes());
    }
}
