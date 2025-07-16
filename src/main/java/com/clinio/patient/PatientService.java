package com.clinio.patient;

import com.clinio.auth.CurrentUserService;
import com.clinio.patient.domain.PatientRegistration;
import com.clinio.patient.dto.CreatePatientRequest;
import com.clinio.patient.dto.PatientResponse;
import com.clinio.patient.persistence.PatientEntity;
import com.clinio.patient.persistence.PatientRepository;
import com.clinio.shared.domain.DomainValidationException;
import com.clinio.shared.domain.PatientStatus;
import com.clinio.shared.security.ClinicSecurityService;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String MRN_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final PatientRepository patientRepository;
    private final CurrentUserService currentUserService;
    private final ClinicSecurityService clinicSecurityService;

    public PatientService(
            PatientRepository patientRepository,
            CurrentUserService currentUserService,
            ClinicSecurityService clinicSecurityService) {
        this.patientRepository = patientRepository;
        this.currentUserService = currentUserService;
        this.clinicSecurityService = clinicSecurityService;
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public PatientResponse register(CreatePatientRequest request) {
        UUID clinicId = currentUserService.requireClinicId();
        detectDuplicates(clinicId, request.lastName(), request.dateOfBirth(), request.phone());
        PatientStatus status = request.status() == null ? PatientStatus.ACTIVE : request.status();
        String mrn = generateUniqueMrn(clinicId);
        PatientEntity entity = new PatientEntity(
                UUID.randomUUID(),
                clinicId,
                mrn,
                request.firstName().trim(),
                request.lastName().trim(),
                request.dateOfBirth(),
                request.phone().trim(),
                status);
        entity.setEmail(request.email());
        entity.setEmergencyName(request.emergencyName());
        entity.setEmergencyPhone(request.emergencyPhone());
        entity.setEmergencyRelationship(request.emergencyRelationship());
        entity.setInsuranceCarrier(request.insuranceCarrier());
        entity.setInsurancePolicyNumber(request.insurancePolicyNumber());
        return toResponse(patientRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<PatientResponse> search(String query) {
        UUID clinicId = currentUserService.requireClinicId();
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return patientRepository.search(clinicId, query.trim()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canAccessClinic(#clinicId)")
    public PatientResponse get(UUID clinicId, UUID patientId) {
        UUID activeClinicId = currentUserService.requireClinicId();
        if (!activeClinicId.equals(clinicId)) {
            throw new DomainValidationException("clinic mismatch");
        }
        PatientEntity entity = patientRepository.findById(patientId)
                .filter(patient -> patient.getClinicId().equals(clinicId))
                .orElseThrow(() -> new DomainValidationException("patient not found"));
        return toResponse(entity);
    }

    private void detectDuplicates(UUID clinicId, String lastName, java.time.LocalDate dob, String phone) {
        List<PatientEntity> nameMatches =
                patientRepository.findByClinicIdAndLastNameIgnoreCaseAndDateOfBirth(clinicId, lastName, dob);
        if (!nameMatches.isEmpty()) {
            throw new DomainValidationException("possible duplicate patient by name and date of birth");
        }
        List<PatientEntity> phoneMatches = patientRepository.search(clinicId, phone);
        if (phoneMatches.stream().anyMatch(p -> p.getPhone().equals(phone))) {
            throw new DomainValidationException("possible duplicate patient by phone");
        }
    }

    private String generateUniqueMrn(UUID clinicId) {
        for (int attempt = 0; attempt < 20; attempt++) {
            String candidate = randomMrn();
            if (patientRepository.findByClinicIdAndMedicalRecordNumber(clinicId, candidate).isEmpty()) {
                return candidate;
            }
        }
        throw new IllegalStateException("unable to generate unique mrn");
    }

    private String randomMrn() {
        StringBuilder builder = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            builder.append(MRN_ALPHABET.charAt(RANDOM.nextInt(MRN_ALPHABET.length())));
        }
        return builder.toString();
    }

    private PatientResponse toResponse(PatientEntity entity) {
        return new PatientResponse(
                entity.getId(),
                entity.getMedicalRecordNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDateOfBirth(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getStatus());
    }
}
