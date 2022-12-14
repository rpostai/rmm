package br.com.rodrigopostai.rmm.builders;

import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.model.DeviceType;

import java.util.Objects;

public class DeviceBuilder {

    private String systemName;
    private DeviceType deviceType;

    private DeviceBuilder() {

    }

    public static DeviceBuilder newBuilder() {
        return new DeviceBuilder();
    }

    public DeviceBuilder withSystemName(String systemName) {
        this.systemName = systemName;
        return this;
    }

    public DeviceBuilder withType(DeviceType type) {
        this.deviceType = type;
        return this;
    }

    public Device build() {
        Objects.requireNonNull(systemName, "System name is mandatory");
        Objects.requireNonNull(deviceType, "Device type is mandatory");
        Device device = new Device();
        device.setSystemName(systemName);
        device.setType(deviceType);
        return device;
    }
}
