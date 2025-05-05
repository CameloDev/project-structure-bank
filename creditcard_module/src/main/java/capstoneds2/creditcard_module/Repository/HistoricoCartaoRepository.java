package capstoneds2.creditcard_module.Repository;

import capstoneds2.creditcard_module.Model.HistoricoCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoricoCartaoRepository extends JpaRepository<HistoricoCartao, Long> {
    // Retorna uma lista de históricos de cartão por intervalo de data
    List<HistoricoCartao> findByCartaoCartao_idAndAlteracaoBetween(Long cartaoId, LocalDate startDate, LocalDate endDate);

    // Retorna todos os históricos de cartão sem filtro de data
    List<HistoricoCartao> findByCartaoCartao_id(Long cartaoId);
}
