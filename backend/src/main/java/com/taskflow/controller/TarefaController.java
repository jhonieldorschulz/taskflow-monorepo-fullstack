package com.taskflow.controller;

import com.taskflow.domain.Tarefa;
import com.taskflow.repository.TarefaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository repository;

    @GetMapping
    public List<Tarefa> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Tarefa criar(@RequestBody Tarefa tarefa) {
        return repository.save(tarefa);
    }

    @GetMapping("/{id}")
    public Tarefa buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @RequestBody Tarefa dados) {
        Tarefa tarefa = repository.findById(id).orElse(null);
        if (tarefa != null) {
            tarefa.setTitulo(dados.getTitulo());
            tarefa.setDescricao(dados.getDescricao());
            tarefa.setStatus(dados.getStatus());
            return repository.save(tarefa);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
