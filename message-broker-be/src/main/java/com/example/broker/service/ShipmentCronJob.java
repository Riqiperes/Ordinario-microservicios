package com.example.broker.service;

import com.example.broker.model.Envio;
import com.example.broker.repository.jpa.EnvioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class ShipmentCronJob {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 10000)
    public void processPendingShipments() {
        List<Envio> pending = envioRepository.findAll().stream()
                .filter(e -> "EN_CAMINO".equals(e.getEstadoEnvio()))
                .toList();

        for (Envio envio : pending) {
            log.info("CronJob: Procesando envío para orden {}", envio.getOrdenId());
            envio.setEstadoEnvio("ENTREGADO");
            envioRepository.save(envio);
            
            emailService.sendEmail("cliente@example.com", "Pedido Entregado", 
                    "Tu pedido de la orden " + envio.getOrdenId() + " ha sido entregado exitosamente.");
        }
    }
}
