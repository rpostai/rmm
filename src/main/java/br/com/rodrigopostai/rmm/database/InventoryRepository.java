package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
