package com.clinio.clinical.persistence;

import com.clinio.shared.persistence.ClinicScopedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "visit_notes")
public class VisitNoteEntity extends ClinicScopedEntity {

    @Id
    private UUID id;

    @Column(name = "appointment_id", nullable = false)
    private UUID appointmentId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Column(name = "template_code", nullable = false)
    private String templateCode;

    @Column(columnDefinition = "TEXT")
    private String subjective;

    @Column(columnDefinition = "TEXT")
    private String objective;

    @Column(columnDefinition = "TEXT")
    private String assessment;

    @Column(columnDefinition = "TEXT")
    private String plan;

    @Column(nullable = false)
    private boolean signed;

    @Column(name = "signed_at")
    private Instant signedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected VisitNoteEntity() {
    }

    public VisitNoteEntity(UUID id, UUID clinicId, UUID appointmentId, UUID patientId, UUID providerId,
                           String templateCode) {
        super(clinicId);
        this.id = id;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.providerId = providerId;
        this.templateCode = templateCode;
        this.signed = false;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public String getSubjective() {
        return subjective;
    }

    public String getObjective() {
        return objective;
    }

    public String getAssessment() {
        return assessment;
    }

    public String getPlan() {
        return plan;
    }

    public boolean isSigned() {
        return signed;
    }

    public Instant getSignedAt() {
        return signedAt;
    }

    public void updateSections(String subjective, String objective, String assessment, String plan) {
        this.subjective = subjective;
        this.objective = objective;
        this.assessment = assessment;
        this.plan = plan;
        this.updatedAt = Instant.now();
    }

    public void sign() {
        this.signed = true;
        this.signedAt = Instant.now();
        this.updatedAt = this.signedAt;
    }
}
