package br.com.fiap.clyvovet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.clyvovet.dto.web.CadastroTutorForm;
import br.com.fiap.clyvovet.exception.CadastroInvalidoException;
import br.com.fiap.clyvovet.service.CadastroTutorService;
import jakarta.validation.Valid;

@Controller
public class CadastroController {

    @Autowired
    private CadastroTutorService cadastroTutorService;

    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("form", new CadastroTutorForm());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("form") CadastroTutorForm form, BindingResult result,
                             Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cadastro";
        }
        try {
            cadastroTutorService.cadastrar(form);
        } catch (CadastroInvalidoException e) {
            model.addAttribute("erroCadastro", e.getMessage());
            return "cadastro";
        }
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso! Faça login para continuar.");
        return "redirect:/login";
    }
}
