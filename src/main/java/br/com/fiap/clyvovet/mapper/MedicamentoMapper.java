package br.com.fiap.clyvovet.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.dto.request.MedicamentoRequest;
import br.com.fiap.clyvovet.dto.response.MedicamentoResponse;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Medicamento;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.PetRepository;

@Component
public class MedicamentoMapper {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Medicamento toEntity(MedicamentoRequest request) {
        if (request == null) return null;
        Medicamento medicamento = new Medicamento();
        medicamento.setNome(request.getNome());
        medicamento.setDosagem(request.getDosagem());
        medicamento.setFrequencia(request.getFrequencia());
        medicamento.setDataInicio(request.getDataInicio());
        medicamento.setDataFim(request.getDataFim());
        medicamento.setStatusMedicamento(request.getStatusMedicamento());
        medicamento.setObservacoes(request.getObservacoes());
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId()).orElse(null);
            medicamento.setPet(pet);
        }
        if (request.getConsultaId() != null) {
            Consulta consulta = consultaRepository.findById(request.getConsultaId()).orElse(null);
            medicamento.setConsulta(consulta);
        }
        return medicamento;
    }

    public MedicamentoResponse toResponse(Medicamento entity) {
        if (entity == null) return null;
        MedicamentoResponse response = new MedicamentoResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setDosagem(entity.getDosagem());
        response.setFrequencia(entity.getFrequencia());
        response.setDataInicio(entity.getDataInicio());
        response.setDataFim(entity.getDataFim());
        response.setStatusMedicamento(entity.getStatusMedicamento());
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