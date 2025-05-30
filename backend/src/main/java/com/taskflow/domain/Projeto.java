package com.taskflow.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private List<Quadro> quadros;
    // Getters e Setters
}
