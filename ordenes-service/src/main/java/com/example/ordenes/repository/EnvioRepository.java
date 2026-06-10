package com.example.ordenes.repository;

import com.example.ordenes.model.Envio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends MongoRepository<Envio, String> {
}
