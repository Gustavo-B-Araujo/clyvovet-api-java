package br.com.fiap.clyvovet.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacinaRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "Data de aplicação é obrigatória")
    private LocalDate dataAplicacao;

    private LocalDate dataProximaDose;

    private String fabricante;

    private String lote;

    private String observacoes;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}