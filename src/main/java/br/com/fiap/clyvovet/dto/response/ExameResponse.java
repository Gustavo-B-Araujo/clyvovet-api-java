package br.com.fiap.clyvovet.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExameResponse {
    private Long id;
    private String tipo;
    private LocalDate dataRealizacao;
    private String resultado;
    private String urlArquivo;
    private String observacoes;
    private Long petId;
    private String petNome;
    private Long consultaId;
}