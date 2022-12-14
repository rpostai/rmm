package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.builders.ServiceBuilder;
import br.com.rodrigopostai.rmm.model.DeviceType;
import br.com.rodrigopostai.rmm.model.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql("/device_type.sql")
@Sql("/device.sql")
class ServiceRepositoryTest {

    private static final String ANTIVIRUS_FOR_WINDOWS = "Antivirus for windows";
    private static final Double PRICE = new Double("5.00");

    @Autowired
    private EntityManager em;

    @Autowired
    private ServiceRepository repository;

    private DeviceType deviceWindows;
    private Service antivirusForWindows;

    @BeforeEach
    void setup() {
        deviceWindows = em.find(DeviceType.class, 2L);
        antivirusForWindows = ServiceBuilder.newBuilder().withName(ANTIVIRUS_FOR_WINDOWS).withPrice(PRICE).withDeviceType(deviceWindows).build();
    }

    @Test
    void shouldSaveService() {
        Service savedService = repository.saveAndFlush(antivirusForWindows);
        assertNotNull(savedService.getId());
        em.clear();
        savedService = em.find(Service.class, antivirusForWindows.getId());
        assertNotNull(savedService);
        assertEquals(ANTIVIRUS_FOR_WINDOWS, savedService.getName());
        assertEquals(PRICE, savedService.getPrice());
        assertEquals(deviceWindows, savedService.getDeviceType());
    }

    @Test
    void shouldFailWhenDuplicateServiceIsInserted() {
        em.persist(antivirusForWindows);
        em.flush();
        em.clear();
        Service duplicatedService = ServiceBuilder.newBuilder()
                .withName(ANTIVIRUS_FOR_WINDOWS).withDeviceType(deviceWindows).withPrice(10.0).build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(duplicatedService);
        });
    }

    @Test
    void shouldFailByUpdatingService() {
        em.persist(antivirusForWindows);
        em.flush();
        em.clear();
        Optional<Service> service = repository.findById(antivirusForWindows.getId());
        assertTrue(service.isPresent());
        Service serviceToBeUpdated = service.get();
        serviceToBeUpdated.setName("New name");
        Assertions.assertThrows(Exception.class, () -> {
           repository.saveAndFlush(serviceToBeUpdated);
        });
    }

    @Test
    @Sql("/device_type.sql")
    @Sql("/service_data.sql")
    void shouldDeleteService() {
        Service service = em.find(Service.class, 1000L);
        assertNotNull(service);
        repository.deleteById(1000L);
        repository.flush();
        service = em.find(Service.class, 1000L);
        assertNull(service);
    }

    @Test
    @Sql("/device_type.sql")
    @Sql("/service_data.sql")
    void shouldReturnAllServicesByDeviceType() {
        List<Service> services = repository.findServicesByDeviceTypeId(2L);
        assertEquals(1, services.size());
        assertEquals("Antivirus for Windows", services.get(0).getName());
    }

    @Test
    void shouldFailWhenDataIsInvalid() {
        Service service = new Service();
        service.setName("Backup");
        service.setDeviceType(DeviceTypeBuilder.getMacDeviceType());
        ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(service);
        });
        assertEquals(1, exception.getConstraintViolations().size());
        ConstraintViolation<?> constraint = exception.getConstraintViolations().iterator().next();
        assertEquals("Price is mandatory", constraint.getMessage());

        em.clear();

        final Service service1 = new Service();
        service1.setName("Backup");
        service1.setDeviceType(DeviceTypeBuilder.getMacDeviceType());
        service1.setPrice(-10.00);
        exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            repository.save(service1);
        });

        assertEquals("Price must be higher than zero", exception.getMessage());

        em.clear();

    }

}
