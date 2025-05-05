package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Repository.HistoricoCartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricoCartaoService {
    @Autowired
    private HistoricoCartaoRepository historicoCartaoRepository;

    // Método para obter o histórico de um cartão, com filtro por data
    public List<HistoricoCartao> obterHistorico(Long cartaoId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return historicoCartaoRepository.findByCartaoCartao_idAndAlteracaoBetween(cartaoId, startDate, endDate);
        }
        return historicoCartaoRepository.findByCartaoCartao_id(cartaoId);
    }

    // Método para obter um histórico específico por ID
    public Optional<HistoricoCartao> obterHistoricoPorId(Long historicoId) {
        return historicoCartaoRepository.findById(historicoId);
    }

}
