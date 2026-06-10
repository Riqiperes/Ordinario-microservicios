package com.example.broker.chain;

import com.example.broker.model.RetryJob;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailRetryHandler implements RetryHandler {
    private RetryHandler next;

    @Override
    public void setNext(RetryHandler next) { this.next = next; }

    @Override
    public void handle(RetryJob job) {
        try {
            log.info("PASO B: Enviando correo de éxito para reintento de {}", job.getDomain());
            job.setSendEmailStatus("SUCCESS");
            if (next != null) next.handle(job);
        } catch (Exception e) {
            job.setSendEmailStatus("ERROR");
            job.setStatus("ERROR");
            log.error("Fallo en PASO B para job {}", job.getId());
        }
    }
}
