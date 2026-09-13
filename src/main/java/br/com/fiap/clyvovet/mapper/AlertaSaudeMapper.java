package br.com.fiap.clyvovet.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.AlertaSaudeRequest;
import br.com.fiap.clyvovet.dto.response.AlertaSaudeResponse;
import br.com.fiap.clyvovet.model.AlertaSaude;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.PetRepository;

@Component
public class AlertaSaudeMapper {

    @Autowired
    private PetRepository petRepository;

    public AlertaSaude toEntity(AlertaSaudeRequest request) {
        if (request == null) return null;
        AlertaSaude alerta = new AlertaSaude();
        alerta.setTipoAlerta(request.getTipoAlerta());
        alerta.setMensagem(request.getMensagem());
        alerta.setDataGeracao(LocalDateTime.now());
        alerta.setLido(false);
        alerta.setDataLeitura(null);
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId()).orElse(null);
            alerta.setPet(pet);
        }
        return alerta;
    }

    public AlertaSaudeResponse toResponse(AlertaSaude entity) {
        if (entity == null) return null;
        AlertaSaudeResponse response = new AlertaSaudeResponse();
        response.setId(entity.getId());
        response.setTipoAlerta(entity.getTipoAlerta());
        response.setMensagem(entity.getMensagem());
        response.setDataGeracao(entity.getDataGeracao());
        response.setLido(entity.getLido());
        response.setDataLeitura(entity.getDataLeitura());
        if (entity.getPet() != null) {
            response.setPetId(entity.getPet().getId());
            response.setPetNome(entity.getPet().getNome());
        }
        return response;
    }
}