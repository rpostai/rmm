package br.com.rodrigopostai.rmm.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import br.com.rodrigopostai.rmm.database.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Component
public final class InventoryServiceCache {

    private Cache<Long, Double> cache;

    private InventoryItemRepository repository;

    public InventoryServiceCache(@Value("#{new Integer('${rtm.inventory.cache.timeout}')}") int expirationTime,
                                 InventoryItemRepository inventoryRepository) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTime, TimeUnit.SECONDS)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
        this.repository = inventoryRepository;
    }

    public Double getCost(Long inventoryItemId) {
        try {
            return cache.get(inventoryItemId, () ->
                         repository
                                .findById(inventoryItemId).orElseThrow(() -> new IllegalArgumentException("Inventory Item was not found"))
                                .calculateTotalServiceCost()

            );
        } catch (ExecutionException e) {
            throw new RuntimeException("Error to get Inventory Item cost",e);
        }
    }

    @EventListener(InventoryItemUpdated.class)
    public void refreshCache(InventoryItemUpdated item) {
        // if action is remove, then only invalidates its entry
        cache.invalidate(item.getItem().getId());
        if (item.getAction().equals(InventoryItemUpdatedAction.UPDATE)) {
            cache.put(item.getItem().getId(), item.getItem().calculateTotalServiceCost());
        }
    }



}
