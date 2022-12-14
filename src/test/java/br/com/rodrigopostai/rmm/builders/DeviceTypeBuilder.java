package br.com.rodrigopostai.rmm.builders;

import br.com.rodrigopostai.rmm.model.DeviceType;

public class DeviceTypeBuilder {

    public static DeviceType getWindowsDeviceType() {
        DeviceType deviceType = new DeviceType();
        deviceType.setId(2L);
        deviceType.setName("Windows");
        return deviceType;
    }

    public static  DeviceType getMacDeviceType() {
        DeviceType deviceType = new DeviceType();
        deviceType.setId(1L);
        deviceType.setName("Mac");
        return deviceType;
    }

    public static  DeviceType getLinuxDeviceType() {
        DeviceType deviceType = new DeviceType();
        deviceType.setId(3L);
        deviceType.setName("Linux");
        return deviceType;
    }

    public static  DeviceType getGenericDeviceType() {
        DeviceType deviceType = new DeviceType();
        deviceType.setId(5L);
        deviceType.setName("Generic Device");
        return deviceType;
    }
}
