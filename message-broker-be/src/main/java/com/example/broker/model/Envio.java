package com.example.broker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ordenId;
    private String direccion;
    private String estadoEnvio; // EN_CAMINO, ENTREGADO
    private LocalDateTime fechaEnvio;
}
