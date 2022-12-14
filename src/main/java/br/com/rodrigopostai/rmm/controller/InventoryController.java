package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.database.InventoryRepository;
import br.com.rodrigopostai.rmm.api.InventoryItemRequest;
import br.com.rodrigopostai.rmm.model.Inventory;
import br.com.rodrigopostai.rmm.service.InventoryItemDTO;
import br.com.rodrigopostai.rmm.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/inventory")
public class InventoryController extends BaseController<Inventory, Long> {

    private final InventoryService inventoryService;

    @Autowired
    protected InventoryController(InventoryRepository repository, InventoryService inventoryService) {
        super(repository);
        this.inventoryService = inventoryService;
    }

    @Override
    @PostMapping("save")
    public ResponseEntity<Inventory> save(@RequestBody Inventory entity) {
        return ResponseEntity.ok(inventoryService.save(entity));
    }

    @PostMapping("{inventoryId}")
    public void addInventoryItem(@PathVariable("inventoryId") Long inventoryId,
                                 @RequestBody List<InventoryItemRequest> inventoryItemRequest) {
        List<InventoryItemDTO> inventoryItems = inventoryItemRequest.parallelStream().map(req -> {
            InventoryItemDTO dto = new InventoryItemDTO();
            dto.setDevice(req.getDevice());
            dto.setServices(req.getServices());
            return dto;
        }).collect(Collectors.toList());
        inventoryService.addInventoryItem(inventoryId,inventoryItems);
    }

    @PostMapping("/item/{inventoryItemId}/service/{serviceId}")
    public void addServiceToInventoryItem(@PathVariable("inventoryItemId") Long inventoryItemId,
                                          @PathVariable("serviceId") Long serviceId) {
        inventoryService.addServiceToInventoryItem(inventoryItemId,serviceId);
    }

    @GetMapping("/{inventoryId}/cost")
    public Double calculateCost(@PathVariable("inventoryId") Long inventoryId) {
        return inventoryService.calculateTotalServiceCost(inventoryId);
    }

    @GetMapping("/item/{inventoryItemId}/cost")
    public Double calculateCostByDevice(@PathVariable("inventoryItemId") Long inventoryItemId) {
        return inventoryService.calculateServiceCostByItem(inventoryItemId);
    }

    @DeleteMapping("item/{inventoryItemId}")
    public void deleteItemFromInventory(@PathVariable("inventoryItemId") Long inventoryItemId) {
        inventoryService.deleteInventoryInventoryItem(inventoryItemId);
    }
}
