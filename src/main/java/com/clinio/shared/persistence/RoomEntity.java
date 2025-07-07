package com.clinio.shared.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "rooms")
public class RoomEntity extends ClinicScopedEntity {

    @Id
    private UUID id;

    private String name;

    private boolean active;

    protected RoomEntity() {
    }

    public RoomEntity(UUID id, UUID clinicId, String name) {
        super(clinicId);
        this.id = id;
        this.name = name;
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
