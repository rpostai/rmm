package br.com.rodrigopostai.rmm.service;

import br.com.rodrigopostai.rmm.model.InventoryItem;

import java.io.Serializable;

public class InventoryItemUpdated implements Serializable {

    private final InventoryItem item;
    private final InventoryItemUpdatedAction action;

    public InventoryItemUpdated(InventoryItem item, InventoryItemUpdatedAction action) {
        this.item = item;
        this.action = action;
    }

    public InventoryItem getItem() {
        return item;
    }

    public InventoryItemUpdatedAction getAction() {
        return action;
    }
}
