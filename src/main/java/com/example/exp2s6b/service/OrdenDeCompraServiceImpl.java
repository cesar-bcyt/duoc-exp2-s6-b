package com.example.exp2s6b.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.exp2s6b.model.Item;
import com.example.exp2s6b.model.OrdenDeCompra;
import com.example.exp2s6b.model.OrdenItem;
import com.example.exp2s6b.repository.ItemRepository;
import com.example.exp2s6b.repository.OrdenDeCompraRepository;
import com.example.exp2s6b.repository.OrdenItemRepository;

@Service
public class OrdenDeCompraServiceImpl implements OrdenDeCompraService {

    private final OrdenDeCompraRepository ordenDeCompraRepository;
    private final ItemRepository itemRepository;
    private final OrdenItemRepository ordenItemRepository;

    public OrdenDeCompraServiceImpl(OrdenDeCompraRepository ordenDeCompraRepository, ItemRepository itemRepository, OrdenItemRepository ordenItemRepository) {
        this.ordenDeCompraRepository = ordenDeCompraRepository;
        this.itemRepository = itemRepository;
        this.ordenItemRepository = ordenItemRepository;
    }

    @Override
    public OrdenDeCompra crearOrden(OrdenDeCompra orden) {
        OrdenDeCompra order = new OrdenDeCompra();

        List<OrdenItem> ordenItems = orden.getOrdenItems();
        for (OrdenItem ordenItem : ordenItems) {
            Item item = itemRepository.findById(ordenItem.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item no encontrado: " + ordenItem.getItem().getId()));
            OrdenItem newOrdenItem = new OrdenItem();
            newOrdenItem.setItem(item);
            newOrdenItem.setQuantity(ordenItem.getQuantity());
            order.addOrdenItem(newOrdenItem);
        }
        order.setEstado(orden.getEstado());

        return ordenDeCompraRepository.save(order);
    }

    @Override
    public Optional<OrdenDeCompra> obtenerOrdenPorId(Long id) {
        return ordenDeCompraRepository.findById(id);
    }

    @Override
    public List<OrdenDeCompra> obtenerTodasLasOrdenes() {
        return ordenDeCompraRepository.findAll();
    }

    @Override
    public OrdenDeCompra actualizarOrden(OrdenDeCompra orden) {
        OrdenDeCompra ordenAnterior = ordenDeCompraRepository.findById(orden.getId()).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        OrdenDeCompra.EstadoOrden estadoAnterior = ordenAnterior.getEstado();
        ordenItemRepository.deleteOrdenItemByOrdenId(orden.getId());
        if (isEstadoChangeLegal(estadoAnterior, orden.getEstado())) {
            orden.setEstado(orden.getEstado());
        } else {
            throw new RuntimeException("No se permite cambiar el estado de la orden");
        }
        return ordenDeCompraRepository.save(orden);
    }

    @Override
    public void eliminarOrden(Long id) {
        ordenDeCompraRepository.deleteById(id);
    }

    private boolean isEstadoChangeLegal(OrdenDeCompra.EstadoOrden estadoActual, OrdenDeCompra.EstadoOrden nuevoEstado) {
        if (estadoActual == nuevoEstado) return true;
        return switch (estadoActual) {
            case Creada -> nuevoEstado == OrdenDeCompra.EstadoOrden.Pagada || nuevoEstado == OrdenDeCompra.EstadoOrden.Anulada;
            case Pagada -> nuevoEstado == OrdenDeCompra.EstadoOrden.Despachada || nuevoEstado == OrdenDeCompra.EstadoOrden.Anulada;
            case Despachada -> nuevoEstado == OrdenDeCompra.EstadoOrden.Completada;
            case Anulada, Completada -> false;
            default -> false;
        }; // No se permiten cambios desde estos estados
    }
}