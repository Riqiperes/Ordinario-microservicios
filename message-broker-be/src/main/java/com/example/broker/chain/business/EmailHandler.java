package com.example.broker.chain.business;

import com.example.broker.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailHandler implements BusinessHandler {
    private BusinessHandler next;

    @Autowired
    private EmailService emailService;

    @Override
    public void setNext(BusinessHandler next) { this.next = next; }

    @Override
    public void handle(String topic, String message) {
        if ("order_status_changed_events".equals(topic)) {
            emailService.sendEmail("cliente@example.com", "Actualización de Orden", "Tu orden " + message + " ha cambiado de estado.");
        } else if ("payment_received_events".equals(topic)) {
            emailService.sendEmail("cliente@example.com", "Pago Recibido", "Hemos recibido tu pago para la orden " + message.split(":")[0]);
        }
        if (next != null) next.handle(topic, message);
    }
}
