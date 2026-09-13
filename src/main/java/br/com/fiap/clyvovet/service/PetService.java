package br.com.fiap.clyvovet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.PetRequest;
import br.com.fiap.clyvovet.dto.response.PetResponse;
import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.PetMapper;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.AlertaSaudeRepository;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.ExameRepository;
import br.com.fiap.clyvovet.repository.MedicamentoRepository;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.TutorRepository;
import br.com.fiap.clyvovet.repository.VacinaRepository;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private AlertaSaudeRepository alertaSaudeRepository;

    @Transactional
    @CacheEvict(value = "pets", allEntries = true)
    public PetResponse create(PetRequest request) {
        if (!tutorRepository.existsById(request.getTutorId())) {
            throw new ResourceNotFoundException("Tutor não encontrado com id: " + request.getTutorId());
        }
        Pet pet = petMapper.toEntity(request);
        pet = petRepository.save(pet);
        return petMapper.toResponse(pet);
    }

    @Cacheable(value = "pets", key = "#id")
    public PetResponse findById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id: " + id));
        return petMapper.toResponse(pet);
    }

    @Cacheable(value = "pets")
    public Page<PetResponse> findAll(Pageable pageable) {
        return petRepository.findAll(pageable)
                .map(petMapper::toResponse);
    }


    @Cacheable(value = "pets")
    public Page<PetResponse> findAll(Especie especie, String raca, Long tutorId, Pageable pageable) {
        Page<Pet> page;
        if (especie != null) {
            page = petRepository.findByEspecie(especie, pageable);
        } else if (raca != null && !raca.isEmpty()) {
            page = petRepository.findByRacaContainingIgnoreCase(raca, pageable);
        } else if (tutorId != null) {
            page = petRepository.findByTutorId(tutorId, pageable);
        } else {
            page = petRepository.findAll(pageable);
        }
        return page.map(petMapper::toResponse);
    }


    @Transactional
    @CacheEvict(value = "pets", allEntries = true)
    public PetResponse update(Long id, PetRequest request) {
        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet não encontrado para atualização com id: " + id);
        }
        if (!tutorRepository.existsById(request.getTutorId())) {
            throw new ResourceNotFoundException("Tutor não encontrado com id: " + request.getTutorId());
        }
        Pet pet = petMapper.toEntity(request);
        pet.setId(id);
        pet = petRepository.save(pet);
        return petMapper.toResponse(pet);
    }

    @Transactional
    @CacheEvict(value = { "pets", "vacinas", "consultas", "medicamentos", "exames", "alertas" }, allEntries = true)
    public void delete(Long id) {
        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet não encontrado para exclusão com id: " + id);
        }
        // Remove primeiro os registros que dependem do pet — exame e medicamento
        // também podem referenciar uma consulta, por isso saem antes dela — para
        // não violar as constraints de integridade referencial (ORA-02292) ao
        // excluir o pet.
        exameRepository.deleteByPetId(id);
        medicamentoRepository.deleteByPetId(id);
        consultaRepository.deleteByPetId(id);
        vacinaRepository.deleteByPetId(id);
        alertaSaudeRepository.deleteByPetId(id);
        petRepository.deleteById(id);
    }
}