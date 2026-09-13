package br.com.fiap.clyvovet.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fiap.clyvovet.enums.Role;
import br.com.fiap.clyvovet.model.Usuario;

public class UsuarioAutenticado implements UserDetails {

    private final Usuario usuario;

    public UsuarioAutenticado(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getUsuarioId() {
        return usuario.getId();
    }

    public String getNome() {
        return usuario.getNome();
    }

    public Long getTutorId() {
        return usuario.getTutor() != null ? usuario.getTutor().getId() : null;
    }

    public Long getVeterinarioId() {
        return usuario.getVeterinario() != null ? usuario.getVeterinario().getId() : null;
    }

    public Role getRole() {
        return usuario.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }
}
