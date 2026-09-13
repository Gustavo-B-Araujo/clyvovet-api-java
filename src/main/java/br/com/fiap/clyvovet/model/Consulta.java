package br.com.fiap.clyvovet.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.clyvovet.enums.StatusConsulta;
import br.com.fiap.clyvovet.enums.TipoConsulta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_CONSULTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_consulta")
    @SequenceGenerator(name = "seq_consulta", sequenceName = "SEQ_CONSULTA", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConsulta tipoConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta statusConsulta;

    @Column(length = 500)
    private String observacoes;

    @Column(length = 500)
    private String diagnostico;

    @Column(length = 500)
    private String tratamento;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @OneToMany(mappedBy = "consulta")
    private List<Medicamento> medicamentos = new ArrayList<>();

    @OneToMany(mappedBy = "consulta")
    private List<Exame> exames = new ArrayList<>();
}