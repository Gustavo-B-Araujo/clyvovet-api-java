package br.com.fiap.clyvovet.service;

import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.web.AgendamentoConsultaForm;
import br.com.fiap.clyvovet.enums.StatusConsulta;
import br.com.fiap.clyvovet.exception.HorarioIndisponivelException;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.Veterinario;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.VeterinarioRepository;

/**
 * Fluxo completo de agendamento de consulta pelo tutor: valida que o pet pertence
 * ao tutor autenticado e que o veterinário está livre no horário escolhido antes
 * de efetivar o agendamento.
 */
@Service
public class AgendamentoConsultaService {

    private static final Set<StatusConsulta> STATUS_ALTERAVEIS =
            EnumSet.of(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Transactional
    public Consulta agendar(Long tutorId, AgendamentoConsultaForm form) {
        Pet pet = petRepository.findById(form.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet", form.getPetId()));

        if (!pet.getTutor().getId().equals(tutorId)) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }

        Veterinario veterinario = veterinarioRepository.findById(form.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário", form.getVeterinarioId()));

        return criarConsulta(pet, veterinario, form);
    }

    /**
     * Fluxo de cadastro de consulta pelo próprio veterinário: o veterinário é o
     * autenticado (sem checagem de posse), e qualquer pet cadastrado no sistema
     * pode ser selecionado.
     */
    @Transactional
    public Consulta agendarPeloVeterinario(Long veterinarioId, AgendamentoConsultaForm form) {
        Pet pet = petRepository.findById(form.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet", form.getPetId()));

        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário", veterinarioId));

        return criarConsulta(pet, veterinario, form);
    }

    private Consulta criarConsulta(Pet pet, Veterinario veterinario, AgendamentoConsultaForm form) {
        if (consultaRepository.existsByVeterinarioIdAndDataHora(veterinario.getId(), form.getDataHora())) {
            throw new HorarioIndisponivelException(
                    "O veterinário " + veterinario.getNome() + " já possui uma consulta agendada neste horário");
        }

        Consulta consulta = new Consulta();
        consulta.setPet(pet);
        consulta.setVeterinario(veterinario);
        consulta.setDataHora(form.getDataHora());
        consulta.setTipoConsulta(form.getTipoConsulta());
        consulta.setStatusConsulta(StatusConsulta.AGENDADA);
        consulta.setObservacoes(form.getObservacoes());

        return consultaRepository.save(consulta);
    }

    public Consulta buscarParaEdicao(Long tutorId, Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", consultaId));

        if (!consulta.getPet().getTutor().getId().equals(tutorId)) {
            throw new AccessDeniedException("Esta consulta não pertence ao tutor autenticado");
        }
        if (!STATUS_ALTERAVEIS.contains(consulta.getStatusConsulta())) {
            throw new IllegalStateException(
                    "Consulta com status " + consulta.getStatusConsulta() + " não pode ser alterada");
        }
        return consulta;
    }

    @Transactional
    public Consulta cancelar(Long tutorId, Long consultaId) {
        Consulta consulta = buscarParaEdicao(tutorId, consultaId);
        consulta.setStatusConsulta(StatusConsulta.CANCELADA);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta remarcar(Long tutorId, Long consultaId, AgendamentoConsultaForm form) {
        Consulta consulta = buscarParaEdicao(tutorId, consultaId);

        Veterinario veterinario = veterinarioRepository.findById(form.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário", form.getVeterinarioId()));

        if (consultaRepository.existsByVeterinarioIdAndDataHoraAndIdNot(
                veterinario.getId(), form.getDataHora(), consultaId)) {
            throw new HorarioIndisponivelException(
                    "O veterinário " + veterinario.getNome() + " já possui uma consulta agendada neste horário");
        }

        consulta.setVeterinario(veterinario);
        consulta.setDataHora(form.getDataHora());
        consulta.setTipoConsulta(form.getTipoConsulta());
        consulta.setObservacoes(form.getObservacoes());
        consulta.setStatusConsulta(StatusConsulta.AGENDADA);

        return consultaRepository.save(consulta);
    }
}
