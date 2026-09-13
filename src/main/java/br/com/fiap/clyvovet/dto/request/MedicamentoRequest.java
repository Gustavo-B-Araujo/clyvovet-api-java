package br.com.fiap.clyvovet.dto.request;

import java.time.LocalDate;

import br.com.fiap.clyvovet.enums.StatusMedicamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String dosagem;
    private String frequencia;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @NotNull(message = "Status do medicamento é obrigatório")
    private StatusMedicamento statusMedicamento;

    private String observacoes;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;

    private Long consultaId;
}