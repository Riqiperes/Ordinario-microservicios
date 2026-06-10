package com.example.ordenes.service;

import com.example.ordenes.model.Orden;
import com.example.ordenes.repository.OrdenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public Orden actualizarOrden(String id, Orden request) {
        log.info("[DEBUG] Nivel 3: Iniciando análisis forense para Orden ID: {}", id);

        Orden ordenPersistida = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La orden " + id + " no existe en DB."));

        int cantAnterior = ordenPersistida.getCantidad();
        int cantNueva = request.getCantidad();
        String prodAnterior = ordenPersistida.getProductoId();
        String prodNueva = request.getProductoId();

        boolean cambioProducto = !prodAnterior.equals(prodNueva);
        boolean cambioCantidad = cantAnterior != cantNueva;

        if (cambioProducto || cambioCantidad) {
            log.info("[DEBUG] Cambio detectado. Anterior: {} (Cant: {}), Nuevo: {} (Cant: {})", 
                      prodAnterior, cantAnterior, prodNueva, cantNueva);

            if (cambioProducto) {
                kafkaTemplate.send("inventory_update_events", prodAnterior + ":" + cantAnterior);
                kafkaTemplate.send("inventory_update_events", prodNueva + ":-" + cantNueva);
                log.info("[KAFKA] Eventos de intercambio de producto enviados.");
            } else {
                int delta = cantNueva - cantAnterior;
                kafkaTemplate.send("inventory_update_events", prodNueva + ":" + (-delta));
                log.info("[KAFKA] Delta de inventario enviado: {}", (-delta));
            }
        }

        double precioUnitario = ordenPersistida.getMontoTotal() / cantAnterior;
        double nuevoTotal = precioUnitario * cantNueva;
        
        log.info("[DEBUG] Monto recalculado: {} -> {}. Actualizando entidad.", 
                  ordenPersistida.getMontoTotal(), nuevoTotal);

        if ("PAGADA".equals(ordenPersistida.getEstado()) && nuevoTotal > ordenPersistida.getMontoTotal()) {
            log.warn("[DEBUG] Monto incrementado en orden PAGADA. Revirtiendo a PENDIENTE.");
            ordenPersistida.setEstado("PENDIENTE");
        } else {
            ordenPersistida.setEstado(request.getEstado());
        }

        ordenPersistida.setProductoId(prodNueva);
        ordenPersistida.setCantidad(cantNueva);
        ordenPersistida.setMontoTotal(nuevoTotal);

        Orden guardada = ordenRepository.save(ordenPersistida);
        kafkaTemplate.send("order_status_changed_events", guardada.getId().toString());
        
        log.info("[DEBUG] Persistencia exitosa en PostgreSQL para Orden ID: {}", guardada.getId());
        return guardada;
    }
}
