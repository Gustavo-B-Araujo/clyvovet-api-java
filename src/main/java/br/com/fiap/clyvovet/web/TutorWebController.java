package br.com.fiap.clyvovet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.clyvovet.dto.web.AgendamentoConsultaForm;
import br.com.fiap.clyvovet.dto.web.NovoPetForm;
import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Sexo;
import br.com.fiap.clyvovet.exception.HorarioIndisponivelException;
import br.com.fiap.clyvovet.exception.ResourceNotFoundException;
import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.repository.AlertaSaudeRepository;
import br.com.fiap.clyvovet.repository.ConsultaRepository;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.TutorRepository;
import br.com.fiap.clyvovet.repository.VeterinarioRepository;
import br.com.fiap.clyvovet.security.UsuarioAutenticado;
import br.com.fiap.clyvovet.service.AgendamentoConsultaService;
import br.com.fiap.clyvovet.service.AlertaSaudeService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/app/tutor")
public class TutorWebController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private AlertaSaudeRepository alertaSaudeRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AlertaSaudeService alertaSaudeService;

    @Autowired
    private AgendamentoConsultaService agendamentoConsultaService;

    @GetMapping("/pets")
    public String meusPets(@AuthenticationPrincipal UsuarioAutenticado usuario, Model model) {
        model.addAttribute("pets", petRepository.findByTutorId(usuario.getTutorId()));
        return "tutor/pets";
    }

    @GetMapping("/pets/novo")
    public String novoPetForm(Model model) {
        model.addAttribute("form", new NovoPetForm());
        model.addAttribute("especies", Especie.values());
        model.addAttribute("sexos", Sexo.values());
        return "tutor/novo-pet";
    }

    @PostMapping("/pets")
    public String cadastrarPet(@Valid @ModelAttribute("form") NovoPetForm form, BindingResult result,
                                @AuthenticationPrincipal UsuarioAutenticado usuario, Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("especies", Especie.values());
            model.addAttribute("sexos", Sexo.values());
            return "tutor/novo-pet";
        }
        Tutor tutor = tutorRepository.findById(usuario.getTutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor", usuario.getTutorId()));

        Pet pet = new Pet();
        pet.setNome(form.getNome());
        pet.setEspecie(form.getEspecie());
        pet.setRaca(form.getRaca());
        pet.setDataNascimento(form.getDataNascimento());
        pet.setPeso(form.getPeso());
        pet.setSexo(form.getSexo());
        pet.setCastrado(form.isCastrado());
        pet.setTutor(tutor);
        petRepository.save(pet);

        redirectAttributes.addFlashAttribute("mensagem", "Pet cadastrado com sucesso!");
        return "redirect:/app/tutor/pets";
    }

    @GetMapping("/pets/{id}")
    public String detalhePet(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario, Model model) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", id));
        if (!pet.getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }
        model.addAttribute("pet", pet);
        return "tutor/pet-detalhe";
    }

    @GetMapping("/pets/{id}/editar")
    public String editarPetForm(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario, Model model) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", id));
        if (!pet.getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }
        NovoPetForm form = new NovoPetForm();
        form.setNome(pet.getNome());
        form.setEspecie(pet.getEspecie());
        form.setRaca(pet.getRaca());
        form.setDataNascimento(pet.getDataNascimento());
        form.setPeso(pet.getPeso());
        form.setSexo(pet.getSexo());
        form.setCastrado(Boolean.TRUE.equals(pet.getCastrado()));

        model.addAttribute("pet", pet);
        model.addAttribute("form", form);
        model.addAttribute("especies", Especie.values());
        model.addAttribute("sexos", Sexo.values());
        return "tutor/editar-pet";
    }

    @PostMapping("/pets/{id}/editar")
    public String editarPet(@PathVariable Long id, @Valid @ModelAttribute("form") NovoPetForm form, BindingResult result,
                             @AuthenticationPrincipal UsuarioAutenticado usuario, Model model,
                             RedirectAttributes redirectAttributes) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", id));
        if (!pet.getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }
        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            model.addAttribute("especies", Especie.values());
            model.addAttribute("sexos", Sexo.values());
            return "tutor/editar-pet";
        }

        pet.setNome(form.getNome());
        pet.setEspecie(form.getEspecie());
        pet.setRaca(form.getRaca());
        pet.setDataNascimento(form.getDataNascimento());
        pet.setPeso(form.getPeso());
        pet.setSexo(form.getSexo());
        pet.setCastrado(form.isCastrado());
        petRepository.save(pet);

        redirectAttributes.addFlashAttribute("mensagem", "Pet atualizado com sucesso!");
        return "redirect:/app/tutor/pets/" + id;
    }

    @PostMapping("/pets/{id}/excluir")
    public String excluirPet(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario,
                              RedirectAttributes redirectAttributes) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", id));
        if (!pet.getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }
        boolean possuiHistorico = !pet.getConsultas().isEmpty() || !pet.getVacinas().isEmpty()
                || !pet.getMedicamentos().isEmpty() || !pet.getExames().isEmpty() || !pet.getAlertas().isEmpty();
        if (possuiHistorico) {
            throw new IllegalStateException("Não é possível excluir um pet que já possui consultas, vacinas ou alertas registrados.");
        }
        petRepository.delete(pet);

        redirectAttributes.addFlashAttribute("mensagem", "Pet excluído com sucesso!");
        return "redirect:/app/tutor/pets";
    }

    @GetMapping("/consultas/nova")
    public String novaConsultaForm(@RequestParam Long petId, @AuthenticationPrincipal UsuarioAutenticado usuario,
                                    Model model) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet", petId));
        if (!pet.getTutor().getId().equals(usuario.getTutorId())) {
            throw new AccessDeniedException("Este pet não pertence ao tutor autenticado");
        }
        AgendamentoConsultaForm form = new AgendamentoConsultaForm();
        form.setPetId(petId);
        model.addAttribute("pet", pet);
        model.addAttribute("form", form);
        model.addAttribute("veterinarios", veterinarioRepository.findAll());
        return "tutor/nova-consulta";
    }

    @PostMapping("/consultas")
    public String agendarConsulta(@Valid @ModelAttribute("form") AgendamentoConsultaForm form, BindingResult result,
                                   @AuthenticationPrincipal UsuarioAutenticado usuario, Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pet", petRepository.findById(form.getPetId()).orElse(null));
            model.addAttribute("veterinarios", veterinarioRepository.findAll());
            return "tutor/nova-consulta";
        }
        try {
            agendamentoConsultaService.agendar(usuario.getTutorId(), form);
        } catch (HorarioIndisponivelException e) {
            model.addAttribute("pet", petRepository.findById(form.getPetId()).orElse(null));
            model.addAttribute("veterinarios", veterinarioRepository.findAll());
            model.addAttribute("erroAgendamento", e.getMessage());
            return "tutor/nova-consulta";
        }
        redirectAttributes.addFlashAttribute("mensagem", "Consulta agendada com sucesso!");
        return "redirect:/app/tutor/pets/" + form.getPetId();
    }

    @PostMapping("/consultas/{id}/cancelar")
    public String cancelarConsulta(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario,
                                    RedirectAttributes redirectAttributes) {
        Consulta consulta = agendamentoConsultaService.cancelar(usuario.getTutorId(), id);
        redirectAttributes.addFlashAttribute("mensagem", "Consulta cancelada com sucesso!");
        return "redirect:/app/tutor/pets/" + consulta.getPet().getId();
    }

    @GetMapping("/consultas/{id}/remarcar")
    public String remarcarConsultaForm(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario,
                                        Model model) {
        Consulta consulta = agendamentoConsultaService.buscarParaEdicao(usuario.getTutorId(), id);

        AgendamentoConsultaForm form = new AgendamentoConsultaForm();
        form.setPetId(consulta.getPet().getId());
        form.setVeterinarioId(consulta.getVeterinario().getId());
        form.setDataHora(consulta.getDataHora());
        form.setTipoConsulta(consulta.getTipoConsulta());
        form.setObservacoes(consulta.getObservacoes());

        model.addAttribute("consulta", consulta);
        model.addAttribute("pet", consulta.getPet());
        model.addAttribute("form", form);
        model.addAttribute("veterinarios", veterinarioRepository.findAll());
        return "tutor/remarcar-consulta";
    }

    @PostMapping("/consultas/{id}/remarcar")
    public String remarcarConsulta(@PathVariable Long id, @Valid @ModelAttribute("form") AgendamentoConsultaForm form,
                                    BindingResult result, @AuthenticationPrincipal UsuarioAutenticado usuario,
                                    Model model, RedirectAttributes redirectAttributes) {
        Consulta consulta = consultaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
        if (result.hasErrors()) {
            model.addAttribute("consulta", consulta);
            model.addAttribute("pet", petRepository.findById(form.getPetId()).orElse(null));
            model.addAttribute("veterinarios", veterinarioRepository.findAll());
            return "tutor/remarcar-consulta";
        }
        try {
            agendamentoConsultaService.remarcar(usuario.getTutorId(), id, form);
        } catch (HorarioIndisponivelException e) {
            model.addAttribute("consulta", consulta);
            model.addAttribute("pet", petRepository.findById(form.getPetId()).orElse(null));
            model.addAttribute("veterinarios", veterinarioRepository.findAll());
            model.addAttribute("erroAgendamento", e.getMessage());
            return "tutor/remarcar-consulta";
        }
        redirectAttributes.addFlashAttribute("mensagem", "Consulta remarcada com sucesso!");
        return "redirect:/app/tutor/pets/" + form.getPetId();
    }

    @GetMapping("/alertas")
    public String meusAlertas(@AuthenticationPrincipal UsuarioAutenticado usuario, Model model) {
        model.addAttribute("alertas", alertaSaudeRepository.findByPet_Tutor_IdOrderByDataGeracaoDesc(usuario.getTutorId()));
        return "tutor/alertas";
    }

    @PostMapping("/alertas/{id}/lido")
    public String marcarAlertaComoLido(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario) {
        if (!alertaSaudeRepository.existsByIdAndPet_Tutor_Id(id, usuario.getTutorId())) {
            throw new AccessDeniedException("Este alerta não pertence ao tutor autenticado");
        }
        alertaSaudeService.marcarComoLido(id);
        return "redirect:/app/tutor/alertas";
    }
}
