package com.clinio.scheduling.persistence;

import com.clinio.shared.persistence.ClinicScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class AppointmentEntity extends ClinicScopedEntity {

    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "appointment_type", nullable = false)
    private String appointmentType;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    private String status;

    @Version
    private long version;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected AppointmentEntity() {
    }

    public AppointmentEntity(UUID id, UUID clinicId, UUID patientId, UUID providerId, UUID roomId,
                             String appointmentType, Instant startAt, int durationMinutes, String status) {
        super(clinicId);
        this.id = id;
        this.patientId = patientId;
        this.providerId = providerId;
        this.roomId = roomId;
        this.appointmentType = appointmentType;
        this.startAt = startAt;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
