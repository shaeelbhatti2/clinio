package com.clinio.shared.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "providers")
public class ProviderEntity extends ClinicScopedEntity {

    @Id
    private UUID id;

    @jakarta.persistence.Column(name = "user_id", nullable = false)
    private UUID userId;

    @jakarta.persistence.Column(name = "npi_number", nullable = false)
    private String npiNumber;

    private String specialty;

    @jakarta.persistence.Column(name = "created_at")
    private Instant createdAt;

    protected ProviderEntity() {
    }

    public ProviderEntity(UUID id, UUID clinicId, UUID userId, String npiNumber, String specialty) {
        super(clinicId);
        this.id = id;
        this.userId = userId;
        this.npiNumber = npiNumber;
        this.specialty = specialty;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getNpiNumber() {
        return npiNumber;
    }
}
