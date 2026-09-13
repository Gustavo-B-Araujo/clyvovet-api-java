package br.com.fiap.clyvovet.dto.response;

import java.time.LocalDateTime;
import br.com.fiap.clyvovet.enums.TipoConsulta;
import br.com.fiap.clyvovet.enums.StatusConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponse {
    private Long id;
    private LocalDateTime dataHora;
    private TipoConsulta tipoConsulta;
    private StatusConsulta statusConsulta;
    private String observacoes;
    private String diagnostico;
    private String tratamento;
    private Long petId;
    private String petNome;
    private Long veterinarioId;
    private String veterinarioNome;
}