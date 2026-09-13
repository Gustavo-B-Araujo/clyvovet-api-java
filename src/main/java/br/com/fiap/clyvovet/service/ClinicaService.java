package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.ClinicaRequest;
import br.com.fiap.clyvovet.dto.response.ClinicaResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.ClinicaMapper;
import br.com.fiap.clyvovet.model.Clinica;
import br.com.fiap.clyvovet.repository.ClinicaRepository;

@Service
public class ClinicaService {

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private ClinicaMapper clinicaMapper;

    @Transactional
    @CacheEvict(value = "clinicas", allEntries = true)
    public ClinicaResponse create(ClinicaRequest request) {
        Clinica clinica = clinicaMapper.toEntity(request);
        clinica = clinicaRepository.save(clinica);
        return clinicaMapper.toResponse(clinica);
    }

    @Cacheable(value = "clinicas", key = "#id")
    public ClinicaResponse findById(Long id) {
        Clinica clinica = clinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada com id: " + id));
        return clinicaMapper.toResponse(clinica);
    }

    @Cacheable(value = "clinicas")
    public Page<ClinicaResponse> findAll(Pageable pageable) {
        return clinicaRepository.findAll(pageable)
                .map(clinicaMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "clinicas", allEntries = true)
    public ClinicaResponse update(Long id, ClinicaRequest request) {
        if (!clinicaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clínica não encontrada para atualização com id: " + id);
        }
        Clinica clinica = clinicaMapper.toEntity(request);
        clinica.setId(id);
        clinica = clinicaRepository.save(clinica);
        return clinicaMapper.toResponse(clinica);
    }

    @Transactional
    @CacheEvict(value = "clinicas", allEntries = true)
    public void delete(Long id) {
        if (!clinicaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clínica não encontrada para exclusão com id: " + id);
        }
        clinicaRepository.deleteById(id);
    }
}