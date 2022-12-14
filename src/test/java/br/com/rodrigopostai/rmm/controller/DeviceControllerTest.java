package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.builders.DeviceBuilder;
import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.model.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Sql("/clear.sql")
@Sql("/device_type.sql")
@Sql("/device.sql")
class DeviceControllerTest extends AbstractControllerTest{

    @Override
    protected String getEndpoint() {
        return "device";
    }

    @BeforeEach
    void setup() {
        windows = DeviceTypeBuilder.getWindowsDeviceType();
    }

    @Test
    void shouldFindByIdSuccessfully() {
        String url = getHost()+"/1000";
        Device device = client.getForObject(url, Device.class);
        assertEquals(1000L, device.getId());
        assertEquals("My mac", device.getSystemName());
    }

    @Test
    void shouldSaveDevice() {
        Device myWindows = DeviceBuilder.newBuilder().withSystemName("Windows").withType(this.windows).build();
        ResponseEntity<Device> response = client.postForEntity(getHost(), myWindows, Device.class);
        assertEquals(200, response.getStatusCodeValue());
        Device savedDevice = response.getBody();
        assertNotNull(savedDevice);
        assertNotNull(savedDevice.getId());
        assertEquals("Windows", savedDevice.getSystemName());
        assertEquals(windows, savedDevice.getType());
    }

    @Test
    void shouldFailByUpdatingDevice() {
        Device myWindows = DeviceBuilder.newBuilder().withSystemName("My mac renamed").withType(DeviceTypeBuilder.getWindowsDeviceType()).build();
        myWindows.setId(1000L);
        HttpEntity<Device> entity = new HttpEntity<>(myWindows);
        ResponseEntity<String> response = client.exchange(getHost(), HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
        });
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Device cannot be updated", response.getBody());
    }

    @Test
    void shouldFailWhenDuplicateDeviceIsInserted() {
        Device myNewMac = DeviceBuilder.newBuilder().withSystemName("My mac").withType(DeviceTypeBuilder.getMacDeviceType()).build();
        ResponseEntity<String> response = client.postForEntity(getHost(), myNewMac, String.class);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Record already exists", response.getBody());
    }

    @Test
    void shouldDeleteDevice() {
        String url = getHost() + "/1000";
        client.delete(url);
        Device device = client.getForObject(url, Device.class);
        assertNull(device);
    }
}
