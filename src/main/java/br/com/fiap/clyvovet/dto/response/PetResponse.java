package br.com.fiap.clyvovet.dto.response;

import java.time.LocalDate;
import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private Long id;
    private String nome;
    private Especie especie;
    private String raca;
    private LocalDate dataNascimento;
    private Double peso;
    private Sexo sexo;
    private Boolean castrado;
    private Long tutorId;
    private String tutorNome;
}