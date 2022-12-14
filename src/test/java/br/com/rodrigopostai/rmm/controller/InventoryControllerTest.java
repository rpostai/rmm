package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.api.InventoryItemRequest;
import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.model.Inventory;
import br.com.rodrigopostai.rmm.model.Service;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("/clear.sql")
@Sql("/inventory.sql")
class InventoryControllerTest extends AbstractControllerTest {

    @Override
    protected String getEndpoint() {
        return "inventory";
    }

    @Test
    void shouldIncludeInventory() {
        Inventory inventory = new Inventory();
        Device device = new Device();
        device.setId(1001L);
        Service antivirusForWindows = new Service();
        antivirusForWindows.setId(1001L);
        inventory.addInventoryItem(device,antivirusForWindows);
        ResponseEntity<String> response = client.postForEntity(getHost()+"/save", inventory, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldAddServiceToInventoryItem() {
        InventoryItemRequest request = new InventoryItemRequest();
        request.setDevice(1001L);
        request.setServices(Arrays.asList(1001L));

        client.postForEntity(getHost()+"/1000", Arrays.asList(request), Void.class);

        ResponseEntity<Inventory> inventorySaved = client.getForEntity(getHost() + "/1000", Inventory.class);
        assertEquals(200, inventorySaved.getStatusCodeValue());
        Inventory inventory = inventorySaved.getBody();
        assertEquals(2,inventory.getItems().size());
    }

    @Test
    void shouldAddServiceToAnInventoryItem() {
        ResponseEntity<Inventory> response = client.getForEntity(getHost() + "/1000", Inventory.class);
        Inventory currentInventory = response.getBody();
        assertEquals(3, currentInventory.getItems().get(0).getServices().size());

        client.postForEntity(getHost()+"/item/1000/service/1000", HttpEntity.EMPTY, Void.class);
        ResponseEntity<Inventory> inventorySaved = client.getForEntity(getHost() + "/1000", Inventory.class);
        assertEquals(200, inventorySaved.getStatusCodeValue());
        Inventory inventory = inventorySaved.getBody();
        assertEquals(1,inventory.getItems().size());
        assertEquals(4, inventory.getItems().get(0).getServices().size());
        assertTrue(inventory.getItems().get(0).getServices().stream().anyMatch(s -> s.getId() == 1000L && s.getName().equals("Device of any type")));
    }

    @Test
    void shouldCalculateInventoryCost() {
        ResponseEntity<Double> response = client.getForEntity(getHost() + "/1010/cost", Double.class);
        assertEquals(200, response.getStatusCodeValue());
        Double value = response.getBody();
        assertEquals(64.00, value);
    }

    @Test
    void shouldCalculateCostByInventoryItem() {
        ResponseEntity<Double> response = client.getForEntity(getHost() + "/item/1010/cost", Double.class);
        assertEquals(200, response.getStatusCodeValue());
        Double value = response.getBody();
        assertEquals(10.00, value);
    }

    @Test
    void shouldDeleteInventoryItem() {
        ResponseEntity<Inventory> response = client.getForEntity(getHost() + "/1010", Inventory.class);
        assertEquals(200, response.getStatusCodeValue());
        Inventory inventory = response.getBody();
        assertEquals(5,inventory.getItems().size());
        client.delete(getHost()+"/item/1014");
        response = client.getForEntity(getHost() + "/1010", Inventory.class);
        assertEquals(200, response.getStatusCodeValue());
        inventory = response.getBody();
        assertEquals(4,inventory.getItems().size());
    }

}
