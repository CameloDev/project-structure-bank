package capstoneds2.creditcard_module.Model;

import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historico_cartoes")
public class HistoricoCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historico_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;

    @Column(name = "acao", nullable = false)
    @Enumerated(EnumType.STRING)
    private AcaoHistorico acao;

    private String detalhes;

    @Column(name = "data_alteracao", nullable = false)
    private LocalDateTime dataAlteracao;

    @PrePersist
    protected void onCreate() {
        this.dataAlteracao = LocalDateTime.now();
    }
}

