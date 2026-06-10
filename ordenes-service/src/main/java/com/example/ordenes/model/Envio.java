package com.example.ordenes.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "envios")
public class Envio {
    @Id
    private String id;
    private String ordenId;
    private String direccion;
    private String estadoEnvio; // EN_CAMINO, ENTREGADO
    private LocalDateTime fechaEnvio;
}
