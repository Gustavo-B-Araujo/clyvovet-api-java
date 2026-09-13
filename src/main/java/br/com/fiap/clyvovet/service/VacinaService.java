package br.com.fiap.clyvovet.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.VacinaRequest;
import br.com.fiap.clyvovet.dto.response.VacinaResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.VacinaMapper;
import br.com.fiap.clyvovet.model.Vacina;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.VacinaRepository;

@Service
public class VacinaService {

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VacinaMapper vacinaMapper;

    @Transactional
    @CacheEvict(value = "vacinas", allEntries = true)
    public VacinaResponse create(VacinaRequest request) {
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        Vacina vacina = vacinaMapper.toEntity(request);
        vacina = vacinaRepository.save(vacina);
        return vacinaMapper.toResponse(vacina);
    }

    @Cacheable(value = "vacinas", key = "#id")
    public VacinaResponse findById(Long id) {
        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacina não encontrada com id: " + id));
        return vacinaMapper.toResponse(vacina);
    }

    @Cacheable(value = "vacinas")
    public Page<VacinaResponse> findAll(Pageable pageable) {
        return vacinaRepository.findAll(pageable)
                .map(vacinaMapper::toResponse);
    }


    @Cacheable(value = "vacinas")
    public Page<VacinaResponse> findAll(Long petId, String nome, Pageable pageable) {
        if (petId != null) {
            return vacinaRepository.findByPetId(petId, pageable).map(vacinaMapper::toResponse);
        }
        if (nome != null && !nome.isEmpty()) {
            return vacinaRepository.findByNomeContainingIgnoreCase(nome, pageable).map(vacinaMapper::toResponse);
        }
        return vacinaRepository.findAll(pageable).map(vacinaMapper::toResponse);
    }


    @Cacheable(value = "vacinas")
    public Page<VacinaResponse> findVacinasVencidas(Pageable pageable) {
        return vacinaRepository.findVacinasVencidas(LocalDate.now(), pageable)
                .map(vacinaMapper::toResponse);
    }


    @Cacheable(value = "vacinas")
    public Page<VacinaResponse> findVacinasProximas(Pageable pageable) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);
        return vacinaRepository.findVacinasProximas(hoje, limite, pageable)
                .map(vacinaMapper::toResponse);
    }


    @Cacheable(value = "vacinas")
    public Page<VacinaResponse> findByPetId(Long petId, Pageable pageable) {
        return vacinaRepository.findByPetId(petId, pageable).map(vacinaMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "vacinas", allEntries = true)
    public VacinaResponse update(Long id, VacinaRequest request) {
        if (!vacinaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vacina não encontrada para atualização com id: " + id);
        }
        if (!petRepository.existsById(request.getPetId())) {
            throw new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId());
        }
        Vacina vacina = vacinaMapper.toEntity(request);
        vacina.setId(id);
        vacina = vacinaRepository.save(vacina);
        return vacinaMapper.toResponse(vacina);
    }

    @Transactional
    @CacheEvict(value = "vacinas", allEntries = true)
    public void delete(Long id) {
        if (!vacinaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vacina não encontrada para exclusão com id: " + id);
        }
        vacinaRepository.deleteById(id);
    }
}