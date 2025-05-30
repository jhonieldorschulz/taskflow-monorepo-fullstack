package com.taskflow.external;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "usuarioClient", url = "https://jsonplaceholder.typicode.com")
public interface UsuarioClient {
    @GetMapping("/users")
    List<UsuarioDTO> getUsuariosExternos();
}
