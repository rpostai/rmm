package br.com.rodrigopostai.rmm.model;

import br.com.rodrigopostai.rmm.builders.DeviceBuilder;
import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.builders.ServiceBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryTest {

    @Test
    void shouldCalculateTotalCostForSeveralDevicesAndServices() {
        Device windows = DeviceBuilder.newBuilder().withSystemName("Windows").withType(DeviceTypeBuilder.getWindowsDeviceType()).build();

        Device mac = DeviceBuilder.newBuilder().withSystemName("Mac").withType(DeviceTypeBuilder.getMacDeviceType()).build();

        Service deviceRent = ServiceBuilder.newBuilder().withName("Rent of any device").withPrice(4.0).build();

        Service antivirusForWindows = ServiceBuilder.newBuilder().withName("Antivirus")
                .withDeviceType(DeviceTypeBuilder.getWindowsDeviceType())
                .withPrice(5.0)
                .build();
        Service antivirusForMac = ServiceBuilder.newBuilder().withName("Antivirus")
                .withDeviceType(DeviceTypeBuilder.getMacDeviceType())
                .withPrice(7.0)
                .build();

        Service backup = ServiceBuilder.newBuilder().withName("Backup")
                .withPrice(3.0)
                .build();

        Service screenShare = ServiceBuilder.newBuilder().withName("Screen share")
                .withPrice(1.0).build();

        Inventory inventory = new Inventory();
        inventory.addInventoryItem(windows, deviceRent, antivirusForWindows, backup, screenShare);
        inventory.addInventoryItem(windows, deviceRent, antivirusForWindows, screenShare);
        inventory.addInventoryItem(mac, deviceRent, antivirusForMac,backup,screenShare);
        inventory.addInventoryItem(mac, deviceRent, antivirusForMac, backup, screenShare);
        inventory.addInventoryItem(mac, deviceRent, antivirusForMac);


        assertEquals(64.0, inventory.calculateTotalServiceCost());

    }
}
