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
public class ExameRequest {

    @NotBlank(message = "Tipo do exame é obrigatório")
    @Size(max = 100, message = "Tipo deve ter no máximo 100 caracteres")
    private String tipo;

    @NotNull(message = "Data de realização é obrigatória")
    private LocalDate dataRealizacao;

    private String resultado;
    private String urlArquivo;
    private String observacoes;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;

    private Long consultaId;
}