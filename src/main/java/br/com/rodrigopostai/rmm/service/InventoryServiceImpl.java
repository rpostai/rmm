package br.com.rodrigopostai.rmm.service;

import br.com.rodrigopostai.rmm.database.InventoryRepository;
import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.model.Service;
import br.com.rodrigopostai.rmm.database.DeviceRepository;
import br.com.rodrigopostai.rmm.database.InventoryItemRepository;
import br.com.rodrigopostai.rmm.database.ServiceRepository;
import br.com.rodrigopostai.rmm.model.Inventory;
import br.com.rodrigopostai.rmm.model.InventoryItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final ApplicationEventPublisher publisher;

    private final InventoryRepository inventoryRepository;

    private final InventoryItemRepository inventoryItemRepository;

    private final ServiceRepository serviceRepository;

    private final DeviceRepository deviceRepository;

    private final InventoryServiceCache inventoryServiceCache;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                InventoryItemRepository inventoryItemRepository,
                                ServiceRepository serviceRepository, DeviceRepository deviceRepository,
                                ApplicationEventPublisher publisher,
                                InventoryServiceCache inventoryServiceCache) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.serviceRepository = serviceRepository;
        this.deviceRepository = deviceRepository;
        this.publisher = publisher;
        this.inventoryServiceCache = inventoryServiceCache;
    }

    @Override
    public Inventory save(Inventory entity) {
        for (InventoryItem item : entity.getItems()) {
            Optional<Device> device = deviceRepository.findById(item.getDevice().getId());
            if (device.isPresent()) {
                item.setDevice(device.get());
            } else {
                throw new IllegalArgumentException("Device not found");
            }
            List<Service> managedServices = new ArrayList<>();
            for (Service service : item.getServices()) {
                Optional<Service> serviceFound = serviceRepository.findById(service.getId());
                if (serviceFound.isPresent()) {
                    managedServices.add(serviceFound.get());
                }
            }
            entity.clearItems();
            entity.addInventoryItem(device.get(),managedServices);
        }
        return inventoryRepository.save(entity);
    }

    @Override
    public void addInventoryItem(Long inventoryId, List<InventoryItemDTO> items) {
        Inventory inventory = getInventory(inventoryId);
        if (CollectionUtils.isNotEmpty(items)) {
            items.forEach(item -> {
                Device device = getDevice(item.getDevice());
                List<Service> services = getServices(item.getServices());
                if (CollectionUtils.isNotEmpty(services)) {
                    inventory.addInventoryItem(device, services);
                }
            });
        }
    }

    private List<Service> getServices(List<Long> services) {
        return serviceRepository.findAllById(services);
    }

    @Override
    public void addServiceToInventoryItem(Long inventoryItemId, Long serviceId) {
        Service service = getService(serviceId);
        InventoryItem item = getInventoryItem(inventoryItemId);
        item.addService(service);
        publisher.publishEvent(new InventoryItemUpdated(item, InventoryItemUpdatedAction.UPDATE));
    }

    private InventoryItem getInventoryItem(Long inventoryItemId) {
        return inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory id does not belong to the inventory"));
    }

    @Override
    public Double calculateServiceCostByItem(Long inventoryItemId) {
        return inventoryServiceCache.getCost(inventoryItemId);
    }

    @Override
    public void deleteInventoryInventoryItem(Long inventoryItemId) {
        InventoryItem inventoryItem = inventoryItemRepository
                .findById(inventoryItemId).orElseThrow(() -> new IllegalArgumentException("Inventory item was not found"));
        inventoryItemRepository.delete(inventoryItem);
        publisher.publishEvent(new InventoryItemUpdated(inventoryItem, InventoryItemUpdatedAction.REMOVE));
    }

    @Override
    public Double calculateTotalServiceCost(Long inventoryId) {
        return getInventory(inventoryId).calculateTotalServiceCost();
    }

    private Device getDevice(Long deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        return device.orElseThrow(() -> new IllegalArgumentException(String.format("Device %d was not found", deviceId)));
    }

    private Service getService(Long serviceId) {
        Optional<Service> service = serviceRepository.findById(serviceId);
        return service.orElseThrow(() -> new IllegalArgumentException(String.format("Service %d was not found", serviceId)));
    }

    private Inventory getInventory(Long inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Inventory %d was not found", inventoryId)));
    }

}
