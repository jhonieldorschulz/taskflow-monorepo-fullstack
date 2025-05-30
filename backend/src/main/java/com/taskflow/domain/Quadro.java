package com.taskflow.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Quadro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @OneToMany(mappedBy = "quadro", cascade = CascadeType.ALL)
    private List<Tarefa> tarefas;
    // Getters e Setters
}
