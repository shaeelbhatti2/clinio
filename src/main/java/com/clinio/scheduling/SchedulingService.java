package com.clinio.scheduling;

import com.clinio.auth.CurrentUserService;
import com.clinio.scheduling.dto.AppointmentResponse;
import com.clinio.scheduling.dto.BookAppointmentRequest;
import com.clinio.scheduling.dto.CreateScheduleBlockRequest;
import com.clinio.scheduling.persistence.AppointmentEntity;
import com.clinio.scheduling.persistence.AppointmentRepository;
import com.clinio.scheduling.persistence.ProviderScheduleEntity;
import com.clinio.scheduling.persistence.ProviderScheduleRepository;
import com.clinio.shared.domain.DomainValidationException;
import com.clinio.shared.domain.appointment.AppointmentStatuses;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchedulingService {

    private final ProviderScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final CurrentUserService currentUserService;

    public SchedulingService(
            ProviderScheduleRepository scheduleRepository,
            AppointmentRepository appointmentRepository,
            CurrentUserService currentUserService) {
        this.scheduleRepository = scheduleRepository;
        this.appointmentRepository = appointmentRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public ProviderScheduleEntity addScheduleBlock(CreateScheduleBlockRequest request) {
        UUID clinicId = currentUserService.requireClinicId();
        if (!request.endTime().isAfter(request.startTime())) {
            throw new DomainValidationException("invalid schedule block times");
        }
        ProviderScheduleEntity entity = new ProviderScheduleEntity(
                UUID.randomUUID(),
                clinicId,
                request.providerId(),
                (short) request.dayOfWeek().getValue(),
                request.startTime(),
                request.endTime());
        return scheduleRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<ProviderScheduleEntity> listProviderSchedule(UUID providerId) {
        UUID clinicId = currentUserService.requireClinicId();
        return scheduleRepository.findByClinicIdAndProviderId(clinicId, providerId);
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse book(BookAppointmentRequest request) {
        UUID clinicId = currentUserService.requireClinicId();
        Instant endAt = request.startAt().plus(request.durationMinutes(), ChronoUnit.MINUTES);
        ensureNoProviderConflict(clinicId, request.providerId(), request.startAt(), endAt);
        if (request.roomId() != null) {
            ensureNoRoomConflict(clinicId, request.roomId(), request.startAt(), endAt);
        }
        AppointmentEntity entity = new AppointmentEntity(
                UUID.randomUUID(),
                clinicId,
                request.patientId(),
                request.providerId(),
                request.roomId(),
                request.appointmentType(),
                request.startAt(),
                request.durationMinutes(),
                AppointmentStatuses.SCHEDULED.label());
        return toResponse(appointmentRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<AppointmentResponse> listByProvider(UUID providerId) {
        UUID clinicId = currentUserService.requireClinicId();
        return appointmentRepository.findByClinicIdAndProviderIdAndStartAtLessThanAndStatusNot(
                        clinicId, providerId, Instant.now().plus(365, ChronoUnit.DAYS), "CANCELLED")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void ensureNoProviderConflict(UUID clinicId, UUID providerId, Instant startAt, Instant endAt) {
        List<AppointmentEntity> candidates = appointmentRepository
                .findByClinicIdAndProviderIdAndStartAtLessThanAndStatusNot(clinicId, providerId, endAt, "CANCELLED");
        boolean conflict = candidates.stream().anyMatch(existing -> overlaps(existing, startAt, endAt));
        if (conflict) {
            throw new DomainValidationException("provider schedule conflict");
        }
    }

    private void ensureNoRoomConflict(UUID clinicId, UUID roomId, Instant startAt, Instant endAt) {
        List<AppointmentEntity> candidates = appointmentRepository
                .findByClinicIdAndRoomIdAndStartAtLessThanAndStatusNot(clinicId, roomId, endAt, "CANCELLED");
        boolean conflict = candidates.stream().anyMatch(existing -> overlaps(existing, startAt, endAt));
        if (conflict) {
            throw new DomainValidationException("room schedule conflict");
        }
    }

    private boolean overlaps(AppointmentEntity existing, Instant startAt, Instant endAt) {
        Instant existingEnd = existing.getStartAt().plus(existing.getDurationMinutes(), ChronoUnit.MINUTES);
        return existing.getStartAt().isBefore(endAt) && startAt.isBefore(existingEnd);
    }

    private AppointmentResponse toResponse(AppointmentEntity entity) {
        return new AppointmentResponse(
                entity.getId(),
                entity.getPatientId(),
                entity.getProviderId(),
                entity.getRoomId(),
                entity.getAppointmentType(),
                entity.getStartAt(),
                entity.getDurationMinutes(),
                entity.getStatus());
    }
}
