package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Repository.CartaoRepository;
import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import capstoneds2.creditcard_module.Repository.HistoricoCartaoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CartaoService {


    private final CartaoRepository cartaoRepository;
    private final HistoricoCartaoRepository historicoCartaoRepository;
    public CartaoService(CartaoRepository cartaoRepository, HistoricoCartaoRepository historicoCartaoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.historicoCartaoRepository = historicoCartaoRepository;
    }
    public void gerarCartao(CartaoRegister cartaoRegister) {
        String numero = gerarNumeroCartaoUnico();
        String cvv = String.format("%03d", new Random().nextInt(1000));
        LocalDate dataValidade = LocalDate.now().plusYears(4);
        LocalDate dataEmissao = LocalDate.now();

        Cartao cartao = new Cartao();
        cartao.setNumero(numero);
        cartao.setCvv(cvv);
        cartao.setData_validade(dataValidade);
        cartao.setData_emissao(dataEmissao);
        cartao.setLimite_total(5000f);
        cartao.setLimite_disponivel(5000f);
        cartao.setBandeiraCartao(cartaoRegister.bandeiraCartao());
        cartao.setStatusCartao(StatusCartao.ATIVO);
        cartao.setAprovacaoAutomatica(cartaoRegister.aprovacao_automatica());
        cartao.setEh_adicional(cartaoRegister.eh_adicional());

        cartao.setNome_impresso("JOAO DA SILVA");

        cartaoRepository.save(cartao);
    }

    private String gerarNumeroCartaoUnico() {
        Random random = new Random();
        String numero;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                sb.append(random.nextInt(10)); // Gera um dígito de 0 a 9
            }
            numero = sb.toString();
        } while (cartaoRepository.existsByNumero(numero));

        return numero;
    }
    // READ (por ID)
    public Optional<Cartao> buscarCartaoPorId(Long id) {
        return cartaoRepository.findById(id);
    }

    // READ (todos)
    public List<Cartao> listarTodosCartoes() {
        return cartaoRepository.findAll();
    }

    // UPDATE
    public Optional<Cartao> atualizarCartao(Long id, Cartao novosDados) {
        return cartaoRepository.findById(id).map(cartao -> {
            cartao.atualizarCartao(novosDados);
            return cartaoRepository.save(cartao);
        });
    }

    // DELETE
    public boolean deletarCartaoPorId(Long id) {
        if (cartaoRepository.existsById(id)) {
            cartaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private static final float LIMITE_MAXIMO = 10000f;

    public String ajustarLimite(Long cartaoId, Float novoLimite) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);

        if (cartaoOpt.isEmpty()) {
            return "Cartão não encontrado.";
        }

        Cartao cartao = cartaoOpt.get();

        if (novoLimite < 0) {
            return "O limite não pode ser negativo.";
        }
        if (novoLimite > LIMITE_MAXIMO) {
            return "O limite não pode ser superior ao limite máximo permitido.";
        }

        HistoricoCartao historico = new HistoricoCartao();
        historico.setCartao(cartao);
        historico.setAcao(AcaoHistorico.ajuste_limite);
        historico.setDetalhes("Ajuste de limite para: " + novoLimite);
        historico.setAlteracao(LocalDate.now());

        historicoCartaoRepository.save(historico);

        cartao.setLimite_total(novoLimite);
        cartao.setLimite_disponivel(novoLimite);
        cartaoRepository.save(cartao);

        return "Limite ajustado com sucesso!";
    }

    public List<HistoricoCartao> obterHistoricoDeAjustes(Long cartaoId) {
        return historicoCartaoRepository.findByCartaoId(cartaoId);
    }

    public void alterarModoAprovacao(Long cartaoId, boolean modoAutomatico) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        Boolean antigo = cartao.getAprovacaoAutomatica();
        if (antigo == modoAutomatico) return;

        cartao.setAprovacaoAutomatica(modoAutomatico);
        cartaoRepository.save(cartao);

        HistoricoCartao historico = new HistoricoCartao();
        historico.setCartao(cartao);
        historico.setAcao(AcaoHistorico.alteracao_modo_aprovacao);
        historico.setDetalhes("Alterado de " + (antigo ? "AUTOMÁTICO" : "MANUAL") +
                " para " + (modoAutomatico ? "AUTOMÁTICO" : "MANUAL"));
        historico.setAlteracao(LocalDate.now());
        historicoCartaoRepository.save(historico);
    }

}
