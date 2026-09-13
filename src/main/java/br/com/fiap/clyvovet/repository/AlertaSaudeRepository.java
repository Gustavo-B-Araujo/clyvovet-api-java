package br.com.fiap.clyvovet.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.clyvovet.model.AlertaSaude;
import br.com.fiap.clyvovet.enums.TipoAlerta;

public interface AlertaSaudeRepository extends JpaRepository<AlertaSaude, Long> {


    Page<AlertaSaude> findByPetId(Long petId, Pageable pageable);
    Page<AlertaSaude> findByTipoAlerta(TipoAlerta tipo, Pageable pageable);
    Page<AlertaSaude> findByLidoFalse(Pageable pageable);
    Page<AlertaSaude> findByLidoTrue(Pageable pageable);
    List<AlertaSaude> findByPetId(Long petId);
    void deleteByPetId(Long petId);
    List<AlertaSaude> findByTipoAlerta(TipoAlerta tipo);
    List<AlertaSaude> findByLidoFalse();
    List<AlertaSaude> findByPet_Tutor_IdOrderByDataGeracaoDesc(Long tutorId);
    boolean existsByIdAndPet_Tutor_Id(Long id, Long tutorId);

    @Modifying
    @Transactional
    @Query("UPDATE AlertaSaude a SET a.lido = true, a.dataLeitura = CURRENT_TIMESTAMP WHERE a.id = :id")
    void marcarComoLido(@Param("id") Long id);
}