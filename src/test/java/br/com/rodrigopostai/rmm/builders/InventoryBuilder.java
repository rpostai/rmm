package br.com.rodrigopostai.rmm.builders;

import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.model.Service;
import br.com.rodrigopostai.rmm.model.Inventory;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryBuilder {

    private Device device;
    private List<Service> services = new ArrayList<>();

    private InventoryBuilder() {

    }

    public static InventoryBuilder newBuilder() {
        return new InventoryBuilder();
    }

    public InventoryBuilder withDevice(Device device) {
        this.device = device;
        return this;
    }

    public InventoryBuilder withService(Service service) {
        services.add(service);
        return this;
    }

    public Inventory build() {
        Objects.requireNonNull(device, "Device is mandatory");
        if (CollectionUtils.isEmpty(services)) {
            throw new IllegalArgumentException("At least 1 service is mandatory");
        }
        Inventory inventory = new Inventory();
        inventory.addInventoryItem(device, services);
        return inventory;
    }
}
