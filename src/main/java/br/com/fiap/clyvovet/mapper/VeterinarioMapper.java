package br.com.fiap.clyvovet.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.VeterinarioRequest;
import br.com.fiap.clyvovet.dto.response.VeterinarioResponse;
import br.com.fiap.clyvovet.model.Clinica;
import br.com.fiap.clyvovet.model.Veterinario;
import br.com.fiap.clyvovet.repository.ClinicaRepository;

@Component
public class VeterinarioMapper {

    @Autowired
    private ClinicaRepository clinicaRepository;

    public Veterinario toEntity(VeterinarioRequest request) {
        if (request == null) return null;
        Veterinario veterinario = new Veterinario();
        veterinario.setNome(request.getNome());
        veterinario.setCrmv(request.getCrmv());
        veterinario.setEspecialidade(request.getEspecialidade());
        veterinario.setEmail(request.getEmail());
        veterinario.setTelefone(request.getTelefone());
        if (request.getClinicaId() != null) {
            Clinica clinica = clinicaRepository.findById(request.getClinicaId()).orElse(null);
            veterinario.setClinica(clinica);
        }
        return veterinario;
    }

    public VeterinarioResponse toResponse(Veterinario entity) {
        if (entity == null) return null;
        VeterinarioResponse response = new VeterinarioResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setCrmv(entity.getCrmv());
        response.setEspecialidade(entity.getEspecialidade());
        response.setEmail(entity.getEmail());
        response.setTelefone(entity.getTelefone());
        if (entity.getClinica() != null) {
            response.setClinicaId(entity.getClinica().getId());
            response.setClinicaNome(entity.getClinica().getNome());
        }
        return response;
    }
}