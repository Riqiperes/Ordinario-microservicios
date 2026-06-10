package com.example.broker.repository.jpa;

import com.example.broker.model.RetryJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RetryJobRepository extends JpaRepository<RetryJob, Long> {
    List<RetryJob> findByStatus(String status);
}
