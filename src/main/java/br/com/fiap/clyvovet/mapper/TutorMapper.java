package br.com.fiap.clyvovet.mapper;

import org.springframework.stereotype.Component;
import br.com.fiap.clyvovet.dto.request.TutorRequest;
import br.com.fiap.clyvovet.dto.response.TutorResponse;
import br.com.fiap.clyvovet.model.Endereco;
import br.com.fiap.clyvovet.model.Tutor;

@Component
public class TutorMapper {

    public Tutor toEntity(TutorRequest request) {
        if (request == null) return null;
        Tutor tutor = new Tutor();
        tutor.setNome(request.getNome());
        tutor.setEmail(request.getEmail());
        tutor.setTelefone(request.getTelefone());
        tutor.setCpf(request.getCpf());
        tutor.setDataNascimento(request.getDataNascimento());
        Endereco endereco = new Endereco();
        endereco.setLogradouro(request.getLogradouro());
        endereco.setNumero(request.getNumero());
        endereco.setComplemento(request.getComplemento());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setCep(request.getCep());
        tutor.setEndereco(endereco);
        return tutor;
    }

    public TutorResponse toResponse(Tutor entity) {
        if (entity == null) return null;
        TutorResponse response = new TutorResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setEmail(entity.getEmail());
        response.setTelefone(entity.getTelefone());
        response.setCpf(entity.getCpf());
        response.setDataNascimento(entity.getDataNascimento());
        if (entity.getEndereco() != null) {
            response.setLogradouro(entity.getEndereco().getLogradouro());
            response.setNumero(entity.getEndereco().getNumero());
            response.setComplemento(entity.getEndereco().getComplemento());
            response.setBairro(entity.getEndereco().getBairro());
            response.setCidade(entity.getEndereco().getCidade());
            response.setEstado(entity.getEndereco().getEstado());
            response.setCep(entity.getEndereco().getCep());
        }
        return response;
    }
}