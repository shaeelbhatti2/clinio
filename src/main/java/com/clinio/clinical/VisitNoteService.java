package com.clinio.clinical;

import com.clinio.auth.CurrentUserService;
import com.clinio.clinical.dto.SaveVisitNoteRequest;
import com.clinio.clinical.dto.VisitNoteResponse;
import com.clinio.clinical.persistence.VisitNoteEntity;
import com.clinio.clinical.persistence.VisitNoteRepository;
import com.clinio.scheduling.persistence.AppointmentEntity;
import com.clinio.scheduling.persistence.AppointmentRepository;
import com.clinio.shared.domain.DomainValidationException;
import com.clinio.shared.domain.appointment.AppointmentStatuses;
import com.clinio.shared.persistence.UserEntity;
import com.clinio.shared.security.ClinicSecurityService;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitNoteService {

    private final VisitNoteRepository visitNoteRepository;
    private final AppointmentRepository appointmentRepository;
    private final CurrentUserService currentUserService;
    private final ClinicSecurityService clinicSecurityService;

    public VisitNoteService(
            VisitNoteRepository visitNoteRepository,
            AppointmentRepository appointmentRepository,
            CurrentUserService currentUserService,
            ClinicSecurityService clinicSecurityService) {
        this.visitNoteRepository = visitNoteRepository;
        this.appointmentRepository = appointmentRepository;
        this.currentUserService = currentUserService;
        this.clinicSecurityService = clinicSecurityService;
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canWriteClinicalNotes()")
    public VisitNoteResponse startFromAppointment(UUID appointmentId) {
        UUID clinicId = currentUserService.requireClinicId();
        UserEntity provider = currentUserService.currentUser()
                .orElseThrow(() -> new DomainValidationException("provider required"));
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .filter(entity -> entity.getClinicId().equals(clinicId))
                .orElseThrow(() -> new DomainValidationException("appointment not found"));
        if (!AppointmentStatuses.COMPLETED.label().equals(appointment.getStatus())
                && !AppointmentStatuses.IN_PROGRESS.label().equals(appointment.getStatus())) {
            throw new DomainValidationException("appointment not ready for note");
        }
        List<VisitNoteEntity> existing = visitNoteRepository.findByClinicIdAndAppointmentId(clinicId, appointmentId);
        if (!existing.isEmpty()) {
            return toResponse(existing.getFirst());
        }
        VisitNoteEntity entity = new VisitNoteEntity(
                UUID.randomUUID(),
                clinicId,
                appointmentId,
                appointment.getPatientId(),
                provider.getId(),
                "SOAP");
        return toResponse(visitNoteRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canWriteClinicalNotes()")
    public VisitNoteResponse saveDraft(UUID noteId, SaveVisitNoteRequest request) {
        VisitNoteEntity entity = loadEditableNote(noteId);
        entity.updateSections(request.subjective(), request.objective(), request.assessment(), request.plan());
        return toResponse(visitNoteRepository.save(entity));
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canWriteClinicalNotes()")
    public VisitNoteResponse sign(UUID noteId) {
        VisitNoteEntity entity = loadEditableNote(noteId);
        UUID providerId = currentUserService.currentUser()
                .map(UserEntity::getId)
                .orElseThrow(() -> new DomainValidationException("provider required"));
        if (!entity.getProviderId().equals(providerId)) {
            throw new DomainValidationException("only authoring provider can sign");
        }
        entity.sign();
        return toResponse(visitNoteRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canReadClinicalNotes()")
    public List<VisitNoteResponse> listForPatient(UUID patientId) {
        UUID clinicId = currentUserService.requireClinicId();
        return visitNoteRepository.findByClinicIdAndPatientIdOrderByCreatedAtDesc(clinicId, patientId).stream()
                .map(this::toResponse)
                .toList();
    }

    private VisitNoteEntity loadEditableNote(UUID noteId) {
        UUID clinicId = currentUserService.requireClinicId();
        VisitNoteEntity entity = visitNoteRepository.findById(noteId)
                .filter(note -> note.getClinicId().equals(clinicId))
                .orElseThrow(() -> new DomainValidationException("note not found"));
        if (entity.isSigned()) {
            throw new DomainValidationException("signed notes are immutable");
        }
        return entity;
    }

    private VisitNoteResponse toResponse(VisitNoteEntity entity) {
        return new VisitNoteResponse(
                entity.getId(),
                entity.getAppointmentId(),
                entity.getPatientId(),
                entity.getProviderId(),
                entity.getTemplateCode(),
                entity.getSubjective(),
                entity.getObjective(),
                entity.getAssessment(),
                entity.getPlan(),
                entity.isSigned(),
                entity.getSignedAt());
    }
}
