package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.builders.DeviceTypeBuilder;
import br.com.rodrigopostai.rmm.model.DeviceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public abstract class AbstractControllerTest {

    private static final String URL = "http://localhost:%d/%s";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    protected TestRestTemplate client;

    protected String getHost() {
        return String.format(URL, port,getEndpoint());
    }

    protected DeviceType windows = DeviceTypeBuilder.getWindowsDeviceType();

    protected abstract String getEndpoint();
}
