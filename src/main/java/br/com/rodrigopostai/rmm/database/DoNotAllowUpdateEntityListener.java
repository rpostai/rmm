package br.com.rodrigopostai.rmm.database;

import javax.persistence.PreUpdate;

public class DoNotAllowUpdateEntityListener {

    @PreUpdate
    public void doNotAllowEdit(Object value) {
        throw new IllegalStateException(String.format("%s cannot be updated", value.getClass().getSimpleName()));
    }

}
