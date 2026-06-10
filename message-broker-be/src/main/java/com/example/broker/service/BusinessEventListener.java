package com.example.broker.service;

import com.example.broker.chain.business.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class BusinessEventListener {

    @Autowired
    private InventoryHandler inventoryHandler;

    @Autowired
    private EmailHandler emailHandler;

    @Autowired
    private ShipmentHandler shipmentHandler;

    private BusinessHandler chain;

    @PostConstruct
    public void init() {
        // Configurar la cadena
        inventoryHandler.setNext(emailHandler);
        emailHandler.setNext(shipmentHandler);
        this.chain = inventoryHandler;
    }

    @KafkaListener(topics = "inventory_update_events", groupId = "broker-business-group")
    public void listenInventory(String message) {
        log.info("Recibido inventory_update_events: {}", message);
        chain.handle("inventory_update_events", message);
    }

    @KafkaListener(topics = "payment_received_events", groupId = "broker-business-group")
    public void listenPayment(String message) {
        log.info("Recibido payment_received_events: {}", message);
        chain.handle("payment_received_events", message);
    }

    @KafkaListener(topics = "order_status_changed_events", groupId = "broker-business-group")
    public void listenOrderStatus(String message) {
        log.info("Recibido order_status_changed_events: {}", message);
        chain.handle("order_status_changed_events", message);
    }
}
