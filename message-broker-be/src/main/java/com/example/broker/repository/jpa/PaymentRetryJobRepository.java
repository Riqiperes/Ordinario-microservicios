package com.example.broker.repository.jpa;

import com.example.broker.model.PaymentRetryJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRetryJobRepository extends JpaRepository<PaymentRetryJob, Long> {
    List<PaymentRetryJob> findByStatus(String status);
}
