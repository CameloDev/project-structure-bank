package capstoneds2.creditcard_module.Repository;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoricoCartaoRepository extends JpaRepository<HistoricoCartao, Long> {
    List<HistoricoCartao> findByCartao_IdAndDataAlteracaoBetween(Long cartao_id, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<HistoricoCartao> findByCartao_Id(Long cartaoId);

    List<HistoricoCartao> findByCartao_IdOrderByDataAlteracaoDesc(Long id);
}
