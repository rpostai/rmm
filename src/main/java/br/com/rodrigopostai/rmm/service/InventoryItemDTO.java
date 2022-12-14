package br.com.rodrigopostai.rmm.service;

import lombok.Data;

import java.util.List;

@Data
public class InventoryItemDTO {

    private Long device;
    private List<Long> services;
}
