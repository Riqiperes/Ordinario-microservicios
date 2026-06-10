package com.example.broker.chain.business;

import com.example.broker.model.Envio;
import com.example.broker.model.Orden;
import com.example.broker.model.Pago;
import com.example.broker.repository.jpa.EnvioRepository;
import com.example.broker.repository.mongo.OrdenRepository;
import com.example.broker.repository.mongo.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
public class ShipmentHandler implements BusinessHandler {
    private BusinessHandler next;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public void setNext(BusinessHandler next) { this.next = next; }

    @Override
    public void handle(String topic, String message) {
        if ("payment_received_events".equals(topic) || "order_status_changed_events".equals(topic)) {
            String ordenId = message.split(":")[0];
            checkAndCreateShipment(ordenId);
        }
        if (next != null) next.handle(topic, message);
    }

    private void checkAndCreateShipment(String ordenId) {
        ordenRepository.findById(ordenId).ifPresent(orden -> {
            Double totalPagado = pagoRepository.findByOrdenId(ordenId).stream()
                .filter(p -> "COMPLETADO".equals(p.getEstado()))
                .mapToDouble(Pago::getMonto)
                .sum();

            if (totalPagado >= orden.getMontoTotal() && !"PAGADA".equals(orden.getEstado())) {
                log.info("CoR: Pago completo detectado para orden {}. Creando envío.", ordenId);
                orden.setEstado("PAGADA");
                ordenRepository.save(orden);

                Envio envio = new Envio();
                envio.setOrdenId(ordenId);
                envio.setDireccion("Dirección confirmada");
                envio.setEstadoEnvio("EN_CAMINO");
                envio.setFechaEnvio(LocalDateTime.now());
                envioRepository.save(envio);
            }
        });
    }
}
