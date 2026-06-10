package com.example.ordenes.service;

import com.example.ordenes.model.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PagoRepository extends MongoRepository<Pago, String> {
    List<Pago> findByOrdenId(String ordenId);
}
