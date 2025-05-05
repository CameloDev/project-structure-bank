package capstoneds2.creditcard_module.Repository;

import capstoneds2.creditcard_module.Model.HistoricoCartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface HistoricoCartaoRepository extends JpaRepository<HistoricoCartao, Long> {
    List<HistoricoCartao> findByCartaoIdAndAlteracaoBetween(Long cartaoId, LocalDate startDate, LocalDate endDate);
    List<HistoricoCartao> findByCartaoId(Long cartaoId);
}
