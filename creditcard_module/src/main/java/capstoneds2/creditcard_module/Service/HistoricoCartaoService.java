package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Repository.HistoricoCartaoRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HistoricoCartaoService {
    private final HistoricoCartaoRepository historicoCartaoRepository;

    public HistoricoCartaoService(HistoricoCartaoRepository historicoCartaoRepository) {
        this.historicoCartaoRepository = historicoCartaoRepository;
    }
    public List<HistoricoCartao> obterHistorico(Long cartaoId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return historicoCartaoRepository.findByCartao_IdAndDataAlteracaoBetween(cartaoId, startDate, endDate);
        }
        return historicoCartaoRepository.findByCartao_Id(cartaoId);
    }
    public Optional<HistoricoCartao> obterHistoricoPorId(Long historicoId) {
        return historicoCartaoRepository.findById(historicoId);
    }

}
