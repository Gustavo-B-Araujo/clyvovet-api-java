package br.com.fiap.clyvovet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.clyvovet.model.Veterinario;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    // Listas sem paginação
    List<Veterinario> findByNomeContainingIgnoreCase(String nome);
    Veterinario findByCrmv(String crmv);
    List<Veterinario> findByEspecialidadeContainingIgnoreCase(String especialidade);
    List<Veterinario> findByClinicaId(Long clinicaId);

    // Versões paginadas (para filtros do controller)
    Page<Veterinario> findByEspecialidadeContainingIgnoreCase(String especialidade, Pageable pageable);
    Page<Veterinario> findByClinicaId(Long clinicaId, Pageable pageable);
}