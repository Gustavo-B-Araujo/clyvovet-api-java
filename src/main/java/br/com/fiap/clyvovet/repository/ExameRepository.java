package br.com.fiap.clyvovet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.clyvovet.model.Exame;

public interface ExameRepository extends JpaRepository<Exame, Long> {

    // Listas sem paginação
    List<Exame> findByPetId(Long petId);
    void deleteByPetId(Long petId);
    List<Exame> findByTipoContainingIgnoreCase(String tipo);
    List<Exame> findByConsultaId(Long consultaId);
    List<Exame> findByDataRealizacaoBetween(LocalDate inicio, LocalDate fim);

    // Versões paginadas
    Page<Exame> findByPetId(Long petId, Pageable pageable);
    Page<Exame> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);
    Page<Exame> findByConsultaId(Long consultaId, Pageable pageable);
    Page<Exame> findByDataRealizacaoBetween(LocalDate inicio, LocalDate fim, Pageable pageable);
}