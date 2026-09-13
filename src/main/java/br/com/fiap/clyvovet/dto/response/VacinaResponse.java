package br.com.fiap.clyvovet.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacinaResponse {
    private Long id;
    private String nome;
    private LocalDate dataAplicacao;
    private LocalDate dataProximaDose;
    private String fabricante;
    private String lote;
    private String observacoes;
    private Long petId;
    private String petNome;
}