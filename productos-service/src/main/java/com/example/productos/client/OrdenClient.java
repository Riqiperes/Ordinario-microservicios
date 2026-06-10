package com.example.productos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ordenes-service")
public interface OrdenClient {

    @GetMapping("/ordenes/producto/{productoId}/en-uso")
    boolean verificarUsoProducto(@PathVariable("productoId") String productoId);
}
