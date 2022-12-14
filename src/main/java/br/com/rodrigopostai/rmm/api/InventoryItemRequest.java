package br.com.rodrigopostai.rmm.api;

import lombok.Data;

import java.util.List;

@Data
public class InventoryItemRequest {
    private Long device;
    private List<Long> services;
}
