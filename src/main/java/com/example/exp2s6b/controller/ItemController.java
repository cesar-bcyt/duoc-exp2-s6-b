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

import com.example.exp2s6b.model.Item;
import com.example.exp2s6b.service.ItemService;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private static final Logger logger = LogManager.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Item> crearItem(@RequestBody Item item) {
        logger.info("Creando nuevo item: {}", item.getName());
        Item nuevoItem = itemService.crearItem(item);
        logger.info("Item creado con ID: {}", nuevoItem.getId());
        return new ResponseEntity<>(nuevoItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> obtenerItemPorId(@PathVariable Long id) {
        logger.info("Buscando item con ID: {}", id);
        return itemService.obtenerItemPorId(id)
                .map(item -> {
                    logger.info("Item encontrado: {}", item.getName());
                    return new ResponseEntity<>(item, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("Item con ID {} no encontrado", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping
    public ResponseEntity<List<Item>> obtenerTodosLosItems() {
        logger.info("Obteniendo todos los items");
        List<Item> items = itemService.obtenerTodosLosItems();
        logger.info("Se encontraron {} items", items.size());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> actualizarItem(@PathVariable Long id, @RequestBody Item item) {
        logger.info("Actualizando item con ID: {}", id);
        return itemService.obtenerItemPorId(id)
                .map(itemExistente -> {
                    item.setId(id);
                    Item itemActualizado = itemService.actualizarItem(item);
                    logger.info("Item actualizado: {}", itemActualizado.getName());
                    return new ResponseEntity<>(itemActualizado, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("Item con ID {} no encontrado para actualizar", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        logger.info("Eliminando item con ID: {}", id);
        return itemService.obtenerItemPorId(id)
                .map(item -> {
                    itemService.eliminarItem(id);
                    logger.info("Item eliminado con ID: {}", id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> {
                    logger.warn("Item con ID {} no encontrado para eliminar", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }
}