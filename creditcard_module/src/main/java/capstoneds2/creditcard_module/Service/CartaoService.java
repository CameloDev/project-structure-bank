package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import capstoneds2.creditcard_module.Model.HistoricoCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Repository.CartaoRepository;
import capstoneds2.creditcard_module.Model.Enums.AcaoHistorico;
import capstoneds2.creditcard_module.Repository.HistoricoCartaoRepository;
import capstoneds2.creditcard_module.Service.Exceptions.CustomException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final HistoricoCartaoRepository historicoCartaoRepository;
    private final HistoricoCartaoService historicoCartaoService;
    public CartaoService(CartaoRepository cartaoRepository, HistoricoCartaoRepository historicoCartaoRepository, HistoricoCartaoService historicoCartaoService) {
        this.cartaoRepository = cartaoRepository;
        this.historicoCartaoRepository = historicoCartaoRepository;
        this.historicoCartaoService = historicoCartaoService;
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
        cartao.setStatusCartao(StatusCartao.ativo);
        cartao.setAprovacaoAutomatica(cartaoRegister.aprovacao_automatica());
        cartao.setEhAdicional(cartaoRegister.eh_adicional());
        cartao.setSenha(cartaoRegister.Senha());
        cartao.setNome_impresso("JOAO DA SILVA"); // depois altere para o nome real via UserID: userid == account.getId()

        cartaoRepository.save(cartao);

        historicoCartaoService.registrarHistorico(
                cartao,
                AcaoHistorico.solicitacao,
                "Cartão gerado com sucesso"
        );
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
            historicoCartaoService.registrarHistorico(
                    cartao,
                    AcaoHistorico.alteracao_status,
                    "Cartão atualizado com sucesso"
            );
            return cartaoRepository.save(cartao);
        });
    }
// acho que nao vai precisar por enquanto
    public boolean deletarCartaoPorId(Long id) {
        Optional<Cartao> cartaoOptional = cartaoRepository.findById(id);

        if (cartaoOptional.isPresent()) {
            Cartao cartao = cartaoOptional.get();

            historicoCartaoService.registrarHistorico(
                    cartao,
                    AcaoHistorico.bloqueio,
                    "Cartão cancelado e removido do sistema."
            );

            cartaoRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private static final float LIMITE_MAXIMO = 10000f; // procurar um jeito de mudar isso aqui

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

        historicoCartaoService.registrarHistorico(
                cartao,
                AcaoHistorico.ajuste_limite,
                "Ajuste de limite para: " + novoLimite
        );

        cartao.setLimite_total(novoLimite);
        cartao.setLimite_disponivel(novoLimite);
        cartaoRepository.save(cartao);

        return "Limite ajustado com sucesso!";
    }

    // BL-009
    public void alterarModoAprovacao(Long cartaoId, boolean modoAutomatico) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CustomException("Cartão não encontrado"));

        Boolean antigo = cartao.getAprovacaoAutomatica();
        if (antigo == modoAutomatico) return;

        cartao.setAprovacaoAutomatica(modoAutomatico);
        cartaoRepository.save(cartao);

        historicoCartaoService.registrarHistorico(
                cartao,
                AcaoHistorico.alteracao_modo_aprovacao,
                "Alterado de " + (antigo ? "AUTOMÁTICO" : "MANUAL") +
                        " para " + (modoAutomatico ? "AUTOMÁTICO" : "MANUAL")
        );
    }

    // BL-010
    @Transactional
    public List<Cartao> listarCartoesAtivos() { // cliente ID
        return cartaoRepository.findByStatusCartao(StatusCartao.ativo);
    }

    @Transactional
    public List<Cartao> listarCartoesBloqueados() { // Cliente Id
        return cartaoRepository.findByStatusCartao(StatusCartao.bloqueado);
    }

    public void bloquearCartao(Long cartaoId, String senha, String motivo) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CustomException("Cartão não encontrado."));

        if (!cartao.getSenha().equals(senha)) {
            throw new CustomException("Senha incorreta.");
        }

        cartao.setStatusCartao(StatusCartao.bloqueado);
        cartaoRepository.save(cartao);

        historicoCartaoService.registrarHistorico(cartao, AcaoHistorico.bloqueio, motivo);
    }
/* Verificando
    @Transactional()
    public List<HistoricoCartao> consultarHistorico(Long cartaoId) {
        return historicoCartaoRepository.findByCartao_IdOrderByDataAlteracaoDesc(cartaoId);
    }
 */
    @Transactional
    public void desbloquearCartao(Long cartaoId, String senha, String motivo) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CustomException("Cartão não encontrado"));

        if (!cartao.getSenha().equals(senha)) {
            throw new CustomException("Senha incorreta");
        }

        if (cartao.getStatusCartao() != StatusCartao.BLOQUEADO) {
            throw new CustomException("Cartão não está bloqueado temporariamente");
        }

        cartao.setStatusCartao(StatusCartao.ATIVO);


        cartao.adicionarHistorico(AcaoHistorico.desbloqueio,
                "Cartão desbloqueado. Motivo: " + motivo);

        cartaoRepository.save(cartao);
    }

    @Transactional
    public Cartao buscarPorId(Long id) {
        return cartaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
    }

    /*
    Vou add depois o BL-013, vou dormir
     */
}