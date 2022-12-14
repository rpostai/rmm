package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.builders.ServiceBuilder;
import br.com.rodrigopostai.rmm.model.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Sql("/clear.sql")
@Sql("/device_type.sql")
@Sql("/service_data.sql")
class ServiceControllerTest extends AbstractControllerTest{

    @Override
    protected String getEndpoint() {
        return "service";
    }

    @Test
    void shouldReturnAllValidServicesForDeviceType() {
        ResponseEntity<List<Service>> response =
                client.exchange(getHost() + "/type/2", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Service>>() {
                });
        assertEquals(200, response.getStatusCodeValue());
        List<Service> services = response.getBody();
        assertEquals(1, services.size());
        Service service =  services.get(0);
        assertEquals("Antivirus for Windows",service.getName());
        assertEquals("Windows", service.getDeviceType().getName());
        assertEquals(5.00, service.getPrice());
    }

    @Test
    void shouldReturnNoServicesForADeviceType() {
        ResponseEntity<List<Service>> response =
                client.exchange(getHost() + "/type/4", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Service>>() {
                });
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldFindServiceSuccessfully() {
        Service service = client.getForObject(getHost() + "/1000", Service.class);
        assertNotNull(service);
        assertEquals("Screen share", service.getName());
        assertEquals(1.00, service.getPrice());
    }

    @Test
    void shouldSaveService() {
        final String serviceName = "Disk cleaning";
        final Double price = 1.0;
        Service service = ServiceBuilder.newBuilder().withName(serviceName)
                .withDeviceType(DeviceTypeBuilder.getWindowsDeviceType()).withPrice(price).build();
        ResponseEntity<Service> response = client.postForEntity(getHost(), service, Service.class);
        assertEquals(200, response.getStatusCodeValue());
        Service savedService = response.getBody();
        assertNotNull(savedService);
        assertEquals(serviceName, savedService.getName());
        assertEquals(price, savedService.getPrice());
        Assertions.assertEquals(DeviceTypeBuilder.getWindowsDeviceType(), savedService.getDeviceType());
    }

    @Test
    void shouldFailByUpdatingService() {
        Service service = ServiceBuilder.newBuilder().withName("Screen share renamed").withPrice(100.0).build();
        service.setId(1000L);
        HttpEntity<Service> entity = new HttpEntity<>(service);
        ResponseEntity<String> response = client.exchange(getHost(), HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
        });
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Service cannot be updated", response.getBody());
    }

    @Test
    void shouldFailWhenDuplicateServiceIsInserted() {
        Service service = ServiceBuilder.newBuilder().withName("Backup")
                .withDeviceType(DeviceTypeBuilder.getGenericDeviceType())
                .withPrice(10.0).build();
        ResponseEntity<String> response = client.postForEntity(getHost(), service, String.class);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Record already exists", response.getBody());
    }

    @Test
    void shouldDeleteService() {
        String url = getHost() + "/1000";
        client.delete(url);
        Service service = client.getForObject(url, Service.class);
        assertNull(service);
    }
}
