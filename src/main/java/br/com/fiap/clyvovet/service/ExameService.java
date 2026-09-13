package br.com.fiap.clyvovet.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.ExameRequest;
import br.com.fiap.clyvovet.dto.response.ExameResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.ExameMapper;
import br.com.fiap.clyvovet.model.Exame;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.ExameRepository;
import br.com.fiap.clyvovet.repository.PetRepository;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ExameMapper exameMapper;

    @Transactional
    @CacheEvict(value = "exames", allEntries = true)
    public ExameResponse create(ExameRequest request) {
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        if (request.getConsultaId() != null && !consultaRepository.existsById(request.getConsultaId())) {
            throw new ResourceNotFoundException("Consulta não encontrada com id: " + request.getConsultaId());
        }
        Exame exame = exameMapper.toEntity(request);
        exame = exameRepository.save(exame);
        return exameMapper.toResponse(exame);
    }

    @Cacheable(value = "exames", key = "#id")
    public ExameResponse findById(Long id) {
        Exame exame = exameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado com id: " + id));
        return exameMapper.toResponse(exame);
    }

    @Cacheable(value = "exames")
    public Page<ExameResponse> findAll(Pageable pageable) {
        return exameRepository.findAll(pageable)
                .map(exameMapper::toResponse);
    }


    @Cacheable(value = "exames")
    public Page<ExameResponse> findAll(Long petId, String tipo, Long consultaId,
                                       LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (petId != null) {
            return exameRepository.findByPetId(petId, pageable).map(exameMapper::toResponse);
        }
        if (consultaId != null) {
            return exameRepository.findByConsultaId(consultaId, pageable).map(exameMapper::toResponse);
        }
        if (tipo != null && !tipo.isEmpty()) {
            return exameRepository.findByTipoContainingIgnoreCase(tipo, pageable).map(exameMapper::toResponse);
        }
        if (dataInicio != null && dataFim != null) {
            return exameRepository.findByDataRealizacaoBetween(dataInicio, dataFim, pageable).map(exameMapper::toResponse);
        }
        return exameRepository.findAll(pageable).map(exameMapper::toResponse);
    }


    @Cacheable(value = "exames")
    public Page<ExameResponse> findByPetId(Long petId, Pageable pageable) {
        return exameRepository.findByPetId(petId, pageable).map(exameMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "exames", allEntries = true)
    public ExameResponse update(Long id, ExameRequest request) {
        if (!exameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exame não encontrado para atualização com id: " + id);
        }
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        if (request.getConsultaId() != null && !consultaRepository.existsById(request.getConsultaId())) {
            throw new ResourceNotFoundException("Consulta não encontrada com id: " + request.getConsultaId());
        }
        Exame exame = exameMapper.toEntity(request);
        exame.setId(id);
        exame = exameRepository.save(exame);
        return exameMapper.toResponse(exame);
    }

    @Transactional
    @CacheEvict(value = "exames", allEntries = true)
    public void delete(Long id) {
        if (!exameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exame não encontrado para exclusão com id: " + id);
        }
        exameRepository.deleteById(id);
    }
}