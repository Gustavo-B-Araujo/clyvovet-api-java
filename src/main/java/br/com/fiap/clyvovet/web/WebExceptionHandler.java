package br.com.fiap.clyvovet.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fiap.clyvovet.exception.ResourceNotFoundException;

@ControllerAdvice(basePackages = "br.com.fiap.clyvovet.web")
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("mensagem", ex.getMessage());
        return "erro/generico";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, Model model) {
        model.addAttribute("mensagem", ex.getMessage());
        return "erro/generico";
    }
}
