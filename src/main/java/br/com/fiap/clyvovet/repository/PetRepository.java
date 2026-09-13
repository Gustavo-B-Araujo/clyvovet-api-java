package br.com.fiap.clyvovet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.clyvovet.model.Pet;
import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Sexo;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByNomeContainingIgnoreCase(String nome);

    List<Pet> findByEspecie(Especie especie);
    Page<Pet> findByEspecie(Especie especie, Pageable pageable); // já existia

    List<Pet> findByRacaContainingIgnoreCase(String raca);
    Page<Pet> findByRacaContainingIgnoreCase(String raca, Pageable pageable);
    List<Pet> findByTutorId(Long tutorId);
    Page<Pet> findByTutorId(Long tutorId, Pageable pageable);

    @Query("SELECT p FROM Pet p WHERE p.castrado = :castrado")
    List<Pet> findByCastrado(@Param("castrado") Boolean castrado);

    @Query("SELECT p FROM Pet p WHERE p.dataNascimento BETWEEN :inicio AND :fim")
    List<Pet> findPetsNascidosNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}