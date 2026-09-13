package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.AlertaSaudeRequest;
import br.com.fiap.clyvovet.dto.response.AlertaSaudeResponse;
import br.com.fiap.clyvovet.enums.TipoAlerta;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.AlertaSaudeMapper;
import br.com.fiap.clyvovet.model.AlertaSaude;
import br.com.fiap.clyvovet.repository.AlertaSaudeRepository;
import br.com.fiap.clyvovet.repository.PetRepository;

@Service
public class AlertaSaudeService {

    @Autowired
    private AlertaSaudeRepository alertaSaudeRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AlertaSaudeMapper alertaSaudeMapper;

    @Transactional
    @CacheEvict(value = "alertas", allEntries = true)
    public AlertaSaudeResponse create(AlertaSaudeRequest request) {
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        AlertaSaude alerta = alertaSaudeMapper.toEntity(request);
        alerta = alertaSaudeRepository.save(alerta);
        return alertaSaudeMapper.toResponse(alerta);
    }

    @Cacheable(value = "alertas", key = "#id")
    public AlertaSaudeResponse findById(Long id) {
        AlertaSaude alerta = alertaSaudeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado com id: " + id));
        return alertaSaudeMapper.toResponse(alerta);
    }

    @Cacheable(value = "alertas")
    public Page<AlertaSaudeResponse> findAll(Pageable pageable) {
        return alertaSaudeRepository.findAll(pageable)
                .map(alertaSaudeMapper::toResponse);
    }


    @Cacheable(value = "alertas")
    public Page<AlertaSaudeResponse> findAll(Long petId, TipoAlerta tipo, Boolean lido, Pageable pageable) {
        if (petId != null) {
            return alertaSaudeRepository.findByPetId(petId, pageable)
                    .map(alertaSaudeMapper::toResponse);
        }
        if (tipo != null) {
            return alertaSaudeRepository.findByTipoAlerta(tipo, pageable)
                    .map(alertaSaudeMapper::toResponse);
        }
        if (lido != null) {
            if (lido) {

                return alertaSaudeRepository.findByLidoTrue(pageable).map(alertaSaudeMapper::toResponse);
            } else {
                return alertaSaudeRepository.findByLidoFalse(pageable).map(alertaSaudeMapper::toResponse);
            }
        }
        return alertaSaudeRepository.findAll(pageable).map(alertaSaudeMapper::toResponse);
    }


    @Cacheable(value = "alertas")
    public Page<AlertaSaudeResponse> findNaoLidos(Pageable pageable) {
        return alertaSaudeRepository.findByLidoFalse(pageable)
                .map(alertaSaudeMapper::toResponse);
    }

    // Método auxiliar para buscar por petId (usado no PetController)
    @Cacheable(value = "alertas")
    public Page<AlertaSaudeResponse> findByPetId(Long petId, Pageable pageable) {
        return alertaSaudeRepository.findByPetId(petId, pageable)
                .map(alertaSaudeMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "alertas", allEntries = true)
    public AlertaSaudeResponse update(Long id, AlertaSaudeRequest request) {
        if (!alertaSaudeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta não encontrado para atualização com id: " + id);
        }
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        AlertaSaude alerta = alertaSaudeMapper.toEntity(request);
        alerta.setId(id);
        alerta = alertaSaudeRepository.save(alerta);
        return alertaSaudeMapper.toResponse(alerta);
    }

    @Transactional
    @CacheEvict(value = "alertas", allEntries = true)
    public void delete(Long id) {
        if (!alertaSaudeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta não encontrado para exclusão com id: " + id);
        }
        alertaSaudeRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "alertas", allEntries = true)
    public void marcarComoLido(Long id) {
        if (!alertaSaudeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta não encontrado com id: " + id);
        }
        alertaSaudeRepository.marcarComoLido(id);
    }
}