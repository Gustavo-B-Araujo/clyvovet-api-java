package br.com.fiap.clyvovet.dto.response;

import java.time.LocalDateTime;
import br.com.fiap.clyvovet.enums.TipoAlerta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaSaudeResponse {
    private Long id;
    private TipoAlerta tipoAlerta;
    private String mensagem;
    private LocalDateTime dataGeracao;
    private Boolean lido;
    private LocalDateTime dataLeitura;
    private Long petId;
    private String petNome;

}