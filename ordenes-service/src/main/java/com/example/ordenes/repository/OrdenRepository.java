package com.example.ordenes.repository;

import com.example.ordenes.model.Orden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends MongoRepository<Orden, String> {
    boolean existsByProductoId(String productoId);
}
