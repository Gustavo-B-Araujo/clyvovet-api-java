package br.com.fiap.clyvovet.dto.response;

import java.time.LocalDate;
import br.com.fiap.clyvovet.enums.StatusMedicamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoResponse {
    private Long id;
    private String nome;
    private String dosagem;
    private String frequencia;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusMedicamento statusMedicamento;
    private String observacoes;
    private Long petId;
    private String petNome;
    private Long consultaId;
}