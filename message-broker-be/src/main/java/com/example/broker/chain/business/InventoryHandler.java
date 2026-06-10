package com.example.broker.chain.business;

import com.example.broker.model.Producto;
import com.example.broker.repository.mongo.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryHandler implements BusinessHandler {
    private BusinessHandler next;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void setNext(BusinessHandler next) { this.next = next; }

    @Override
    public void handle(String topic, String message) {
        if ("inventory_update_events".equals(topic)) {
            log.info("CoR: Procesando actualización de inventario: {}", message);
            String[] parts = message.split(":");
            String productoId = parts[0];
            Integer cantidad = Integer.parseInt(parts[1]);

            productoRepository.findById(productoId).ifPresent(p -> {
                p.setStock(p.getStock() + cantidad);
                productoRepository.save(p);
                log.info("CoR: Stock actualizado para {}. Nuevo stock: {}", productoId, p.getStock());
            });
        }
        if (next != null) next.handle(topic, message);
    }
}
