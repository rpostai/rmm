package br.com.rodrigopostai.rmm.model;

import br.com.rodrigopostai.rmm.database.DoNotAllowUpdateEntityListener;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Calendar;

@MappedSuperclass
@EntityListeners({DoNotAllowUpdateEntityListener.class})
@Data
@NoArgsConstructor
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Device.class, name = "device"),
        @JsonSubTypes.Type(value = Service.class, name = "service"),
        @JsonSubTypes.Type(value = Inventory.class, name = "inventory"),
        @JsonSubTypes.Type(value = InventoryItem.class, name = "inventory_item"),
        @JsonSubTypes.Type(value = InventoryItemService.class, name = "inventory_item_service")
})
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_at")
    private Calendar createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Calendar.getInstance();
    }
}
