package br.com.rodrigopostai.rmm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Entity
@Table(name = "service", uniqueConstraints = @UniqueConstraint(name="idx_unique_service_device_type",columnNames = {"name","device_type_id"}))
@Data
@EqualsAndHashCode(of = {"name", "deviceType"})
public class Service extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "device_type_id", nullable = true)
    private DeviceType deviceType;

    @Column(scale = 2, precision = 13, nullable = false)
    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must higher than zero")
    @Digits(integer=13, fraction=2)
    private Double price;

    @PrePersist
    @PreUpdate
    public void validatePrice() {
        // This method was implemented because @DecimalMin and @Digits didn't work with BigDecimal or Double
        if (price != null && price < 0) {
            throw new ConstraintViolationException("Price must be higher than zero", Collections.emptySet());
        }
    }

}
