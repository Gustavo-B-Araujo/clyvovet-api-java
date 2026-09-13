package br.com.fiap.clyvovet.dto.request;

import java.time.LocalDateTime;

import br.com.fiap.clyvovet.enums.StatusConsulta;
import br.com.fiap.clyvovet.enums.TipoConsulta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRequest {

    @NotNull(message = "Data e hora são obrigatórias")
    private LocalDateTime dataHora;

    @NotNull(message = "Tipo da consulta é obrigatório")
    private TipoConsulta tipoConsulta;

    @NotNull(message = "Status da consulta é obrigatório")
    private StatusConsulta statusConsulta;

    @Size(max = 500)
    private String observacoes;

    @Size(max = 500)
    private String diagnostico;

    @Size(max = 500)
    private String tratamento;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;

    @NotNull(message = "ID do veterinário é obrigatório")
    private Long veterinarioId;
}