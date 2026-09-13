package br.com.fiap.clyvovet.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.ExameRequest;
import br.com.fiap.clyvovet.dto.response.ExameResponse;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Exame;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.PetRepository;

@Component
public class ExameMapper {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Exame toEntity(ExameRequest request) {
        if (request == null) return null;
        Exame exame = new Exame();
        exame.setTipo(request.getTipo());
        exame.setDataRealizacao(request.getDataRealizacao());
        exame.setResultado(request.getResultado());
        exame.setUrlArquivo(request.getUrlArquivo());
        exame.setObservacoes(request.getObservacoes());
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId()).orElse(null);
            exame.setPet(pet);
        }
        if (request.getConsultaId() != null) {
            Consulta consulta = consultaRepository.findById(request.getConsultaId()).orElse(null);
            exame.setConsulta(consulta);
        }
        return exame;
    }

    public ExameResponse toResponse(Exame entity) {
        if (entity == null) return null;
        ExameResponse response = new ExameResponse();
        response.setId(entity.getId());
        response.setTipo(entity.getTipo());
        response.setDataRealizacao(entity.getDataRealizacao());
        response.setResultado(entity.getResultado());
        response.setUrlArquivo(entity.getUrlArquivo());
        response.setObservacoes(entity.getObservacoes());
        if (entity.getPet() != null) {
            response.setPetId(entity.getPet().getId());
            response.setPetNome(entity.getPet().getNome());
        }
        if (entity.getConsulta() != null) {
            response.setConsultaId(entity.getConsulta().getId());
        }
        return response;
    }
}