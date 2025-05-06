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
@Entity
public class Fatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fatura_id;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;

    private Float valor_total;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
    @Column(name = "data_fechamento")
    private LocalDate dataFechamento;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusFatura statusFatura;

}

