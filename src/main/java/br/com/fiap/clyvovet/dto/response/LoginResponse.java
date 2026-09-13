package br.com.fiap.clyvovet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String nome;
    private String email;
    private String role;
    private Long tutorId;
    private Long veterinarioId;
}
