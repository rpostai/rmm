package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.model.Device;
import br.com.rodrigopostai.rmm.database.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/device")
public class DeviceController extends BaseController<Device,Long> {
    @Autowired
    public DeviceController(DeviceRepository repository) {
        super(repository);

    }
}
