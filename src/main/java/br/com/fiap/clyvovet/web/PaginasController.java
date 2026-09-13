package br.com.fiap.clyvovet.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.fiap.clyvovet.security.UsuarioAutenticado;

@Controller
public class PaginasController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/app";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/app")
    public String dashboard(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        if (usuario.getTutorId() != null) {
            return "redirect:/app/tutor/pets";
        }
        return "redirect:/app/veterinario/agenda";
    }

    @GetMapping("/app/acesso-negado")
    public String acessoNegado() {
        return "erro/acesso-negado";
    }
}
