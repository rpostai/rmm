package br.com.rodrigopostai.rmm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "inventory_item")
@ToString(exclude = "inventory")
@EqualsAndHashCode(exclude = "services")
public class InventoryItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    @JsonBackReference
    @NotNull
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    @NotNull
    private Device device;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "inventoryItem")
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    @JsonProperty("inventory_services")
    @NotEmpty(message = "At least one service must be added to this item")
    private Set<InventoryItemService> services;

    public InventoryItem addService(Service service) {
        if (services == null) {
            services = new HashSet<>();
        }
        insertServiceIfDeviceTypeIsValid(service);
        return this;
    }

    public InventoryItem addServices(List<Service> services) {
        if (CollectionUtils.isNotEmpty(services)) {
            for (Service service : services) {
                addService(service);
            }
        }
        return this;
    }

    public void clearServices() {
        if (CollectionUtils.isNotEmpty(services)) {
            services.clear();
        }
    }

    @JsonIgnore
    public Set<Service> getServices() {
        if (CollectionUtils.isNotEmpty(services)) {
            return Collections.unmodifiableSet(services.stream().map(InventoryItemService::getService).collect(Collectors.toSet()));
        }
        return Collections.emptySet();
    }

    private void insertServiceIfDeviceTypeIsValid(Service service) {
        if (service.getDeviceType() == null || service.getDeviceType().equals(device.getType())) {
            InventoryItemService s = new InventoryItemService();
            s.setInventoryItem(this);
            s.setService(service);
            services.add(s);
        } else {
            throw new IllegalArgumentException("Service is not valid for this device");
        }
    }

    public void removeService(Service service) {
        if (CollectionUtils.isNotEmpty(services)) {
            Optional<InventoryItemService> serviceFound = services.stream().filter(s -> s.getService().equals(service)).findAny();
            if (serviceFound.isPresent()) {
                services.remove(serviceFound.get());
            }
        }
    }

    public Double calculateTotalServiceCost() {
        if (CollectionUtils.isNotEmpty(services)) {
            return services.stream().parallel()
                    .map(InventoryItemService::getService)
                    .map(Service::getPrice).reduce(Double::sum).orElse(new Double("0"));
        }
        return Double.valueOf(0);
    }
}
