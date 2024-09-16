package com.example.exp2s6b.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.exp2s6b.model.Item;
import com.example.exp2s6b.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item crearItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> obtenerItemPorId(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> obtenerTodosLosItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item actualizarItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void eliminarItem(Long id) {
        itemRepository.deleteById(id);
    }
}