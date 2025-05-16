package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Fatura;
import capstoneds2.creditcard_module.Repository.CartaoRepository;
import capstoneds2.creditcard_module.Repository.FaturaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FaturaServiceTest {
    @Mock
    private FaturaRepository faturaRepository;

    @InjectMocks
    private FaturaService faturaService;

    @Test
    void deveListarFaturasDoCartao() {
        // Arrange
        Cartao cartao = new Cartao();
        cartao.setId(1L);

        Fatura fatura1 = new Fatura();
        fatura1.setFatura_id(100L);
        fatura1.setValor_total(200f);
        fatura1.setCartao(cartao);

        Fatura fatura2 = new Fatura();
        fatura2.setFatura_id(101L);
        fatura2.setValor_total(300f);
        fatura2.setCartao(cartao);

        List<Fatura> listaMock = List.of(fatura1, fatura2);

        when(faturaRepository.findByCartao_Id(cartao.getId())).thenReturn(listaMock);

        // Act
        List<Fatura> resultado = faturaService.listarFaturasPorCartao(cartao.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(100L, resultado.get(0).getFatura_id());
        assertEquals(200f, resultado.get(0).getValor_total());
        assertEquals(101L, resultado.get(1).getFatura_id());
        assertEquals(300f, resultado.get(1).getValor_total());
    }

}
