package com.clinio.shared.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "clinics")
public class ClinicEntity {

    @Id
    private UUID id;

    private String name;

    private String slug;

    private String timezone;

    @jakarta.persistence.Column(name = "address_line1")
    private String addressLine1;

    private String city;

    private String state;

    @jakarta.persistence.Column(name = "postal_code")
    private String postalCode;

    @jakarta.persistence.Column(name = "created_at")
    private Instant createdAt;

    protected ClinicEntity() {
    }

    public ClinicEntity(UUID id, String name, String slug, String timezone) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.timezone = timezone;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getTimezone() {
        return timezone;
    }
}
