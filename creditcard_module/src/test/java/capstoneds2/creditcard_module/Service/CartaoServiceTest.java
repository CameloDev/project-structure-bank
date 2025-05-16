package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Repository.CartaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private HistoricoCartaoService historicoCartaoService;

    @Mock
    private FaturaService faturaService;

    @InjectMocks
    private CartaoService cartaoService;

    @Test
    void deveGerarCartaoCorretamente() {
        // Arrange
        CartaoRegister register = new CartaoRegister(
                true,
                false,
                BandeiraCartao.visa,
                "123456"
        );
        // Como nao estou conectado com o JPA realmente, eu estou setando um ID padrao
        when(cartaoRepository.save(any(Cartao.class))).thenAnswer(invocation -> {
            Cartao c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        // Act
        cartaoService.gerarCartao(register);

        // Assert
        ArgumentCaptor<Cartao> captor = ArgumentCaptor.forClass(Cartao.class);
        verify(cartaoRepository).save(captor.capture());
        Cartao salvo = captor.getValue();

        assertNotNull(salvo.getNumero());
        assertNotNull(salvo.getCvv());
        assertEquals(5000f, salvo.getLimite_total());
        assertEquals(5000f, salvo.getLimite_disponivel());
        assertEquals(BandeiraCartao.visa, salvo.getBandeiraCartao());
        assertEquals(StatusCartao.ativo, salvo.getStatusCartao());
        assertEquals("123456", salvo.getSenha());

        verify(historicoCartaoService).registrarHistorico(
                any(Cartao.class),
                eq(AcaoHistorico.solicitacao),
                eq("Cartão gerado com sucesso")
        );

        verify(faturaService).criarFaturaParaCartao(anyLong());
    }


}
