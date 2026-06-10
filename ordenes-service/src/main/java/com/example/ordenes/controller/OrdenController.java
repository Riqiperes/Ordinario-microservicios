package com.example.ordenes.controller;

import com.example.ordenes.model.Orden;
import com.example.ordenes.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private com.example.ordenes.service.OrderService orderService;

    @Autowired
    private com.example.ordenes.client.ProductoClient productoClient;

    @Autowired
    private com.example.ordenes.client.ClienteClient clienteClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public List<Orden> listarOrdenes() {
        return ordenRepository.findAll();
    }

    @PostMapping
    public Orden crearOrden(@RequestBody Orden orden) {
        // Validar que el cliente exista
        com.example.ordenes.dto.ClienteDTO cliente = clienteClient.obtenerCliente(orden.getClienteId());
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }

        // Obtener el producto para validar el precio
        com.example.ordenes.dto.ProductoDTO producto = productoClient.obtenerProducto(orden.getProductoId());
        
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }

        // VALIDACIÓN DE STOCK: El backend verifica stock disponible
        if (orden.getCantidad() > producto.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Calcular el monto total automáticamente
        double montoTotal = producto.getPrecio() * orden.getCantidad();
        orden.setMontoTotal(montoTotal);
        
        orden.setEstado("PENDIENTE");
        Orden nuevaOrden = ordenRepository.save(orden);
        
        String mensaje = nuevaOrden.getProductoId() + ":-" + nuevaOrden.getCantidad();
        kafkaTemplate.send("inventory_update_events", mensaje);
        
        return nuevaOrden;
    }

    @GetMapping("/producto/{productoId}/en-uso")
    public boolean verificarUsoProducto(@PathVariable String productoId) {
        return ordenRepository.existsByProductoId(productoId);
    }

    @PutMapping("/{id}")
    public Orden actualizarOrden(@PathVariable String id, @RequestBody Orden ordenDetalles) {
        return orderService.actualizarOrden(id, ordenDetalles);
    }
}
