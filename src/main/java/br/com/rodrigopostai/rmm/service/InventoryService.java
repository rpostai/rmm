package br.com.rodrigopostai.rmm.service;

import br.com.rodrigopostai.rmm.model.Inventory;

import java.util.List;

public interface InventoryService {

    void addInventoryItem(Long inventoryId, List<InventoryItemDTO> items);

    void addServiceToInventoryItem(Long inventoryItemId, Long serviceId);

    void deleteInventoryInventoryItem(Long inventoryItemId);

    Double calculateTotalServiceCost(Long inventoryItemId);

    Inventory save(Inventory entity);

    Double calculateServiceCostByItem(Long inventoryItemId);
}
