package capstoneds2.creditcard_module.Model.Register;

import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import jakarta.validation.constraints.NotBlank;

public record CartaoRegister(
        Boolean aprovacao_automatica,
        Boolean eh_adicional,
        BandeiraCartao bandeiraCartao,
        String Senha
        // futuramente: Long clienteId
) {
}
