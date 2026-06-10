package com.example.pagos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "pagos")
public class Pago {
    @Id
    private String id;
    private String ordenId;
    private Double monto;
    private String estado; // COMPLETADO, FALLIDO
    private LocalDateTime fechaPago;
}
