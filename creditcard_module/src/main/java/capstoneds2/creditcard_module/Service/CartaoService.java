package capstoneds2.creditcard_module.Service;

import capstoneds2.creditcard_module.Model.Cartao;
import capstoneds2.creditcard_module.Model.Enums.StatusCartao;
import capstoneds2.creditcard_module.Model.Register.CartaoRegister;
import capstoneds2.creditcard_module.Repository.CartaoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CartaoService {

    private final CartaoRepository cartaoRepository;
    public CartaoService(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
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
        cartao.setLimite_total(5000f); // valor padrão inicial
        cartao.setLimite_disponivel(5000f);
        cartao.setBandeiraCartao(cartaoRegister.bandeiraCartao());
        cartao.setStatusCartao(StatusCartao.ATIVO);
        cartao.setAprovacao_automatica(cartaoRegister.aprovacao_automatica());
        cartao.setEh_adicional(cartaoRegister.eh_adicional());

        // Gerar nome impresso fake por enquanto
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


}
