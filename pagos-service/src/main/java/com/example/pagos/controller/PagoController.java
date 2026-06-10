package com.example.pagos.controller;

import com.example.pagos.model.Pago;
import com.example.pagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    @PostMapping
    public Pago crearPago(@RequestBody Pago pago) {
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("COMPLETADO");
        Pago pagoGuardado = pagoRepository.save(pago);
        
        // Notificar que se recibió un pago (Tópico 3)
        // Formato: "ordenId:monto"
        String mensaje = pagoGuardado.getOrdenId() + ":" + pagoGuardado.getMonto();
        kafkaTemplate.send("payment_received_events", mensaje);
        
        return pagoGuardado;
    }
}
