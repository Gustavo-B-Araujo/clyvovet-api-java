package br.com.fiap.clyvovet.dto.request;

import br.com.fiap.clyvovet.enums.TipoAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaSaudeRequest {

    @NotNull(message = "Tipo de alerta é obrigatório")
    private TipoAlerta tipoAlerta;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(max = 500, message = "Mensagem deve ter no máximo 500 caracteres")
    private String mensagem;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}