package com.example.exp2s6b.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exp2s6b.model.OrdenDeCompra;
import com.example.exp2s6b.service.OrdenDeCompraService;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenDeCompraController {

    private static final Logger logger = LogManager.getLogger(OrdenDeCompraController.class);

    private final OrdenDeCompraService ordenDeCompraService;

    public OrdenDeCompraController(OrdenDeCompraService ordenDeCompraService) {
        this.ordenDeCompraService = ordenDeCompraService;
    }

    @PostMapping
    public ResponseEntity<OrdenDeCompra> crearOrden(@RequestBody OrdenDeCompra orden) {
        logger.info("Creando nueva orden de compra");
        OrdenDeCompra nuevaOrden = ordenDeCompraService.crearOrden(orden);
        logger.info("Orden de compra creada con ID: {}", nuevaOrden.getId());
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }

    @GetMapping("/estado/{id}")
    public ResponseEntity<OrdenDeCompra.EstadoOrden> obtenerEstadoOrdenPorId(@PathVariable Long id) {
        // Devolver sólo el estado de la orden
        logger.info("Buscando estado de la orden de compra con ID: {}", id);
        return ordenDeCompraService.obtenerOrdenPorId(id)
                .map(orden -> {
                    logger.info("Orden de compra encontrada con ID: {}", id);
                    return new ResponseEntity<>(orden.getEstado(), HttpStatus.OK);

                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDeCompra> obtenerOrdenPorId(@PathVariable Long id) {
        logger.info("Buscando orden de compra con ID: {}", id);
        return ordenDeCompraService.obtenerOrdenPorId(id)
                .map(orden -> {
                    logger.info("Orden de compra encontrada con ID: {}", id);
                    return new ResponseEntity<>(orden, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<OrdenDeCompra>> obtenerTodasLasOrdenes() {
        logger.info("Obteniendo todas las órdenes de compra");
        List<OrdenDeCompra> ordenes = ordenDeCompraService.obtenerTodasLasOrdenes();
        logger.info("Se encontraron {} órdenes de compra", ordenes.size());
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenDeCompra> actualizarOrden(@PathVariable Long id, @RequestBody OrdenDeCompra orden) {
        logger.info("Actualizando orden de compra con ID: {}", id);
        return ordenDeCompraService.obtenerOrdenPorId(id)
                .map(ordenExistente -> {
                    orden.setId(id);
                    OrdenDeCompra ordenActualizada = ordenDeCompraService.actualizarOrden(orden);
                    logger.info("Orden de compra actualizada con ID: {}", id);
                    return new ResponseEntity<>(ordenActualizada, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        logger.info("Eliminando orden de compra con ID: {}", id);
        return ordenDeCompraService.obtenerOrdenPorId(id)
                .map(orden -> {
                    ordenDeCompraService.eliminarOrden(id);
                    logger.info("Orden de compra eliminada con ID: {}", id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}