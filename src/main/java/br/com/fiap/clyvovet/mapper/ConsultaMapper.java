package br.com.fiap.clyvovet.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.ConsultaRequest;
import br.com.fiap.clyvovet.dto.response.ConsultaResponse;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.Veterinario;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.VeterinarioRepository;

@Component
public class ConsultaMapper {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    public Consulta toEntity(ConsultaRequest request) {
        if (request == null) return null;
        Consulta consulta = new Consulta();
        consulta.setDataHora(request.getDataHora());
        consulta.setTipoConsulta(request.getTipoConsulta());
        consulta.setStatusConsulta(request.getStatusConsulta());
        consulta.setObservacoes(request.getObservacoes());
        consulta.setDiagnostico(request.getDiagnostico());
        consulta.setTratamento(request.getTratamento());
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId()).orElse(null);
            consulta.setPet(pet);
        }
        if (request.getVeterinarioId() != null) {
            Veterinario veterinario = veterinarioRepository.findById(request.getVeterinarioId()).orElse(null);
            consulta.setVeterinario(veterinario);
        }
        return consulta;
    }

    public ConsultaResponse toResponse(Consulta entity) {
        if (entity == null) return null;
        ConsultaResponse response = new ConsultaResponse();
        response.setId(entity.getId());
        response.setDataHora(entity.getDataHora());
        response.setTipoConsulta(entity.getTipoConsulta());
        response.setStatusConsulta(entity.getStatusConsulta());
        response.setObservacoes(entity.getObservacoes());
        response.setDiagnostico(entity.getDiagnostico());
        response.setTratamento(entity.getTratamento());
        if (entity.getPet() != null) {
            response.setPetId(entity.getPet().getId());
            response.setPetNome(entity.getPet().getNome());
        }
        if (entity.getVeterinario() != null) {
            response.setVeterinarioId(entity.getVeterinario().getId());
            response.setVeterinarioNome(entity.getVeterinario().getNome());
        }
        return response;
    }
}