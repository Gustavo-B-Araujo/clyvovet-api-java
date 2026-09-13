package br.com.fiap.clyvovet.dto.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AtendimentoConsultaForm {

    @NotBlank(message = "Informe o diagnóstico")
    @Size(max = 500)
    private String diagnostico;

    @NotBlank(message = "Informe o tratamento")
    @Size(max = 500)
    private String tratamento;

    private boolean aplicarVacina;

    @Size(max = 100)
    private String vacinaNome;

    @Size(max = 255)
    private String vacinaFabricante;

    @Min(value = 1, message = "A próxima dose deve ser em pelo menos 1 dia")
    private Integer vacinaDiasProximaDose;

    private boolean necessitaRetorno;

    @Min(value = 1, message = "O retorno deve ser em pelo menos 1 dia")
    private Integer retornoDias;
}
