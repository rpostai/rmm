package br.com.rodrigopostai.rmm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "inventory_item_service")
@Data
@ToString(exclude = "inventoryItem")
public class InventoryItemService extends  BaseEntity{

    @ManyToOne
    @JoinColumn(name = "inventory_item_id", nullable = false)
    @JsonBackReference
    @NotNull
    private InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull
    private Service service;

}
