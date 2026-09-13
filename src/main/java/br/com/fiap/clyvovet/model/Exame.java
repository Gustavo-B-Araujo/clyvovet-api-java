package br.com.fiap.clyvovet.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "TB_EXAME")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_exame")
    @SequenceGenerator(name = "seq_exame", sequenceName = "SEQ_EXAME", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(nullable = false)
    private LocalDate dataRealizacao;

    private String resultado;

    private String urlArquivo;

    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;
}