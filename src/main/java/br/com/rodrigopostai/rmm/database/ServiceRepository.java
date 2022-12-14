package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("select s from Service s where s.deviceType.id = :id order by s.name asc")
    List<Service> findServicesByDeviceTypeId(Long id);

}
