package com.example.ordenes.client;

import com.example.ordenes.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes-service")
public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    ClienteDTO obtenerCliente(@PathVariable("id") String id);
}
