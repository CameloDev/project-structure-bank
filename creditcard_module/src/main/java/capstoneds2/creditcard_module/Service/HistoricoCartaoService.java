package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Repository.HistoricoCartaoRepository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HistoricoCartaoService {
    private final HistoricoCartaoRepository historicoCartaoRepository;

    public HistoricoCartaoService(HistoricoCartaoRepository historicoCartaoRepository) {
        this.historicoCartaoRepository = historicoCartaoRepository;
    }
    public List<HistoricoCartao> obterHistorico(Long cartaoId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return historicoCartaoRepository.findByCartaoIdAndAlteracaoBetween(cartaoId, startDate, endDate);
        }
        return historicoCartaoRepository.findByCartaoId(cartaoId);
    }
    public Optional<HistoricoCartao> obterHistoricoPorId(Long historicoId) {
        return historicoCartaoRepository.findById(historicoId);
    }

}
