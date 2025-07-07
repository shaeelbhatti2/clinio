package com.clinio.shared.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class ClinicScopedEntity {

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    protected ClinicScopedEntity() {
    }

    protected ClinicScopedEntity(UUID clinicId) {
        this.clinicId = clinicId;
    }

    public UUID getClinicId() {
        return clinicId;
    }

    public void setClinicId(UUID clinicId) {
        this.clinicId = clinicId;
    }
}
