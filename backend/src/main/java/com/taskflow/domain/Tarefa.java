package com.taskflow.domain;

import jakarta.persistence.*;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private String status;

    @ManyToOne
    @JoinColumn(name = "quadro_id")
    private Quadro quadro;
    // Getters e Setters
}
