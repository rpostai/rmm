package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
