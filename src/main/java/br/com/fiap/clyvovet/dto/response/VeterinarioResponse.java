package br.com.fiap.clyvovet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioResponse {
    private Long id;
    private String nome;
    private String crmv;
    private String especialidade;
    private String email;
    private String telefone;
    private Long clinicaId;
    private String clinicaNome;
}