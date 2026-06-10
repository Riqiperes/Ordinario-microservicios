package com.example.broker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "retry_history")
public class RetryHistory {
    @Id
    private String id;
    private Long retryJobId; // Referencia al ID de Postgres
    private String serviceType; // PRODUCT, PAYMENT, ORDER
    private String log;
    private LocalDateTime timestamp;
}
