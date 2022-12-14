package br.com.rodrigopostai.rmm.database;

import br.com.rodrigopostai.rmm.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

}
