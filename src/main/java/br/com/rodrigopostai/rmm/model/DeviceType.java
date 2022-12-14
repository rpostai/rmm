package br.com.rodrigopostai.rmm.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "device_type")
@Data
public class DeviceType {

    @Id
    @GeneratedValue()
    private Long id;

    @NotNull
    @Column(length = 200)
    private String name;
}
