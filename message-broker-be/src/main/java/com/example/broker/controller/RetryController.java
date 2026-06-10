package com.example.broker.controller;

import com.example.broker.model.*;
import com.example.broker.repository.jpa.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/retry")
public class RetryController {

    @Autowired
    private OrderRetryJobRepository orderRepository;

    @Autowired
    private PaymentRetryJobRepository paymentRepository;

    @Autowired
    private ProductRetryJobRepository productRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/envios")
    public java.util.List<Envio> listarEnvios() {
        return envioRepository.findAll();
    }

    @PostMapping("/capture")
    public Object captureFailure(@RequestBody String payload, @RequestParam String domain) throws Exception {
        Map<String, Object> data = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        String dom = domain.toUpperCase();
        
        if ("PRODUCTOS".equals(dom)) {
            ProductRetryJob job = new ProductRetryJob();
            job.setRequestData(data);
            job.setStatus("PENDING");
            job.setCreatedAt(LocalDateTime.now());
            return productRepository.save(job);
        } else if ("ORDENES".equals(dom)) {
            OrderRetryJob job = new OrderRetryJob();
            job.setRequestData(data);
            job.setStatus("PENDING");
            job.setCreatedAt(LocalDateTime.now());
            return orderRepository.save(job);
        } else if ("PAGOS".equals(dom)) {
            PaymentRetryJob job = new PaymentRetryJob();
            job.setRequestData(data);
            job.setStatus("PENDING");
            job.setCreatedAt(LocalDateTime.now());
            return paymentRepository.save(job);
        }
        
        throw new IllegalArgumentException("Dominio no soportado: " + dom);
    }
}
