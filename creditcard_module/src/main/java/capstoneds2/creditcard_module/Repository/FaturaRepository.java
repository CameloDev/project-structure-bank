package capstoneds2.creditcard_module.Repository;

import capstoneds2.creditcard_module.Model.Enums.StatusFatura;
import capstoneds2.creditcard_module.Model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    List<Fatura> findByCartao_Id(Long cartaoId);

    List<Fatura> findByDataVencimentoBeforeAndStatusFaturaNot(
            LocalDate data, StatusFatura status
    );

    List<Fatura> findByCartao_IdAndStatusFatura(
            Long cartaoId, StatusFatura status
    );
}
