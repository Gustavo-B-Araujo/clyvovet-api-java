package br.com.fiap.clyvovet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.clyvovet.model.Vacina;

public interface VacinaRepository extends JpaRepository<Vacina, Long> {


    List<Vacina> findByPetId(Long petId);
    void deleteByPetId(Long petId);
    List<Vacina> findByNomeContainingIgnoreCase(String nome);
    Page<Vacina> findByPetId(Long petId, Pageable pageable);
    Page<Vacina> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT v FROM Vacina v WHERE v.dataProximaDose < :hoje")
    List<Vacina> findVacinasVencidas(@Param("hoje") LocalDate hoje);


    @Query("SELECT v FROM Vacina v WHERE v.dataProximaDose < :hoje")
    Page<Vacina> findVacinasVencidas(@Param("hoje") LocalDate hoje, Pageable pageable);

    @Query("SELECT v FROM Vacina v WHERE v.dataProximaDose BETWEEN :hoje AND :limite")
    List<Vacina> findVacinasProximas(@Param("hoje") LocalDate hoje, @Param("limite") LocalDate limite);

    // Versão paginada
    @Query("SELECT v FROM Vacina v WHERE v.dataProximaDose BETWEEN :hoje AND :limite")
    Page<Vacina> findVacinasProximas(@Param("hoje") LocalDate hoje, @Param("limite") LocalDate limite, Pageable pageable);
}