package br.com.fiap.clyvovet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.clyvovet.dto.web.AtendimentoConsultaForm;
import br.com.fiap.clyvovet.enums.StatusConsulta;
import br.com.fiap.clyvovet.enums.TipoAlerta;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.model.AlertaSaude;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Vacina;
import br.com.fiap.clyvovet.repository.AlertaSaudeRepository;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.VacinaRepository;

/**
 * Fluxo completo de atendimento de consulta pelo veterinário: conclui a consulta e,
 * de acordo com as decisões clínicas registradas, aplica uma vacina e/ou agenda um
 * retorno, gerando automaticamente os alertas de saúde correspondentes para o pet.
 */
@Service
public class AtendimentoConsultaService {

    private static final Set<StatusConsulta> STATUS_ATENDIVEIS =
            EnumSet.of(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA, StatusConsulta.EM_ANDAMENTO);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private AlertaSaudeRepository alertaSaudeRepository;

    @Transactional
    public Consulta atender(Long veterinarioId, Long consultaId, AtendimentoConsultaForm form) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", consultaId));

        if (!consulta.getVeterinario().getId().equals(veterinarioId)) {
            throw new AccessDeniedException("Esta consulta não pertence ao veterinário autenticado");
        }
        if (!STATUS_ATENDIVEIS.contains(consulta.getStatusConsulta())) {
            throw new IllegalStateException("Consulta com status " + consulta.getStatusConsulta() + " não pode ser atendida");
        }

        consulta.setDiagnostico(form.getDiagnostico());
        consulta.setTratamento(form.getTratamento());
        consulta.setStatusConsulta(StatusConsulta.CONCLUIDA);
        consulta = consultaRepository.save(consulta);

        if (form.isAplicarVacina()) {
            registrarVacina(consulta, form);
        }
        if (form.isNecessitaRetorno()) {
            registrarAlertaRetorno(consulta, form.getRetornoDias());
        }

        return consulta;
    }

    private void registrarVacina(Consulta consulta, AtendimentoConsultaForm form) {
        LocalDate hoje = LocalDate.now();
        LocalDate proximaDose = hoje.plusDays(form.getVacinaDiasProximaDose());

        Vacina vacina = new Vacina();
        vacina.setPet(consulta.getPet());
        vacina.setNome(form.getVacinaNome());
        vacina.setFabricante(form.getVacinaFabricante());
        vacina.setDataAplicacao(hoje);
        vacina.setDataProximaDose(proximaDose);
        vacina.setObservacoes("Aplicada durante a consulta #" + consulta.getId());
        vacinaRepository.save(vacina);

        AlertaSaude alerta = new AlertaSaude();
        alerta.setPet(consulta.getPet());
        alerta.setTipoAlerta(TipoAlerta.VACINA_PROXIMA);
        alerta.setMensagem("Próxima dose da vacina " + form.getVacinaNome() + " prevista para " + proximaDose);
        alerta.setDataGeracao(LocalDateTime.now());
        alerta.setLido(false);
        alertaSaudeRepository.save(alerta);
    }

    private void registrarAlertaRetorno(Consulta consulta, Integer dias) {
        LocalDate dataRetorno = LocalDate.now().plusDays(dias);

        AlertaSaude alerta = new AlertaSaude();
        alerta.setPet(consulta.getPet());
        alerta.setTipoAlerta(TipoAlerta.RETORNO_PENDENTE);
        alerta.setMensagem("Retorno recomendado a partir de " + dataRetorno
                + " referente à consulta #" + consulta.getId());
        alerta.setDataGeracao(LocalDateTime.now());
        alerta.setLido(false);
        alertaSaudeRepository.save(alerta);
    }
}
