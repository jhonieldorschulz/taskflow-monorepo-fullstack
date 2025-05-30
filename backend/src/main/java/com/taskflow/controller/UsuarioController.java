package com.taskflow.controller;

import com.taskflow.external.UsuarioClient;
import com.taskflow.external.UsuarioDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
public class UsuarioController {

    @Autowired
    private UsuarioClient usuarioClient;

    @GetMapping("/usuarios")
    public List<UsuarioDTO> getUsuariosExternos() {
        return usuarioClient.getUsuariosExternos();
    }
}
