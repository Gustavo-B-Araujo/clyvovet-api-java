package br.com.fiap.clyvovet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.clyvovet.model.Medicamento;
import br.com.fiap.clyvovet.enums.StatusMedicamento;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Listas sem paginação
    List<Medicamento> findByPetId(Long petId);
    void deleteByPetId(Long petId);
    List<Medicamento> findByStatusMedicamento(StatusMedicamento status);
    List<Medicamento> findByConsultaId(Long consultaId);

    // Versões paginadas
    Page<Medicamento> findByPetId(Long petId, Pageable pageable);
    Page<Medicamento> findByStatusMedicamento(StatusMedicamento status, Pageable pageable);
    Page<Medicamento> findByConsultaId(Long consultaId, Pageable pageable);

    @Query("SELECT m FROM Medicamento m WHERE m.statusMedicamento = 'ATIVO' AND m.dataFim < CURRENT_DATE")
    List<Medicamento> findMedicamentosAtivosExpirados();
}