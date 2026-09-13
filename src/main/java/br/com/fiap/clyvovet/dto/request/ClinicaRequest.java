package br.com.fiap.clyvovet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicaRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(min = 14, max = 18, message = "CNPJ deve ter entre 14 e 18 caracteres")
    private String cnpj;

    @Email(message = "Email deve ser válido")
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String telefone;

    // Endereço
    @Size(max = 150)
    private String logradouro;

    @Size(max = 10)
    private String numero;

    @Size(max = 50)
    private String complemento;

    @Size(max = 50)
    private String bairro;

    @Size(max = 50)
    private String cidade;

    @Size(min = 2, max = 2)
    private String estado;

    @Size(min = 8, max = 9)
    private String cep;
}