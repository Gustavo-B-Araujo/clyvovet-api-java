package br.com.fiap.clyvovet.dto.request;

import java.time.LocalDate;

import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Sexo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotNull(message = "Espécie é obrigatória")
    private Especie especie;

    @Size(max = 50)
    private String raca;

    private LocalDate dataNascimento;

    @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0")
    private Double peso;

    private Sexo sexo;

    private Boolean castrado;

    @NotNull(message = "ID do tutor é obrigatório")
    private Long tutorId;
}