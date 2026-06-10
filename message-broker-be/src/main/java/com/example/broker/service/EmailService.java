package com.example.broker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        log.info("ENVIANDO CORREO REAL A: {} | ASUNTO: {} | CUERPO: {}", to, subject, body);
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                log.info("Correo enviado exitosamente via JavaMailSender.");
            } catch (Exception e) {
                log.error("Error al enviar correo: {}", e.getMessage());
            }
        } else {
            log.warn("JavaMailSender no está disponible. El correo no pudo enviarse realmente.");
        }
    }
}
