package com.example.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private final WebClient webClient;

    public FallbackController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://message-broker-be:8084").build();
    }

    @PostMapping("/ordenes")
    public Mono<String> fallbackOrdenes(@RequestBody String body) {
        return handleFallback(body, "ORDENES", "órdenes");
    }

    @PostMapping("/pagos")
    public Mono<String> fallbackPagos(@RequestBody String body) {
        return handleFallback(body, "PAGOS", "pagos");
    }

    @PostMapping("/productos")
    public Mono<String> fallbackProductos(@RequestBody String body) {
        return handleFallback(body, "PRODUCTOS", "productos");
    }

    private Mono<String> handleFallback(String body, String domain, String label) {
        return webClient.post()
                .uri("/retry/capture?domain=" + domain)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> "Servicio de " + label + " no disponible. Su solicitud ha sido guardada para reintento automático.");
    }
}
