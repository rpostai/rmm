package br.com.rodrigopostai.rmm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "items")
public class Inventory extends BaseEntity{

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotEmpty(message = "At least one service must be added to the inventory")
    private List<InventoryItem> items;

    public InventoryItem addInventoryItem(Device device, Service... services) {
        if (items == null) {
            items = new ArrayList<>();
        }
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setDevice(device);
        if (services != null) {
            for (Service service : services) {
                inventoryItem.addService(service);
            }
        }
        inventoryItem.setInventory(this);
        items.add(inventoryItem);
        return inventoryItem;
    }

    public InventoryItem addInventoryItem(Device device, List<Service> services) {
        if (device != null && CollectionUtils.isNotEmpty(services)) {
            Service[] arr = new Service[services.size()];
            services.toArray(arr);
            return addInventoryItem(device, arr);
        }
        return null;
    }

    public void clearItems() {
        if (CollectionUtils.isNotEmpty(items)) {
            items.clear();
        }
    }

    public List<InventoryItem> getItems() {
        if (CollectionUtils.isNotEmpty(items)) {
            return Collections.unmodifiableList(items);
        }
        return Collections.emptyList();
    }

    public Double calculateTotalServiceCost() {
        if (CollectionUtils.isNotEmpty(items)) {
            return items.stream().map(InventoryItem::calculateTotalServiceCost)
                    .reduce(Double::sum).orElse((double) 0);
        }
        return (double) 0;
    }


}
