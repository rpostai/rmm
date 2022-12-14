package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.builders.DeviceBuilder;
import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.model.DeviceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql("/device_type.sql")
@Sql("/device.sql")
class DeviceRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private DeviceRepository repository;

    private DeviceType deviceTypeMac;
    private DeviceType deviceTypeWindows;

    @BeforeEach
    void setup() {
        deviceTypeMac = em.find(DeviceType.class,1L);
        deviceTypeWindows = em.find(DeviceType.class, 2L);
    }

    @Test
    @Sql("/device_type.sql")
    void shouldSaveDevice() {
        Device mac = DeviceBuilder.newBuilder().withSystemName("Mac").withType(deviceTypeMac).build();
        mac = repository.save(mac);
        assertNotNull(mac.getId());
        repository.flush();
        Device savedDevice = em.find(Device.class, mac.getId());
        assertNotNull(savedDevice);
        assertNotNull(savedDevice.getCreatedAt());
    }

    @Test
    void shouldFailWhenDuplicateDeviceIsInserted() {
        Device mac = DeviceBuilder.newBuilder().withSystemName("My mac").withType(deviceTypeMac).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(mac);
        });
    }

    @Test
    void shouldDeleteDevice() {
        Device device = em.find(Device.class, 1000L);
        assertNotNull(device);
        repository.deleteById(1000L);
        repository.flush();
        em.flush();
        em.clear();
        device = em.find(Device.class, 1000L);
        assertNull(device);
    }

    @Test
    void shouldFailByUpdatingDevice() {
        Optional<Device> device = repository.findById(1000L);
        assertTrue(device.isPresent());
        Device deviceToBeUpdated = device.get();
        deviceToBeUpdated.setType(deviceTypeWindows);
        InvalidDataAccessApiUsageException exception =
                Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
                    repository.saveAndFlush(deviceToBeUpdated);
                });
        assertTrue(exception.getMessage().contains("Device cannot be updated"));
    }

    @Test
    void shouldFailWhenDataIsInvalid() {
        final Device device = new Device();
        device.setSystemName("Windows");
        ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(device);
        });
        assertEquals(1, exception.getConstraintViolations().size());
        ConstraintViolation<?> constraint = exception.getConstraintViolations().iterator().next();
        assertEquals("Device type is mandatory", constraint.getMessage());

        em.clear();

        final Device device1 = new Device();
        device1.setType(DeviceTypeBuilder.getMacDeviceType());
        device1.setSystemName("System name with more than one hundred chars System name with more " +
                "than one hundred chars System name with more than one hundred chars");
        exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(device1);
        });

        assertEquals(1, exception.getConstraintViolations().size());
        constraint = exception.getConstraintViolations().iterator().next();
        assertEquals("Maximum length is 100 characters for System name", constraint.getMessage());

        em.clear();

        final Device device2 = new Device();
        exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(device2);
        });

        assertEquals(2, exception.getConstraintViolations().size());
        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch( c -> c.getMessage().equals("Device type is mandatory")));
        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch( c -> c.getMessage().equals("System name is mandatory")));
    }

}
