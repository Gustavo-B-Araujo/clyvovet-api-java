package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.web.CadastroTutorForm;
import br.com.fiap.clyvovet.enums.Role;
import br.com.fiap.clyvovet.exception.CadastroInvalidoException;
import br.com.fiap.clyvovet.model.Endereco;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.repository.TutorRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;

/**
 * Autocadastro de tutor: cria o registro de negócio (Tutor) e o login (Usuario)
 * em conjunto, seguindo o mesmo par Tutor+Usuario montado hoje pelo DataSeeder.
 */
@Service
public class CadastroTutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrar(CadastroTutorForm form) {
        if (!form.getSenha().equals(form.getConfirmarSenha())) {
            throw new CadastroInvalidoException("As senhas não coincidem");
        }
        if (tutorRepository.findByEmail(form.getEmail()) != null) {
            throw new CadastroInvalidoException("Já existe um cadastro com este e-mail");
        }
        if (tutorRepository.findByCpf(form.getCpf()) != null) {
            throw new CadastroInvalidoException("Já existe um cadastro com este CPF");
        }
        if (usuarioRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new CadastroInvalidoException("Já existe uma conta com este e-mail");
        }

        Tutor tutor = new Tutor();
        tutor.setNome(form.getNome());
        tutor.setEmail(form.getEmail());
        tutor.setTelefone(form.getTelefone());
        tutor.setCpf(form.getCpf());
        tutor.setDataNascimento(form.getDataNascimento());
        tutor.setEndereco(new Endereco(form.getLogradouro(), form.getNumero(), form.getComplemento(),
                form.getBairro(), form.getCidade(), form.getEstado(), form.getCep()));
        tutor = tutorRepository.save(tutor);

        Usuario usuario = new Usuario();
        usuario.setNome(tutor.getNome());
        usuario.setEmail(form.getEmail());
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));
        usuario.setRole(Role.TUTOR);
        usuario.setTutor(tutor);

        return usuarioRepository.save(usuario);
    }
}
