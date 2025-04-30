package capstoneds2.creditcard_module.Model;

import capstoneds2.creditcard_module.Model.Enums.StatusFatura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "faturas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "fatura")
public class Fatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fatura_id;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;

    private Float valor_total;

    private LocalDate data_vencimento;
    private LocalDate data_fechamento;

    @Column(name = "status",nullable = false)
    private StatusFatura statusFatura;

}

