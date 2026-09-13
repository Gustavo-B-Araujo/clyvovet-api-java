package br.com.fiap.clyvovet.dto.web;

import java.time.LocalDateTime;

import br.com.fiap.clyvovet.enums.TipoConsulta;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class AgendamentoConsultaForm {

    @NotNull(message = "Selecione o pet")
    private Long petId;

    @NotNull(message = "Selecione o veterinário")
    private Long veterinarioId;

    @NotNull(message = "Informe a data e hora da consulta")
    @Future(message = "A consulta deve ser marcada para uma data futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHora;

    @NotNull(message = "Selecione o tipo de consulta")
    private TipoConsulta tipoConsulta;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;
}
