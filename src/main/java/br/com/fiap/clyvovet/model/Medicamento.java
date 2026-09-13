package br.com.fiap.clyvovet.model;

import java.time.LocalDate;

import br.com.fiap.clyvovet.enums.StatusMedicamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_MEDICAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medicamento")
    @SequenceGenerator(name = "seq_medicamento", sequenceName = "SEQ_MEDICAMENTO", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    private String dosagem;

    private String frequencia;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMedicamento statusMedicamento;

    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;
}