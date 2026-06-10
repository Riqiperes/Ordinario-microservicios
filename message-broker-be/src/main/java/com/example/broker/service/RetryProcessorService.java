package com.example.broker.service;

import com.example.broker.chain.*;
import com.example.broker.model.*;
import com.example.broker.repository.jpa.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RetryProcessorService {

    @Autowired
    private ProductRetryJobRepository productRepo;
    @Autowired
    private OrderRetryJobRepository orderRepo;
    @Autowired
    private PaymentRetryJobRepository paymentRepo;
    
    @Autowired
    private EmailService emailService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Scheduled(fixedRate = 10000)
    public void processAllRetries() {
        processProducts();
        processOrders();
        processPayments();
    }

    private void processProducts() {
        List<ProductRetryJob> jobs = productRepo.findByStatus("PENDING");
        for (ProductRetryJob job : jobs) {
            try {
                log.info("Reintentando PRODUCTO Job ID: {}", job.getId());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(job.getRequestData()), headers);
                
                restTemplate.postForEntity("http://productos-service:8081/productos", entity, String.class);
                
                job.setStatus("SUCCESS");
                productRepo.save(job);
                emailService.sendEmail("admin@tienda.com", "Reintento Exitoso", "Producto procesado tras fallo.");
            } catch (Exception e) {
                log.error("Fallo reintento producto: {}", e.getMessage());
            }
        }
    }

    private void processOrders() {
        List<OrderRetryJob> jobs = orderRepo.findByStatus("PENDING");
        for (OrderRetryJob job : jobs) {
            try {
                log.info("Reintentando ORDEN Job ID: {}", job.getId());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(job.getRequestData()), headers);
                
                restTemplate.postForEntity("http://ordenes-service:8082/ordenes", entity, String.class);
                
                job.setStatus("SUCCESS");
                orderRepo.save(job);
                emailService.sendEmail("admin@tienda.com", "Orden Recuperada", "La orden fallida ha sido procesada exitosamente.");
            } catch (Exception e) {
                log.error("Fallo reintento orden: {}", e.getMessage());
            }
        }
    }

    private void processPayments() {
        List<PaymentRetryJob> jobs = paymentRepo.findByStatus("PENDING");
        for (PaymentRetryJob job : jobs) {
            try {
                log.info("Reintentando PAGO Job ID: {}", job.getId());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(job.getRequestData()), headers);
                
                restTemplate.postForEntity("http://pagos-service:8083/pagos", entity, String.class);
                
                job.setStatus("SUCCESS");
                paymentRepo.save(job);
                emailService.sendEmail("admin@tienda.com", "Pago Recuperado", "El pago fallido ha sido procesado exitosamente.");
            } catch (Exception e) {
                log.error("Fallo reintento pago: {}", e.getMessage());
            }
        }
    }
}
