package capstoneds2.creditcard_module.Model.Register;

import capstoneds2.creditcard_module.Model.Enums.BandeiraCartao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartaoRegister(
        @NotNull Boolean aprovacao_automatica,
        Boolean eh_adicional,
        BandeiraCartao bandeiraCartao,
        @NotNull
        String Senha
        // futuramente: Long clienteId
) {
}
