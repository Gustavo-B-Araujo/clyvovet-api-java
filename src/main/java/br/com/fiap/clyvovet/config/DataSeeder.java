package br.com.fiap.clyvovet.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Role;
import br.com.fiap.clyvovet.enums.Sexo;
import br.com.fiap.clyvovet.model.Clinica;
import br.com.fiap.clyvovet.model.Endereco;
import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.model.Tutor;
import br.com.fiap.clyvovet.model.Usuario;
import br.com.fiap.clyvovet.model.Veterinario;
import br.com.fiap.clyvovet.repository.ClinicaRepository;
import br.com.fiap.clyvovet.repository.PetRepository;
import br.com.fiap.clyvovet.repository.TutorRepository;
import br.com.fiap.clyvovet.repository.UsuarioRepository;
import br.com.fiap.clyvovet.repository.VeterinarioRepository;

/**
 * Substitui o antigo seed do Flyway (V3__seed_dados.sql): garante, na subida da aplicação,
 * uma massa mínima de dados para demonstrar o login e os fluxos completos da aplicação web.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ClinicaRepository clinicaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ClinicaRepository clinicaRepository,
                       VeterinarioRepository veterinarioRepository,
                       TutorRepository tutorRepository,
                       PetRepository petRepository,
                       UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.clinicaRepository = clinicaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.tutorRepository = tutorRepository;
        this.petRepository = petRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Clinica clinica = clinicaRepository.findByCnpj("12.345.678/0001-90");
        if (clinica == null) {
            clinica = new Clinica();
            clinica.setNome("Clínica ClyvoVet Centro");
            clinica.setCnpj("12.345.678/0001-90");
            clinica.setEmail("contato@clyvovet.com.br");
            clinica.setTelefone("(11) 4002-8922");
            clinica.setEndereco(new Endereco("Av. Paulista", "1000", null, "Bela Vista", "São Paulo", "SP", "01310-100"));
            clinica = clinicaRepository.save(clinica);
        }

        Veterinario veterinario = veterinarioRepository.findByCrmv("CRMV-SP 12345");
        if (veterinario == null) {
            veterinario = new Veterinario();
            veterinario.setNome("Dra. Ana Souza");
            veterinario.setCrmv("CRMV-SP 12345");
            veterinario.setEspecialidade("Clínica Geral");
            veterinario.setEmail("ana.souza@clyvovet.com.br");
            veterinario.setTelefone("(11) 98888-1111");
            veterinario.setClinica(clinica);
            veterinario = veterinarioRepository.save(veterinario);
        }

        Tutor carlos = tutorRepository.findByCpf("123.456.789-00");
        if (carlos == null) {
            carlos = new Tutor();
            carlos.setNome("Carlos Mendes");
            carlos.setEmail("carlos.mendes@email.com");
            carlos.setTelefone("(11) 97777-2222");
            carlos.setCpf("123.456.789-00");
            carlos.setDataNascimento(LocalDate.of(1990, 4, 12));
            carlos.setEndereco(new Endereco("Rua das Flores", "200", null, "Jardins", "São Paulo", "SP", "01401-000"));
            carlos = tutorRepository.save(carlos);
        }

        Tutor juliana = tutorRepository.findByCpf("987.654.321-00");
        if (juliana == null) {
            juliana = new Tutor();
            juliana.setNome("Juliana Farias");
            juliana.setEmail("juliana.farias@email.com");
            juliana.setTelefone("(11) 96666-3333");
            juliana.setCpf("987.654.321-00");
            juliana.setDataNascimento(LocalDate.of(1988, 9, 30));
            juliana.setEndereco(new Endereco("Rua Augusta", "500", null, "Consolação", "São Paulo", "SP", "01305-000"));
            juliana = tutorRepository.save(juliana);
        }

        if (petRepository.findByTutorId(carlos.getId()).isEmpty()) {
            Pet thor = new Pet();
            thor.setNome("Thor");
            thor.setEspecie(Especie.CACHORRO);
            thor.setRaca("Labrador");
            thor.setDataNascimento(LocalDate.of(2021, 2, 15));
            thor.setPeso(28.5);
            thor.setSexo(Sexo.MACHO);
            thor.setCastrado(true);
            thor.setTutor(carlos);
            petRepository.save(thor);
        }

        if (petRepository.findByTutorId(juliana.getId()).isEmpty()) {
            Pet mimi = new Pet();
            mimi.setNome("Mimi");
            mimi.setEspecie(Especie.GATO);
            mimi.setRaca("Siamês");
            mimi.setDataNascimento(LocalDate.of(2022, 6, 1));
            mimi.setPeso(4.2);
            mimi.setSexo(Sexo.FEMEA);
            mimi.setCastrado(true);
            mimi.setTutor(juliana);
            petRepository.save(mimi);
        }

        if (usuarioRepository.findByEmail("tutor@clyvovet.com.br").isEmpty()) {
            Usuario usuarioTutor = new Usuario();
            usuarioTutor.setNome(carlos.getNome());
            usuarioTutor.setEmail("tutor@clyvovet.com.br");
            usuarioTutor.setSenha(passwordEncoder.encode("tutor123"));
            usuarioTutor.setRole(Role.TUTOR);
            usuarioTutor.setTutor(carlos);
            usuarioRepository.save(usuarioTutor);
        }

        if (usuarioRepository.findByEmail("veterinario@clyvovet.com.br").isEmpty()) {
            Usuario usuarioVet = new Usuario();
            usuarioVet.setNome(veterinario.getNome());
            usuarioVet.setEmail("veterinario@clyvovet.com.br");
            usuarioVet.setSenha(passwordEncoder.encode("vet123"));
            usuarioVet.setRole(Role.VETERINARIO);
            usuarioVet.setVeterinario(veterinario);
            usuarioRepository.save(usuarioVet);
        }
    }
}
