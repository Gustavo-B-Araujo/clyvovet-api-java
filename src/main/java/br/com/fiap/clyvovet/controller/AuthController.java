package br.com.fiap.clyvovet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.clyvovet.dto.request.LoginRequest;
import br.com.fiap.clyvovet.dto.response.LoginResponse;
import br.com.fiap.clyvovet.dto.web.CadastroTutorForm;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.security.JwtService;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.CadastroTutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login e cadastro de tutores usados pelo app mobile")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CadastroTutorService cadastroTutorService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário e retorna o JWT usado nas demais chamadas da API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
        UsuarioAutenticado usuario = (UsuarioAutenticado) authentication.getPrincipal();
        return ResponseEntity.ok(toLoginResponse(usuario));
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastra um novo tutor e já retorna o JWT de acesso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail/CPF já cadastrado")
    })
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody CadastroTutorForm form) {
        Usuario usuario = cadastroTutorService.cadastrar(form);
        return ResponseEntity.ok(toLoginResponse(new UsuarioAutenticado(usuario)));
    }

    private LoginResponse toLoginResponse(UsuarioAutenticado usuario) {
        String token = jwtService.generateToken(usuario);
        return new LoginResponse(token, usuario.getNome(), usuario.getUsername(), usuario.getRole().name(),
                usuario.getTutorId(), usuario.getVeterinarioId());
    }
}
