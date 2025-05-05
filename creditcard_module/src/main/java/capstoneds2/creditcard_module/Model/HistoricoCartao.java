package capstoneds2.creditcard_module.Model;

import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "historico_cartoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HistoricoCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historico_id;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;

    @Column(name = "acao",nullable = false)
    @Enumerated(EnumType.STRING)
    private AcaoHistorico acao;

    private String detalhes;
    @Column(name = "data_alteracao")
    private LocalDate alteracao;

}

