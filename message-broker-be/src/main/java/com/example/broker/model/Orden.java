package com.example.broker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ordenes")
public class Orden {
    @Id
    private String id;
    private String clienteId;
    private String productoId;
    private Integer cantidad;
    private Double montoTotal;
    private String estado; // PENDIENTE, PAGADA, ENVIADA, CANCELADA
}
