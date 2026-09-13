package br.com.fiap.clyvovet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CRMV é obrigatório")
    @Size(max = 20, message = "CRMV deve ter no máximo 20 caracteres")
    private String crmv;

    @Size(max = 50)
    private String especialidade;

    @Email(message = "Email deve ser válido")
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String telefone;

    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
}