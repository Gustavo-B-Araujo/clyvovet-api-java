package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.MedicamentoRequest;
import br.com.fiap.clyvovet.dto.response.MedicamentoResponse;
import br.com.fiap.clyvovet.enums.StatusMedicamento;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.MedicamentoMapper;
import br.com.fiap.clyvovet.model.Medicamento;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.MedicamentoRepository;
import br.com.fiap.clyvovet.repository.PetRepository;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicamentoMapper medicamentoMapper;

    @Transactional
    @CacheEvict(value = "medicamentos", allEntries = true)
    public MedicamentoResponse create(MedicamentoRequest request) {
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        if (request.getConsultaId() != null && !consultaRepository.existsById(request.getConsultaId())) {
            throw new ResourceNotFoundException("Consulta não encontrada com id: " + request.getConsultaId());
        }
        Medicamento medicamento = medicamentoMapper.toEntity(request);
        medicamento = medicamentoRepository.save(medicamento);
        return medicamentoMapper.toResponse(medicamento);
    }

    @Cacheable(value = "medicamentos", key = "#id")
    public MedicamentoResponse findById(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado com id: " + id));
        return medicamentoMapper.toResponse(medicamento);
    }

    @Cacheable(value = "medicamentos")
    public Page<MedicamentoResponse> findAll(Pageable pageable) {
        return medicamentoRepository.findAll(pageable)
                .map(medicamentoMapper::toResponse);
    }


    @Cacheable(value = "medicamentos")
    public Page<MedicamentoResponse> findAll(Long petId, StatusMedicamento status, Long consultaId, Pageable pageable) {
        if (petId != null) {
            return medicamentoRepository.findByPetId(petId, pageable).map(medicamentoMapper::toResponse);
        }
        if (status != null) {
            return medicamentoRepository.findByStatusMedicamento(status, pageable).map(medicamentoMapper::toResponse);
        }
        if (consultaId != null) {
            return medicamentoRepository.findByConsultaId(consultaId, pageable).map(medicamentoMapper::toResponse);
        }
        return medicamentoRepository.findAll(pageable).map(medicamentoMapper::toResponse);
    }


    @Cacheable(value = "medicamentos")
    public Page<MedicamentoResponse> findMedicamentosAtivos(Pageable pageable) {
        return medicamentoRepository.findByStatusMedicamento(StatusMedicamento.ATIVO, pageable)
                .map(medicamentoMapper::toResponse);
    }


    @Cacheable(value = "medicamentos")
    public Page<MedicamentoResponse> findByPetId(Long petId, Pageable pageable) {
        return medicamentoRepository.findByPetId(petId, pageable).map(medicamentoMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "medicamentos", allEntries = true)
    public MedicamentoResponse update(Long id, MedicamentoRequest request) {
        if (!medicamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicamento não encontrado para atualização com id: " + id);
        }
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        if (request.getConsultaId() != null && !consultaRepository.existsById(request.getConsultaId())) {
            throw new ResourceNotFoundException("Consulta não encontrada com id: " + request.getConsultaId());
        }
        Medicamento medicamento = medicamentoMapper.toEntity(request);
        medicamento.setId(id);
        medicamento = medicamentoRepository.save(medicamento);
        return medicamentoMapper.toResponse(medicamento);
    }

    @Transactional
    @CacheEvict(value = "medicamentos", allEntries = true)
    public void delete(Long id) {
        if (!medicamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicamento não encontrado para exclusão com id: " + id);
        }
        medicamentoRepository.deleteById(id);
    }
}