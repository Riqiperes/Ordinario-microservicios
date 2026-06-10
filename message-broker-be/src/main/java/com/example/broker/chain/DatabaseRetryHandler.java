package com.example.broker.chain;

import com.example.broker.model.RetryJob;
import com.example.broker.repository.jpa.RetryJobRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseRetryHandler implements RetryHandler {
    private RetryHandler next;
    private final RetryJobRepository repository;

    public DatabaseRetryHandler(RetryJobRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setNext(RetryHandler next) { this.next = next; }

    @Override
    public void handle(RetryJob job) {
        try {
            log.info("PASO C: Actualizando retry_job a SUCCESS");
            job.setUpdateRetryJobsStatus("SUCCESS");
            job.setStatus("SUCCESS");
            repository.save(job);
            if (next != null) next.handle(job);
        } catch (Exception e) {
            job.setUpdateRetryJobsStatus("ERROR");
            job.setStatus("ERROR");
            repository.save(job);
            log.error("Fallo en PASO C para job {}", job.getId());
        }
    }
}
