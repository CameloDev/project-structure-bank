package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Repository.CartaoRepository;
import capstoneds2.creditcard_module.Service.Exceptions.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    /*
    Gerar Cartao
 */
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
    /*
    Bloquear Cartao
     */
    @Test
    void deveBloquearCartaoComSenhaCorreta() {
        // Arrange
        Long cartaoId = 1L;
        String senha = "1234";
        String motivo = "Solicitação do usuário";

        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setSenha(senha);
        cartao.setStatusCartao(StatusCartao.ativo);

        when(cartaoRepository.findById(cartaoId)).thenReturn(Optional.of(cartao));

        // Act
        cartaoService.bloquearCartao(cartaoId, senha, motivo);

        // Assert
        assertEquals(StatusCartao.bloqueado, cartao.getStatusCartao());
        verify(cartaoRepository).save(cartao);
        verify(historicoCartaoService).registrarHistorico(cartao, AcaoHistorico.bloqueio, motivo);
    }
    /*
    Bloquear Cartao
     */
    @Test
    void deveLancarExcecaoQuandoCartaoNaoEncontrado() {
        // Arrange
        Long cartaoId = 1L;
        String senha = "1234";
        String motivo = "Motivo qualquer";

        when(cartaoRepository.findById(cartaoId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException ex = assertThrows(CustomException.class, () ->
                cartaoService.bloquearCartao(cartaoId, senha, motivo)
        );

        assertEquals("Cartão não encontrado.", ex.getMessage());
        verify(cartaoRepository, never()).save(any());
        verify(historicoCartaoService, never()).registrarHistorico(any(), any(), any());
    }
    /*
    Bloquear Cartao
     */
    @Test
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        // Arrange
        Long cartaoId = 1L;
        String senhaCorreta = "1234";
        String senhaIncorreta = "0000";
        String motivo = "Senha errada";

        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setSenha(senhaCorreta);
        cartao.setStatusCartao(StatusCartao.ativo);

        when(cartaoRepository.findById(cartaoId)).thenReturn(Optional.of(cartao));

        // Act & Assert
        CustomException ex = assertThrows(CustomException.class, () ->
                cartaoService.bloquearCartao(cartaoId, senhaIncorreta, motivo)
        );

        assertEquals("Senha incorreta.", ex.getMessage());
        verify(cartaoRepository, never()).save(any());
        verify(historicoCartaoService, never()).registrarHistorico(any(), any(), any());
    }

    /*
    Listar Cartoes ativos
     */

    @Test
    void deveListarApenasCartoesAtivos() {
        // Arrange
        Cartao cartao1 = new Cartao();
        cartao1.setStatusCartao(StatusCartao.ativo);
        Cartao cartao2 = new Cartao();
        cartao2.setStatusCartao(StatusCartao.ativo);

        List<Cartao> cartoesAtivos = List.of(cartao1, cartao2);
        when(cartaoRepository.findByStatusCartao(StatusCartao.ativo)).thenReturn(cartoesAtivos);

        // Act
        List<Cartao> resultado = cartaoService.listarCartoesAtivos();

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(c -> c.getStatusCartao() == StatusCartao.ativo));
        verify(cartaoRepository).findByStatusCartao(StatusCartao.ativo);
    }




}
