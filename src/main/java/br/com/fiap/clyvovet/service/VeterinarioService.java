package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.VeterinarioRequest;
import br.com.fiap.clyvovet.dto.response.VeterinarioResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.VeterinarioMapper;
import br.com.fiap.clyvovet.model.Veterinario;
import br.com.fiap.clyvovet.repository.ClinicaRepository;
import br.com.fiap.clyvovet.repository.VeterinarioRepository;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private VeterinarioMapper veterinarioMapper;

    @Transactional
    @CacheEvict(value = "veterinarios", allEntries = true)
    public VeterinarioResponse create(VeterinarioRequest request) {
        if (!clinicaRepository.existsById(request.getClinicaId())) {
            throw new ResourceNotFoundException("Clínica não encontrada com id: " + request.getClinicaId());
        }
        Veterinario veterinario = veterinarioMapper.toEntity(request);
        veterinario = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toResponse(veterinario);
    }

    @Cacheable(value = "veterinarios", key = "#id")
    public VeterinarioResponse findById(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id: " + id));
        return veterinarioMapper.toResponse(veterinario);
    }

    @Cacheable(value = "veterinarios")
    public Page<VeterinarioResponse> findAll(Pageable pageable) {
        return veterinarioRepository.findAll(pageable)
                .map(veterinarioMapper::toResponse);
    }


    @Cacheable(value = "veterinarios")
    public Page<VeterinarioResponse> findAll(String especialidade, Long clinicaId, Pageable pageable) {
        if (especialidade != null && !especialidade.isEmpty()) {
            return veterinarioRepository.findByEspecialidadeContainingIgnoreCase(especialidade, pageable)
                    .map(veterinarioMapper::toResponse);
        }
        if (clinicaId != null) {
            return veterinarioRepository.findByClinicaId(clinicaId, pageable)
                    .map(veterinarioMapper::toResponse);
        }
        return veterinarioRepository.findAll(pageable).map(veterinarioMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "veterinarios", allEntries = true)
    public VeterinarioResponse update(Long id, VeterinarioRequest request) {
        if (!veterinarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veterinário não encontrado para atualização com id: " + id);
        }
        if (!clinicaRepository.existsById(request.getClinicaId())) {
            throw new ResourceNotFoundException("Clínica não encontrada com id: " + request.getClinicaId());
        }
        Veterinario veterinario = veterinarioMapper.toEntity(request);
        veterinario.setId(id);
        veterinario = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toResponse(veterinario);
    }

    @Transactional
    @CacheEvict(value = "veterinarios", allEntries = true)
    public void delete(Long id) {
        if (!veterinarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veterinário não encontrado para exclusão com id: " + id);
        }
        veterinarioRepository.deleteById(id);
    }
}