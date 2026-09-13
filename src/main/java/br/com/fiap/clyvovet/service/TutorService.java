package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.TutorRequest;
import br.com.fiap.clyvovet.dto.response.TutorResponse;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.TutorMapper;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.repository.TutorRepository;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private TutorMapper tutorMapper;

    @Transactional
    @CacheEvict(value = { "tutores", "tutoresPorEmail" }, allEntries = true)
    public TutorResponse create(TutorRequest request) {
        Tutor tutor = tutorMapper.toEntity(request);
        tutor = tutorRepository.save(tutor);
        return tutorMapper.toResponse(tutor);
    }

    @Cacheable(value = "tutores", key = "#id")
    public TutorResponse findById(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com id: " + id));
        return tutorMapper.toResponse(tutor);
    }

    @Cacheable(value = "tutoresPorEmail", key = "#email")
    public TutorResponse findByEmail(String email) {
        Tutor tutor = tutorRepository.findByEmail(email);
        if (tutor == null) {
            throw new ResourceNotFoundException("Tutor não encontrado com email: " + email);
        }
        return tutorMapper.toResponse(tutor);
    }

    @Cacheable(value = "tutores")
    public Page<TutorResponse> findAll(Pageable pageable) {
        return tutorRepository.findAll(pageable)
                .map(tutorMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = { "tutores", "tutoresPorEmail" }, allEntries = true)
    public TutorResponse update(Long id, TutorRequest request) {
        Tutor existingTutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado para atualização com id: " + id));
        Tutor updatedTutor = tutorMapper.toEntity(request);
        updatedTutor.setId(existingTutor.getId());
        updatedTutor = tutorRepository.save(updatedTutor);
        return tutorMapper.toResponse(updatedTutor);
    }

    @Transactional
    @CacheEvict(value = { "tutores", "tutoresPorEmail" }, allEntries = true)
    public void delete(Long id) {
        if (!tutorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tutor não encontrado para exclusão com id: " + id);
        }
        tutorRepository.deleteById(id);
    }
}