package com.example.broker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "retry_jobs")
public class RetryJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String domain; // PAGOS, ORDENES, PRODUCTOS
    
    @Column(columnDefinition = "TEXT")
    private String payloadJson; // El objeto "data"
    
    private String status; // PENDING, SUCCESS, ERROR
    
    // Status de los pasos del Chain
    private String sendEmailStatus; 
    private String updateRetryJobsStatus;
    
    private LocalDateTime createdAt;
    private LocalDateTime lastAttempt;
}
