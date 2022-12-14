package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.model.Service;
import br.com.rodrigopostai.rmm.database.ServiceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/service")
public class ServiceController extends BaseController<Service, Long> {

    @Autowired
    public ServiceController(ServiceRepository repository) {
        super(repository);
    }

    @GetMapping("/type/{id}")
    public ResponseEntity<List<Service>> findServicesByDeviceType(@PathVariable("id") Long deviceTypeId) {
        List<Service> services = ((ServiceRepository) getRepository()).findServicesByDeviceTypeId(deviceTypeId);
        if (CollectionUtils.isNotEmpty(services)) {
            return ResponseEntity.ok(services);
        }
        return ResponseEntity.noContent().build();
    }
}
