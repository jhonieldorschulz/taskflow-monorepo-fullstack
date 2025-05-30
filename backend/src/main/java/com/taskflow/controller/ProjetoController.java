package com.taskflow.controller;

import com.taskflow.domain.Projeto;
import com.taskflow.repository.ProjetoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoRepository repository;

    @GetMapping
    public List<Projeto> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Projeto criar(@RequestBody Projeto projeto) {
        return repository.save(projeto);
    }

    @GetMapping("/{id}")
    public Projeto buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Projeto atualizar(@PathVariable Long id, @RequestBody Projeto dados) {
        Projeto projeto = repository.findById(id).orElse(null);
        if (projeto != null) {
            projeto.setNome(dados.getNome());
            projeto.setDescricao(dados.getDescricao());
            return repository.save(projeto);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
