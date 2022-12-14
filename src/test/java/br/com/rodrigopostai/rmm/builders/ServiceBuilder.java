package br.com.rodrigopostai.rmm.builders;

import br.com.rodrigopostai.rmm.model.DeviceType;
import br.com.rodrigopostai.rmm.model.Service;

import java.util.Objects;

public class ServiceBuilder {

    private String name;
    private Double price;
    private DeviceType deviceType;

    private ServiceBuilder() {

    }

    public static ServiceBuilder newBuilder() {
        return new ServiceBuilder();
    }

    public ServiceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ServiceBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public ServiceBuilder withDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public Service build() {
        Objects.requireNonNull(name, "Name is mandatory");
        Objects.requireNonNull(price, "Price is mandatory");
        Service service = new Service();
        service.setName(name);
        service.setPrice(price);
        service.setDeviceType(deviceType);
        return service;
    }


}
