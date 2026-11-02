package br.com.fiap.clyvovet.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.fiap.clyvovet.enums.Role;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.model.Usuario;
import io.jsonwebtoken.JwtException;

@DisplayName("JwtService")
class JwtServiceTest {

    private static final String CHAVE_DE_ASSINATURA = "Y2hhdmUtZGUtdGVzdGUtdW5pdGFyaW8tMzItYnl0ZXMh";
    private static final String CHAVE_DE_OUTRO_AMBIENTE = "Y2hhdmUtZGUtb3V0cm8tYW1iaWVudGUtMzItYnl0ZXMh";
    private static final long UM_DIA_EM_MS = 86_400_000L;

    private static final String EMAIL_DO_TUTOR = "tutor@clyvovet.com.br";
    private static final String EMAIL_DO_VETERINARIO = "veterinario@clyvovet.com.br";

    private JwtService jwtService;
    private UsuarioAutenticado tutorAutenticado;

    @BeforeEach
    void criarServicoEUsuarioTutor() {
        jwtService = servicoComChave(CHAVE_DE_ASSINATURA, UM_DIA_EM_MS);

        Tutor tutor = new Tutor();
        tutor.setId(1L);

        Usuario usuarioTutor = new Usuario();
        usuarioTutor.setId(10L);
        usuarioTutor.setNome("Carlos Mendes");
        usuarioTutor.setEmail(EMAIL_DO_TUTOR);
        usuarioTutor.setSenha("senha-criptografada");
        usuarioTutor.setRole(Role.TUTOR);
        usuarioTutor.setTutor(tutor);

        tutorAutenticado = new UsuarioAutenticado(usuarioTutor);
    }

    @Test
    @DisplayName("gera um token JWT com o e-mail do usuário")
    void geraTokenComEmailDoUsuario() {
        String token = jwtService.generateToken(tutorAutenticado);

        assertFalse(token.isBlank());
        assertEquals(3, token.split("\\.").length, "JWT tem cabeçalho, corpo e assinatura");
        assertEquals(EMAIL_DO_TUTOR, jwtService.extractEmail(token));
    }

    @Test
    @DisplayName("aceita o token apenas para o e-mail do próprio dono")
    void aceitaTokenApenasParaODono() {
        String token = jwtService.generateToken(tutorAutenticado);

        assertTrue(jwtService.isTokenValid(token, EMAIL_DO_TUTOR));
        assertFalse(jwtService.isTokenValid(token, EMAIL_DO_VETERINARIO));
    }

    @Test
    @DisplayName("rejeita token assinado com outra chave")
    void rejeitaTokenAssinadoComOutraChave() {
        String tokenDeOutroAmbiente = servicoComChave(CHAVE_DE_OUTRO_AMBIENTE, UM_DIA_EM_MS)
                .generateToken(tutorAutenticado);

        assertThrows(JwtException.class, () -> jwtService.extractEmail(tokenDeOutroAmbiente));
    }

    @Test
    @DisplayName("rejeita token expirado")
    void rejeitaTokenExpirado() {
        String tokenJaExpirado = servicoComChave(CHAVE_DE_ASSINATURA, -1_000L)
                .generateToken(tutorAutenticado);

        assertThrows(JwtException.class, () -> jwtService.isTokenValid(tokenJaExpirado, EMAIL_DO_TUTOR));
    }

    @Test
    @DisplayName("gera tokens com e-mails diferentes para usuários diferentes")
    void geraTokensDistintosParaUsuariosDistintos() {
        Usuario usuarioVeterinario = new Usuario();
        usuarioVeterinario.setId(20L);
        usuarioVeterinario.setEmail(EMAIL_DO_VETERINARIO);
        usuarioVeterinario.setSenha("senha-criptografada");
        usuarioVeterinario.setRole(Role.VETERINARIO);

        String tokenDoTutor = jwtService.generateToken(tutorAutenticado);
        String tokenDoVeterinario = jwtService.generateToken(new UsuarioAutenticado(usuarioVeterinario));

        assertEquals(EMAIL_DO_TUTOR, jwtService.extractEmail(tokenDoTutor));
        assertEquals(EMAIL_DO_VETERINARIO, jwtService.extractEmail(tokenDoVeterinario));
    }

    private JwtService servicoComChave(String chave, long expiracaoEmMs) {
        JwtService servico = new JwtService();
        ReflectionTestUtils.setField(servico, "secret", chave);
        ReflectionTestUtils.setField(servico, "expirationMs", expiracaoEmMs);
        return servico;
    }
}
