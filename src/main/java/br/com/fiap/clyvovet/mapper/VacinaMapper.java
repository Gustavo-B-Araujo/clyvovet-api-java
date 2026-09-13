package br.com.fiap.clyvovet.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.VacinaRequest;
import br.com.fiap.clyvovet.dto.response.VacinaResponse;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.Vacina;
import br.com.fiap.clyvovet.repository.PetRepository;

@Component
public class VacinaMapper {

    @Autowired
    private PetRepository petRepository;

    public Vacina toEntity(VacinaRequest request) {
        if (request == null) return null;
        Vacina vacina = new Vacina();
        vacina.setNome(request.getNome());
        vacina.setDataAplicacao(request.getDataAplicacao());
        vacina.setDataProximaDose(request.getDataProximaDose());
        vacina.setFabricante(request.getFabricante());
        vacina.setLote(request.getLote());
        vacina.setObservacoes(request.getObservacoes());
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId()).orElse(null);
            vacina.setPet(pet);
        }
        return vacina;
    }

    public VacinaResponse toResponse(Vacina entity) {
        if (entity == null) return null;
        VacinaResponse response = new VacinaResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setDataAplicacao(entity.getDataAplicacao());
        response.setDataProximaDose(entity.getDataProximaDose());
        response.setFabricante(entity.getFabricante());
        response.setLote(entity.getLote());
        response.setObservacoes(entity.getObservacoes());
        if (entity.getPet() != null) {
            response.setPetId(entity.getPet().getId());
            response.setPetNome(entity.getPet().getNome());
        }
        return response;
    }
}