package br.com.rodrigopostai.rmm.model;

import br.com.rodrigopostai.rmm.builders.DeviceBuilder;
import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.builders.ServiceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class InventoryItemTest {

    private static final String SCREEN_SHARE = "Screen share";
    private static final String ANTIVIRUS_FOR_MAC = "Antivirus for Mac";

    private Device device;
    private Service antivirusForMac;
    private Service screenShare;

    @BeforeEach
    void setup() {
        device = DeviceBuilder.newBuilder().withSystemName("WKS001").withType(DeviceTypeBuilder.getWindowsDeviceType()).build();
        antivirusForMac = ServiceBuilder.newBuilder().withName(ANTIVIRUS_FOR_MAC).withPrice(7.0).build();
        screenShare = ServiceBuilder.newBuilder().withName(SCREEN_SHARE).withPrice(1.0).build();
    }

    @Test
    void shouldAddServicesToDevice() {
        InventoryItem item = new InventoryItem();
        item.addService(antivirusForMac).addService(screenShare);
        assertEquals(2, item.getServices().size());
        assertTrue(item.getServices().stream().anyMatch(d -> d.getName().equals(ANTIVIRUS_FOR_MAC)));
        assertTrue(item.getServices().stream().anyMatch(d -> d.getName().equals(SCREEN_SHARE)));
    }

    @Test
    void shouldAvoidDuplicateServicesOnTheSameDevice() {
        InventoryItem item = new InventoryItem();
        item.addService(antivirusForMac).addService(antivirusForMac);
        assertEquals(1,item.getServices().size());
    }

    @Test
    void shouldThrowExceptionsWhenAddingNewServicesDirectlyOnServicesSet() {
        final InventoryItem item = new InventoryItem();
        Set<Service> services = item.getServices();
        assertThrows(UnsupportedOperationException.class, () -> {
            services.add(antivirusForMac);
        });
    }

    @Test
    void shouldFailIfInvalidServiceIsInsertedAtDeviceType() {
        InventoryItem item = new InventoryItem();
        item.setDevice(device);
        item.addService(screenShare);
        Service antivirusForMac = ServiceBuilder.newBuilder().withName("Antivirus for Mac")
                .withDeviceType(DeviceTypeBuilder.getMacDeviceType())
                .withPrice(1.0).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> item.addService(antivirusForMac));
        assertEquals("Service is not valid for this device",exception.getMessage());
    }

    @Test
    void shouldAddServicesToAnInventoryItem() {
        Device mac = DeviceBuilder.newBuilder().withSystemName("WKS001").withType(DeviceTypeBuilder.getMacDeviceType()).build();
        Service service1 = ServiceBuilder.newBuilder().withDeviceType(DeviceTypeBuilder.getMacDeviceType())
                .withPrice(5.0)
                .withName("New service 1").build();
        Service service2 = ServiceBuilder.newBuilder().withDeviceType(DeviceTypeBuilder.getMacDeviceType())
                .withPrice(10.0)
                .withName("New service 2").build();
        InventoryItem item = new InventoryItem();
        item.setDevice(mac);
        item.addServices(Arrays.asList(service1, service2));
        assertEquals(2,item.getServices().size());

        assertThrows(Exception.class, () -> item.getServices().clear());

        item.clearServices();
        assertEquals(0,item.getServices().size());

    }
}
