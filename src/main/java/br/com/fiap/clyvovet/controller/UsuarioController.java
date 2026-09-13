package br.com.fiap.clyvovet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.clyvovet.dto.request.AlterarSenhaRequest;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints relacionados à conta de login do usuário autenticado")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/senha")
    @Operation(summary = "Altera a senha de login do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou confirmação não confere")
    })
    public ResponseEntity<Void> alterarSenha(@AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @Valid @RequestBody AlterarSenhaRequest request) {
        usuarioService.alterarSenha(usuarioAutenticado.getUsuarioId(), request);
        return ResponseEntity.noContent().build();
    }
}
