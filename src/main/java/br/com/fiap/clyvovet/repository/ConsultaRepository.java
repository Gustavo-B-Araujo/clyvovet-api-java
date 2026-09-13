package br.com.fiap.clyvovet.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.clyvovet.model.Consulta;
import br.com.fiap.clyvovet.enums.StatusConsulta;
import br.com.fiap.clyvovet.enums.TipoConsulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    List<Consulta> findByPetId(Long petId);
    void deleteByPetId(Long petId);
    List<Consulta> findByVeterinarioId(Long veterinarioId);
    List<Consulta> findByStatusConsulta(StatusConsulta status);
    List<Consulta> findByTipoConsulta(TipoConsulta tipo);
    Page<Consulta> findByPetId(Long petId, Pageable pageable);
    Page<Consulta> findByVeterinarioId(Long veterinarioId, Pageable pageable);
    Page<Consulta> findByTipoConsulta(TipoConsulta tipo, Pageable pageable);
    Page<Consulta> findByStatusConsulta(StatusConsulta status, Pageable pageable);
    List<Consulta> findByVeterinarioIdOrderByDataHoraAsc(Long veterinarioId);
    boolean existsByVeterinarioIdAndDataHora(Long veterinarioId, LocalDateTime dataHora);
    boolean existsByVeterinarioIdAndDataHoraAndIdNot(Long veterinarioId, LocalDateTime dataHora, Long id);

    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :inicio AND :fim")
    List<Consulta> findConsultasNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :inicio AND :fim")
    Page<Consulta> findConsultasNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, Pageable pageable);

    @Query("SELECT c FROM Consulta c WHERE c.statusConsulta = :status AND c.dataHora < :agora")
    List<Consulta> findConsultasAtrasadas(@Param("status") StatusConsulta status, @Param("agora") LocalDateTime agora);
}