package br.com.fiap.clyvovet.dto.web;

import java.time.LocalDate;

import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NovoPetForm {

    @NotBlank(message = "Informe o nome do pet")
    @Size(max = 100)
    private String nome;

    @NotNull(message = "Selecione a espécie")
    private Especie especie;

    @Size(max = 255)
    private String raca;

    @PastOrPresent(message = "A data de nascimento não pode ser no futuro")
    private LocalDate dataNascimento;

    @Positive(message = "O peso deve ser maior que zero")
    private Double peso;

    private Sexo sexo;

    private boolean castrado;
}
