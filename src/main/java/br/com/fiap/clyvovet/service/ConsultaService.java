package br.com.fiap.clyvovet.service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.request.ConsultaRequest;
import br.com.fiap.clyvovet.dto.response.ConsultaResponse;
import br.com.fiap.clyvovet.enums.Role;
import br.com.fiap.clyvovet.enums.StatusConsulta;
import br.com.fiap.clyvovet.enums.TipoConsulta;
import br.com.fiap.clyvovet.exception.HorarioIndisponivelException;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.mapper.ConsultaMapper;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.VeterinarioRepository;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;

@Service
public class ConsultaService {

    // Só o veterinário pode confirmar, iniciar o atendimento ou concluir uma
    // consulta (ver AtendimentoConsultaService, usado pela área web do vet). O
    // tutor, pela API REST consumida pelo app mobile, só pode agendar ou
    // cancelar — nunca alterar o andamento clínico da consulta.
    private static final Set<StatusConsulta> STATUS_PERMITIDOS_TUTOR =
            EnumSet.of(StatusConsulta.AGENDADA, StatusConsulta.CANCELADA);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ConsultaMapper consultaMapper;

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public ConsultaResponse create(ConsultaRequest request, UsuarioAutenticado usuario) {
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId()));
        if (!veterinarioRepository.existsById(request.getVeterinarioId())) {
            throw new ResourceNotFoundException("Veterinário não encontrado com id: " + request.getVeterinarioId());
        }
        if (consultaRepository.existsByVeterinarioIdAndDataHora(request.getVeterinarioId(), request.getDataHora())) {
            throw new HorarioIndisponivelException("Este veterinário já possui uma consulta agendada neste horário");
        }
        validarPermissaoDoTutor(usuario, pet, request);
        Consulta consulta = consultaMapper.toEntity(request);
        consulta = consultaRepository.save(consulta);
        return consultaMapper.toResponse(consulta);
    }

    @Cacheable(value = "consultas", key = "#id")
    public ConsultaResponse findById(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id: " + id));
        return consultaMapper.toResponse(consulta);
    }

    @Cacheable(value = "consultas")
    public Page<ConsultaResponse> findAll(Pageable pageable) {
        return consultaRepository.findAll(pageable)
                .map(consultaMapper::toResponse);
    }


    @Cacheable(value = "consultas")
    public Page<ConsultaResponse> findAll(TipoConsulta tipo, StatusConsulta status, Long petId, Long veterinarioId,
                                          LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        if (petId != null) {
            return consultaRepository.findByPetId(petId, pageable).map(consultaMapper::toResponse);
        }
        if (veterinarioId != null) {
            return consultaRepository.findByVeterinarioId(veterinarioId, pageable).map(consultaMapper::toResponse);
        }
        if (tipo != null) {
            return consultaRepository.findByTipoConsulta(tipo, pageable).map(consultaMapper::toResponse);
        }
        if (status != null) {
            return consultaRepository.findByStatusConsulta(status, pageable).map(consultaMapper::toResponse);
        }
        if (dataInicio != null && dataFim != null) {
            return consultaRepository.findConsultasNoPeriodo(dataInicio, dataFim, pageable).map(consultaMapper::toResponse);
        }
        return consultaRepository.findAll(pageable).map(consultaMapper::toResponse);
    }

    @Cacheable(value = "consultas")
    public Page<ConsultaResponse> findByPetId(Long petId, Pageable pageable) {
        return consultaRepository.findByPetId(petId, pageable)
                .map(consultaMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public ConsultaResponse update(Long id, ConsultaRequest request, UsuarioAutenticado usuario) {
        Consulta existente = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada para atualização com id: " + id));
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id: " + request.getPetId()));
        if (!veterinarioRepository.existsById(request.getVeterinarioId())) {
            throw new ResourceNotFoundException("Veterinário não encontrado com id: " + request.getVeterinarioId());
        }
        if (usuario != null && usuario.getRole() == Role.TUTOR
                && !existente.getPet().getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Esta consulta não pertence ao tutor autenticado");
        }
        if (consultaRepository.existsByVeterinarioIdAndDataHoraAndIdNot(request.getVeterinarioId(), request.getDataHora(), id)) {
            throw new HorarioIndisponivelException("Este veterinário já possui uma consulta agendada neste horário");
        }
        validarPermissaoDoTutor(usuario, pet, request);
        Consulta consulta = consultaMapper.toEntity(request);
        consulta.setId(id);
        consulta = consultaRepository.save(consulta);
        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public void delete(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta não encontrada para exclusão com id: " + id);
        }
        consultaRepository.deleteById(id);
    }

    // Tutor só pode agendar (AGENDADA) ou cancelar (CANCELADA) uma consulta do
    // próprio pet, e nunca preenche diagnóstico/tratamento — isso é feito pelo
    // veterinário responsável através do fluxo de atendimento da área web
    // (ver AtendimentoConsultaService).
    private void validarPermissaoDoTutor(UsuarioAutenticado usuario, Pet pet, ConsultaRequest request) {
        if (usuario == null || usuario.getRole() != Role.TUTOR) {
            return;
        }
        if (!pet.getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }
        if (!STATUS_PERMITIDOS_TUTOR.contains(request.getStatusConsulta())) {
            throw new AccessDeniedException(
                    "Tutores só podem definir o status da consulta como AGENDADA ou CANCELADA; "
                            + "confirmação e conclusão são feitas pelo veterinário responsável.");
        }
        boolean temDiagnostico = request.getDiagnostico() != null && !request.getDiagnostico().isBlank();
        boolean temTratamento = request.getTratamento() != null && !request.getTratamento().isBlank();
        if (temDiagnostico || temTratamento) {
            throw new AccessDeniedException(
                    "Diagnóstico e tratamento só podem ser preenchidos pelo veterinário responsável pela consulta.");
        }
    }
}