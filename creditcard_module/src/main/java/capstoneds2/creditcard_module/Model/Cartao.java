package capstoneds2.creditcard_module.Model;

import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "cartoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartao_id")
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Account client
    // ONDE VAI PRECISAR DO MODULO ACCOUNT
     */
    @Column(unique = true, nullable = false)
    private String numero;

    @Column(unique = true, nullable = false)
    private String nome_impresso;

    private String cvv;
    private LocalDate data_validade;
    private Float limite_total;
    private Float limite_disponivel;

    @Column(name = "bandeira",nullable = false)
    @Enumerated(EnumType.STRING)
    private BandeiraCartao bandeiraCartao;

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.ALL)
    private List<Fatura> faturas;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusCartao statusCartao;

    @Column(name = "aprovacao_automatica")
    private Boolean aprovacaoAutomatica = true;

    @Column(name = "eh_adicional")
    private Boolean ehAdicional = false;

    private LocalDate data_emissao;

    // fetch = FetchType.LAZY
    @OneToMany(mappedBy = "cartao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoricoCartao> historicos = new ArrayList<>();

    @Column(nullable = false)
    private String senha;


    /*
    Explicando bem basico, aqui é meio que a tabela do banco de dados,
    mas de forma complexa é a base da api, tudo que criar modificar e deletar vai vir apartir dele,
     a verificação e etc, o entity é uma lista basica de manter que os dados estao sendo enviados certos
     e antes de mandar para o banco de dados que seria a função final, ele verifica se aquilo funciona nele
  */

    /*
    Construtor para Atualizar o cartao
     */
    public void atualizarCartao(Cartao novosDados) {
        this.nome_impresso = novosDados.getNome_impresso();
        this.numero = novosDados.getNumero();
        this.cvv = novosDados.getCvv();
        this.data_validade = novosDados.getData_validade();
        this.limite_total = novosDados.getLimite_total();
        this.limite_disponivel = novosDados.getLimite_disponivel();
        this.bandeiraCartao = novosDados.getBandeiraCartao();
        this.statusCartao = novosDados.getStatusCartao();
        this.aprovacaoAutomatica = novosDados.getAprovacaoAutomatica();
        this.ehAdicional = novosDados.getEhAdicional();
        this.data_emissao = novosDados.getData_emissao();
    }
/*
    Construtor para adicionar o historico
 */
    public void adicionarHistorico(AcaoHistorico acao, String detalhes) {
        HistoricoCartao historico = new HistoricoCartao();
        historico.setCartao(this);
        historico.setAcao(acao);
        historico.setDetalhes(detalhes);
        this.historicos.add(historico);
    }

}
