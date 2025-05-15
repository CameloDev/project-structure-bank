package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.StatusFatura;
import capstoneds2.creditcard_module.Model.Fatura;
import capstoneds2.creditcard_module.Repository.FaturaRepository;
import capstoneds2.creditcard_module.Repository.CartaoRepository;
import capstoneds2.creditcard_module.Service.Exceptions.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

@Service
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final CartaoRepository cartaoRepository;

    public FaturaService(FaturaRepository faturaRepository, CartaoRepository cartaoRepository) {
        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @Transactional
    public void criarFaturaParaCartao(Long cartaoId) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CustomException("Cartão não encontrado"));

        // Verifica se já existe uma fatura aberta para esse cartão no mês atual
        LocalDate primeiroDiaDoMes = LocalDate.now().withDayOfMonth(1);
        LocalDate ultimoDiaDoMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        boolean faturaJaExiste = faturaRepository.existsByCartao_IdAndDataFechamentoBetween(
                cartaoId, primeiroDiaDoMes, ultimoDiaDoMes
        );

        if (faturaJaExiste) {
            return;
        }

        Fatura fatura = new Fatura();
        fatura.setCartao(cartao);
        fatura.setStatusFatura(StatusFatura.aberta);
        fatura.setValor_total(0.0f);

        LocalDate hoje = LocalDate.now();
        fatura.setDataFechamento(hoje.with(TemporalAdjusters.lastDayOfMonth()));
        fatura.setDataVencimento(hoje.plusMonths(1).withDayOfMonth(10));

        faturaRepository.save(fatura);
    }

    @Transactional // TRANSACTION
    public void adicionarValorAFatura(Long faturaId, Double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new CustomException("Fatura não encontrada"));

        fatura.setValor_total((float) (fatura.getValor_total() + valor));

        Cartao cartao = fatura.getCartao();
        cartao.setLimite_disponivel((float) (cartao.getLimite_disponivel() - valor));

        faturaRepository.save(fatura);
        cartaoRepository.save(cartao);
    }

    @Transactional
    public Fatura pagarFatura(Long faturaId) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        if (fatura.getStatusFatura() == StatusFatura.paga) {
            throw new IllegalStateException("Fatura já está paga");
        }

        fatura.setStatusFatura(StatusFatura.paga);

        Cartao cartao = fatura.getCartao();
        cartao.setLimite_disponivel(
                Math.min(
                        cartao.getLimite_total(),
                        cartao.getLimite_disponivel() + fatura.getValor_total()
                )
        );

        faturaRepository.save(fatura);
        cartaoRepository.save(cartao);

        return fatura;
    }
    public List<Fatura> listarFaturasPorCartao(Long cartaoId) {
        return faturaRepository.findByCartao_Id(cartaoId);
    }

    public List<Fatura> listarFaturasVencidas() {
        return faturaRepository.findByDataVencimentoBeforeAndStatusFaturaNot(
                LocalDate.now(),
                StatusFatura.paga
        );
    }
    public Double calcularTotalFaturaAtual(Long cartaoId) {
        return faturaRepository.findByCartao_IdAndStatusFatura(
                        cartaoId, StatusFatura.aberta
                ).stream()
                .map(Fatura::getValor_total)
                .filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue)
                .sum();
    }
}

