package com.taskflow.controller;

import com.taskflow.domain.Quadro;
import com.taskflow.repository.QuadroRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quadros")
public class QuadroController {

    @Autowired
    private QuadroRepository repository;

    @GetMapping
    public List<Quadro> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Quadro criar(@RequestBody Quadro quadro) {
        return repository.save(quadro);
    }

    @GetMapping("/{id}")
    public Quadro buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Quadro atualizar(@PathVariable Long id, @RequestBody Quadro dados) {
        Quadro quadro = repository.findById(id).orElse(null);
        if (quadro != null) {
            quadro.setNome(dados.getNome());
            return repository.save(quadro);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
