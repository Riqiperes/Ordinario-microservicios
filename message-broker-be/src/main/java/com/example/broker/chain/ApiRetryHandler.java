package com.example.broker.chain;

import com.example.broker.model.RetryJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ApiRetryHandler implements RetryHandler {
    private RetryHandler next;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setNext(RetryHandler next) { this.next = next; }

    @Override
    public void handle(RetryJob job) {
        try {
            log.info("PASO A: Reintentando endpoint original para dominio: {}", job.getDomain());
            
            String url = "";
            if ("ORDENES".equals(job.getDomain())) {
                url = "http://ordenes-service:8082/ordenes";
            } else if ("PAGOS".equals(job.getDomain())) {
                url = "http://pagos-service:8083/pagos";
            } else if ("PRODUCTOS".equals(job.getDomain())) {
                url = "http://productos-service:8081/productos";
            }

            if (!url.isEmpty()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(job.getPayloadJson(), headers);
                
                restTemplate.postForEntity(url, entity, String.class);
                log.info("Reintento exitoso para dominio: {}", job.getDomain());
            }
            
            if (next != null) next.handle(job);
        } catch (Exception e) {
            job.setStatus("ERROR");
            log.error("Fallo REAL en PASO A para job {}: {}", job.getId(), e.getMessage());
        }
    }
}
