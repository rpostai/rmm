package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.builders.DeviceBuilder;
import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.builders.InventoryBuilder;
import br.com.rodrigopostai.rmm.builders.ServiceBuilder;
import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.model.Service;
import br.com.rodrigopostai.rmm.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql("/clear.sql")
@Sql("/inventory.sql")
class InventoryRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private InventoryRepository repository;

    private Device mac;
    private Service backup;

    @BeforeEach()
    void setup() {
        mac = DeviceBuilder.newBuilder().withType(DeviceTypeBuilder.getMacDeviceType()).withSystemName("Mac").build();
        mac.setId(1000L);

        backup = ServiceBuilder.newBuilder().withName("Backup").withDeviceType(DeviceTypeBuilder.getMacDeviceType())
                .withPrice(10.0).build();
        backup.setId(1000L);
    }

    @Test
    void shouldAddInventory() {
        Inventory inventory = InventoryBuilder.newBuilder()
                .withDevice(mac)
                .withService(backup).build();
        repository.save(inventory);
        assertNotNull(inventory);
        assertNotNull(inventory.getId());
        assertNotNull(inventory.getCreatedAt());
    }

    @Test
    void shouldFailWhenDataIsInvalid() {
        final Inventory inventory = new Inventory();
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(inventory);
        });
        assertEquals(1, exception.getConstraintViolations().size());
        ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().iterator().next();
        assertEquals("At least one service must be added to the inventory", constraintViolation.getMessage());

        em.clear();

        final Inventory inventory1 = new Inventory();
        inventory1.addInventoryItem(mac);
        exception = assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(inventory1);
        });
        assertEquals(1, exception.getConstraintViolations().size());
        constraintViolation = exception.getConstraintViolations().iterator().next();
        assertEquals("At least one service must be added to this item", constraintViolation.getMessage());

    }

    @Test
    void shouldDeleteInventoryAndItsChildren() {
        Optional<Inventory> inventoryFound = repository.findById(1010L);
        assertTrue(inventoryFound.isPresent());
        Inventory inventory = inventoryFound.get();
        assertEquals(5, inventory.getItems().size());

        em.clear();

        repository.deleteById(1010L);

        inventoryFound = repository.findById(1010L);
        assertFalse(inventoryFound.isPresent());

    }
}
