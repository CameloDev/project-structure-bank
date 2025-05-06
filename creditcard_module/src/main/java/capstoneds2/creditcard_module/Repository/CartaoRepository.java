package capstoneds2.creditcard_module.Repository;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    boolean existsByNumero(String numero);

    List<Cartao> findByStatusCartao(StatusCartao statusCartao);

}
