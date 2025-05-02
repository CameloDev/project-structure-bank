package capstoneds2.creditcard_module.Repository;

import capstoneds2.creditcard_module.Model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    boolean existsByNumero(String numero);


}
