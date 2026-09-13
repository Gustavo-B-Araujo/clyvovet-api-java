package br.com.fiap.clyvovet.mapper;

import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.ClinicaRequest;
import br.com.fiap.clyvovet.dto.response.ClinicaResponse;
import br.com.fiap.clyvovet.model.Clinica;
import br.com.fiap.clyvovet.model.Endereco;

@Component
public class ClinicaMapper {

    public Clinica toEntity(ClinicaRequest request) {
        if (request == null) return null;
        Clinica clinica = new Clinica();
        clinica.setNome(request.getNome());
        clinica.setCnpj(request.getCnpj());
        clinica.setEmail(request.getEmail());
        clinica.setTelefone(request.getTelefone());
        Endereco endereco = new Endereco();
        endereco.setLogradouro(request.getLogradouro());
        endereco.setNumero(request.getNumero());
        endereco.setComplemento(request.getComplemento());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setCep(request.getCep());
        clinica.setEndereco(endereco);
        return clinica;
    }

    public ClinicaResponse toResponse(Clinica entity) {
        if (entity == null) return null;
        ClinicaResponse response = new ClinicaResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setCnpj(entity.getCnpj());
        response.setEmail(entity.getEmail());
        response.setTelefone(entity.getTelefone());
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