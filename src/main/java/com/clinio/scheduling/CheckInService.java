package com.clinio.scheduling;

import com.clinio.auth.CurrentUserService;
import com.clinio.scheduling.dto.AppointmentResponse;
import com.clinio.scheduling.dto.WaitingRoomEntry;
import com.clinio.scheduling.persistence.AppointmentEntity;
import com.clinio.scheduling.persistence.AppointmentRepository;
import com.clinio.shared.domain.DomainValidationException;
import com.clinio.shared.domain.appointment.AppointmentStatuses;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckInService {

    private final AppointmentRepository appointmentRepository;
    private final CurrentUserService currentUserService;
    private final SchedulingService schedulingService;

    public CheckInService(
            AppointmentRepository appointmentRepository,
            CurrentUserService currentUserService,
            SchedulingService schedulingService) {
        this.appointmentRepository = appointmentRepository;
        this.currentUserService = currentUserService;
        this.schedulingService = schedulingService;
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse checkIn(UUID appointmentId) {
        AppointmentEntity entity = loadClinicAppointment(appointmentId);
        if (!AppointmentStatuses.SCHEDULED.label().equals(entity.getStatus())) {
            throw new DomainValidationException("appointment not eligible for check-in");
        }
        entity.setStatus(AppointmentStatuses.CHECKED_IN.label());
        return schedulingService.toPublicResponse(appointmentRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse startVisit(UUID appointmentId) {
        AppointmentEntity entity = loadClinicAppointment(appointmentId);
        if (!AppointmentStatuses.CHECKED_IN.label().equals(entity.getStatus())) {
            throw new DomainValidationException("patient must be checked in");
        }
        entity.setStatus(AppointmentStatuses.IN_PROGRESS.label());
        return schedulingService.toPublicResponse(appointmentRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse completeVisit(UUID appointmentId, boolean noShow) {
        AppointmentEntity entity = loadClinicAppointment(appointmentId);
        if (noShow) {
            entity.setStatus(AppointmentStatuses.NO_SHOW.label());
        } else if (AppointmentStatuses.IN_PROGRESS.label().equals(entity.getStatus())
                || AppointmentStatuses.CHECKED_IN.label().equals(entity.getStatus())) {
            entity.setStatus(AppointmentStatuses.COMPLETED.label());
        } else {
            throw new DomainValidationException("appointment not in progress");
        }
        return schedulingService.toPublicResponse(appointmentRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<WaitingRoomEntry> waitingRoomQueue() {
        UUID clinicId = currentUserService.requireClinicId();
        Instant now = Instant.now();
        return appointmentRepository.findByClinicIdAndStatusOrderByStartAtAsc(
                        clinicId, AppointmentStatuses.CHECKED_IN.label())
                .stream()
                .sorted(Comparator.comparing(AppointmentEntity::getStartAt))
                .map(entity -> new WaitingRoomEntry(
                        entity.getId(),
                        entity.getPatientId(),
                        entity.getProviderId(),
                        entity.getStartAt(),
                        Duration.between(entity.getStartAt(), now).abs()))
                .toList();
    }

    private AppointmentEntity loadClinicAppointment(UUID appointmentId) {
        UUID clinicId = currentUserService.requireClinicId();
        return appointmentRepository.findById(appointmentId)
                .filter(entity -> entity.getClinicId().equals(clinicId))
                .orElseThrow(() -> new DomainValidationException("appointment not found"));
    }
}
