package br.com.rodrigopostai.rmm.service;

import br.com.rodrigopostai.rmm.model.Service;
import com.google.common.util.concurrent.UncheckedExecutionException;
import br.com.rodrigopostai.rmm.database.InventoryItemRepository;
import br.com.rodrigopostai.rmm.model.InventoryItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql("/clear.sql")
@Sql("/inventory.sql")
class InventoryServiceCacheTest {


    @Autowired
    private InventoryServiceCache cache;

    @Autowired
    private InventoryItemRepository repository;

    @Autowired
    private ApplicationEventPublisher publisher;


    @Test
    @Transactional
    void shouldGetCostFromCacheAndUpdateIt() {
        Double cost = cache.getCost(1010L);
        assertEquals(10.00, cost);

        // mock an update on the item
        InventoryItem item = repository.findById(1010L).orElseThrow(() -> new IllegalArgumentException("Inventory item was not found"));
        Service service = item.getServices().stream().filter(s -> s.getId().equals(1004L)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Error getting service"));
        item.removeService(service);
        publisher.publishEvent(new InventoryItemUpdated(item, InventoryItemUpdatedAction.UPDATE));

        cost = cache.getCost(1010L);
        assertEquals(9.00, cost);

        repository.deleteById(1010L);
        publisher.publishEvent(new InventoryItemUpdated(item, InventoryItemUpdatedAction.REMOVE));
        UncheckedExecutionException exception = assertThrows(UncheckedExecutionException.class, () -> {
            cache.getCost(1010L);
        });
        assertEquals("Inventory Item was not found", exception.getCause().getMessage());
    }

}
