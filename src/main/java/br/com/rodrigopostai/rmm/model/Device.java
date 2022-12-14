package br.com.rodrigopostai.rmm.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "device", uniqueConstraints = @UniqueConstraint(name="idx_unique_device_device_type",columnNames = {"system_name","device_type_id"}))
@Data
public class Device extends BaseEntity{

    @Column(name="system_name", length = 100, nullable = false, unique = true)
    @Size(max = 100, message = "Maximum length is 100 characters for System name")
    @NotNull(message = "System name is mandatory")
    private String systemName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_type_id", nullable = false)
    @NotNull(message = "Device type is mandatory")
    private DeviceType type;

}
