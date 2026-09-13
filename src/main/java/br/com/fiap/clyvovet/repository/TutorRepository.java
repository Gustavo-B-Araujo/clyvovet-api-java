package br.com.fiap.clyvovet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.clyvovet.model.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // Buscas por nome, email, cpf e etc
    List<Tutor> findByNomeContainingIgnoreCase(String nome);
    Tutor findByEmail(String email);
    Tutor findByCpf(String cpf);
    Page<Tutor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);


    @Query("SELECT DISTINCT t FROM Tutor t JOIN t.pets p WHERE p.especie = :especie")
    List<Tutor> findTutoresByPetEspecie(@Param("especie") String especie);
}