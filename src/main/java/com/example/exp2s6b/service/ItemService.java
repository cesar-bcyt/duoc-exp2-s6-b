package com.example.exp2s6b.service;

import java.util.List;
import java.util.Optional;

import com.example.exp2s6b.model.Item;

public interface ItemService {
    Item crearItem(Item item);
    Optional<Item> obtenerItemPorId(Long id);
    List<Item> obtenerTodosLosItems();
    Item actualizarItem(Item item);
    void eliminarItem(Long id);
}