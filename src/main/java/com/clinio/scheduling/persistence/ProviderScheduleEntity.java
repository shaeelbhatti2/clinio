package com.clinio.scheduling.persistence;

import com.clinio.shared.persistence.ClinicScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "provider_schedules")
public class ProviderScheduleEntity extends ClinicScopedEntity {

    @Id
    private UUID id;

    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Column(name = "day_of_week", nullable = false)
    private short dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "lunch_start")
    private LocalTime lunchStart;

    @Column(name = "lunch_end")
    private LocalTime lunchEnd;

    protected ProviderScheduleEntity() {
    }

    public ProviderScheduleEntity(UUID id, UUID clinicId, UUID providerId, short dayOfWeek,
                                  LocalTime startTime, LocalTime endTime) {
        super(clinicId);
        this.id = id;
        this.providerId = providerId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public short getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public LocalTime getLunchEnd() {
        return lunchEnd;
    }
}
