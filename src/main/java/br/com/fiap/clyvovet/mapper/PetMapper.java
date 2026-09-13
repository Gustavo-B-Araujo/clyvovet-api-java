package br.com.fiap.clyvovet.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.PetRequest;
import br.com.fiap.clyvovet.dto.response.PetResponse;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.repository.TutorRepository;

@Component
public class PetMapper {

    @Autowired
    private TutorRepository tutorRepository;

    public Pet toEntity(PetRequest request) {
        if (request == null) return null;
        Pet pet = new Pet();
        pet.setNome(request.getNome());
        pet.setEspecie(request.getEspecie());
        pet.setRaca(request.getRaca());
        pet.setDataNascimento(request.getDataNascimento());
        pet.setPeso(request.getPeso());
        pet.setSexo(request.getSexo());
        pet.setCastrado(request.getCastrado());
        if (request.getTutorId() != null) {
            Tutor tutor = tutorRepository.findById(request.getTutorId()).orElse(null);
            pet.setTutor(tutor);
        }
        return pet;
    }

    public PetResponse toResponse(Pet entity) {
        if (entity == null) return null;
        PetResponse response = new PetResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setEspecie(entity.getEspecie());
        response.setRaca(entity.getRaca());
        response.setDataNascimento(entity.getDataNascimento());
        response.setPeso(entity.getPeso());
        response.setSexo(entity.getSexo());
        response.setCastrado(entity.getCastrado());
        if (entity.getTutor() != null) {
            response.setTutorId(entity.getTutor().getId());
            response.setTutorNome(entity.getTutor().getNome());
        }
        return response;
    }
}