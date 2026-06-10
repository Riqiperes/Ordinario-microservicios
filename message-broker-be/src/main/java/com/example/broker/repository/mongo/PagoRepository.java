package com.example.broker.repository.mongo;

import com.example.broker.model.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {
    List<Pago> findByOrdenId(String ordenId);
}
