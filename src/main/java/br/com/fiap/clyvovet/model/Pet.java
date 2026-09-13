package br.com.fiap.clyvovet.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.clyvovet.enums.Especie;
import br.com.fiap.clyvovet.enums.Sexo;
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
@Table(name = "TB_PET")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pet")
    @SequenceGenerator(name = "seq_pet", sequenceName = "SEQ_PET", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    private String raca;

    private LocalDate dataNascimento;

    private Double peso;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private Boolean castrado;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @OneToMany(mappedBy = "pet")
    private List<Consulta> consultas = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<Vacina> vacinas = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<Medicamento> medicamentos = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<Exame> exames = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<AlertaSaude> alertas = new ArrayList<>();
}